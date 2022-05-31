# msa-aws-practice

## ベースパッケージの表記

ベースパッケージを`${pkg}`と表す。実際のベースパッケージは以下の通り。

- `msa-frontend`の場合、`com.example.msa.frontend`
- `backend-user-service`の場合、`com.example.msa.backend.usersvc`
- `common`の場合、`com.example.msa.common`

## Spring Security メモ

- 認証のアーキテクチャは[この通り](http://terasolunaorg.github.io/guideline/current/ja/Security/Authentication.html#db)
- `SecurityConfig`の`SecurityFilterChain`Bean にで`loginForm()`を設定している。この設定により、`FormLoginConfigurer`にてデフォルトで`UserPasswordAuthenticationFilter`が設定される
  - [terrasoluna の設定](http://terasolunaorg.github.io/guideline/current/ja/Security/Authentication.html#form-login-usage)だと `<sec:form-login />`に値する
  - これによりフォーム認証が可能となる

### フォームログイン

フォームログインの流れは[この通り](http://terasolunaorg.github.io/guideline/current/ja/Security/Authentication.html#form-login)

1. `UserPasswordAuthenticationFilter`が、認証処理を実行する`AuthenticationManager`に処理を委譲する。実際の処理は`AuthenticationProvider`（今回の実装は`DaoAuthenticationProvider`）が行う。
1. `UserPasswordAuthenticationFilter`が 1 の認証結果を受けて、`Authentication(Success/Failure)Handler`のメソッドを呼び出し、画面遷移を行う

### DB 認証

- DB 認証の流れは[この通り](http://terasolunaorg.github.io/guideline/current/ja/Security/Authentication.html#db)
- このハンズオンでは、フォームログイン時の username/password を、バックエンドサービスの DB から取得した資格情報に照合する認証を行っている。

### 参考

- [terrasoluna](http://terasolunaorg.github.io/guideline/current/ja/Security/Authentication.html#pbkdf2passwordencoder)

## 第三回 詳細なアーキテクチャと利用するサービス/ライブラリ

- `WebSecurityConfigurerAdapter` が非推奨となっている。代替案は Spring の[公式記事](https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter)より
- `AuthnticationManagerBuilder` も当然 Override ではなく Bean 定義する必要あり。この記事では、`DaoAuthenticationProvider` が利用される。
  - `DaoAuthenticationProvider` は、 `AuthenticationManager` から処理を委譲される ([terasoluna 参考](https://terasolunaorg.github.io/guideline/5.2.0.RELEASE/ja/Security/Authentication.html#id3))
  - 上記のため、`AuthenticationManager` に `DaoAuthenticationProvider` を設定した Bean を定義する必要がある。
    - `AuthenticationManager` に、`DaoAuthenticationProvider`の拡張クラスの Bean を登録する ([terasoluna の設定例](http://terasolunaorg.github.io/guideline/current/ja/Security/Authentication.html#id48))
    - 今回は `UserDetailsService`と`PasswordEncoder`を登録したいだけなので、以下のように実装した。
      ```java
      @Bean
      public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailService());
        return new ProviderManager(provider);
      }
      ```

## 第四回 SpringSecurity を使ったカスタム設定

### 認証情報の検証・生成 (UserDetails, UserDetailsService)

- SprintSecurity の UserDetails を implements した CustomUserDetails を作成する
  - 認可情報の AuhorityList を保持する
- SprintSecurity の UserDetailsService を implements した CustomUserDetailsService を作成する
  - SprintSecurity にはリクエストパラメータの ID/PW の検証ロジックが組み込まれている。
    - 検証対象のモデルは、上記の UserDetails の実装クラスである
  - 認証の検証は、本 IF を実装することで可能。認証情報のモデルオブジェクトを SpringSecurity デフォルトで利用することはないので、UserDetails と UserDetailsService の実装は実質必須となる。

### ログイン成功時のハンドラ (LoginSuccessHandler)

- ログイン成功時のリダイレクトパスを設定する。

### エラー処理のカスタマイズ

- `LoginUrlAuthenticationEntryPoint` を拡張することで、ログイン時のエラー処理のカスタマイズが可能

## 第五回 バックエンドマイクロサービスの実装

ユーザ情報をリソースとして扱うバックエンドのマイクロサービスを作成する。

### 設定クラスの作成

`${pkg}.config`に設定クラス作成する

- 環境プロファイルの設定
  - `DevConfig`にて設定する
  - ローカル実行用に`@Profile`アノテーションにて、`dev`プロファイルを定義する
    - dev 環境では、インメモリ DB の HSQL を利用する
    - `DataSource`の bean 定義をする。bean 定義に HSQL の設定を入れる。
- コンポーネントスキャンの設定
  - `MvcConfig`, `JpaConfig`, `DomainCOnfig`にそれぞれコンポーネントスキャン対象のベースパッケージを設定する
- JPA の設定
  - `JpaConfig`では、次の設定を行っている
    - JPA 用のトランザクションマネージャーの Bean 登録
    - JPA EntityManagerFactory のセットアップ
      - スキャン対象のパッケージ指定、プロパティ、データソースなどの定義

### コントローラ層の作成

- `${pkg}.app.web`にコントローラクラスを作成
  - ユーザ ID やログイン ID に応じたユーザリソースを返却する REST Controller
- `@GetMapping`でパスとメソッドのマッピング
- `@PathValiable`, `@RequestParam`, `@RequestBody`で引数をリクエストから取得している

### ドメイン層の作成

- `${pkg}.domain`配下にサービスやリポジトリを作成する
  - サービスは普通にリポジトリで DB にアクセスするだけ
  - リポジトリは JPA を利用

### 認証情報を保持するクラス

- バックエンドサービスは、（※terasoluna の方針に基づき）DB の TBL の１レコードを表現するエンティティクラスを domain 層に作成する
  - `${pkg}.domain.entity`パッケージに`User`、 `Credential`、 `CredentialPK` クラスを作成する。User:Credential=1:多の関係になる。
  - 下記の`common`のリソースと、エンティティの変換関数を持つクラスも作成する（`XxxMapper`）
- 上記の認証情報をフロントエンド・バックエンド双方で参照できるように、`common`プロジェクトに認証情報を保持するための`Resource`クラスを作成する
  - `${pkg}.model`パッケージに`UserResource`と`CredentialResource`クラスを作成する

### 例外処理の一元的な設定

- 例外処理という共通処理のため、`common`プロジェクトにハンドラや例外レスポンスクラスを作成する
  - クラスは`${pkg}.apinfra.exception`にまとめる
- マイクロサービスで発生した例外は`@ControllerAdvice`を付与した`ExceptionHandler`で一元的に設定可能（[参考](https://qiita.com/niwasawa/items/f5a6a285d7bd99e8273a)）

  - `@ExceptinHandler`アノテーションにて、捕捉対象の例外クラスを指定する
  - Controller クラスは try-catch をかかずに例外をスローすればよい

- 今回のサンプル実装では、`RuntimeException`は捕捉しない。

  - Spring がデフォルトで INTERNAL_SERVER_ERROR としてシステム例外を処理しているため

### その他実装 Tips

- エラー情報の出力には `jackson` を利用
  - `ErrorResponse`には、型情報を JSON に出力するための`@JsonTypeInfo`を利用している。（[参考](https://qiita.com/opengl-8080/items/b613b9b3bc5d796c840c#%E5%9E%8B%E6%83%85%E5%A0%B1%E3%82%92-json-%E3%81%AB%E5%87%BA%E5%8A%9B%E3%81%99%E3%82%8B)）
  - `@JsonSubTypes`で、型ごとに Json 出力の値を指定可能
    - ただし、`@JsonSubTypes`はインタフェースや抽象クラスに具象クラス名が入ってしまう。`@JsonTypeName`を使えば、具象クラス側で名前の設定が可能。
- pom に以下を書いていた状態で実行したら 401 エラーとなってしまった。デフォルトで認証が必要とされる模様。
  ```xml
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
  </dependency>
  ```

## 第六回 Web アプリケーションからマイクロサービスを呼び出す(1)

第３，４回で作成したフロントエンドを修正して、バックエンドからユーザリソースを使って認証処理を行う。
（１）ではバックエンドのサービスからリソースを取得する実装をする。

### ドメイン層の実装

- `${pkg}.domain`配下にサービスとリポジトリを作成する
- サービスはいつも通りリポジトリからデータを取得するように実装する
  - `OrchestratioinService(Impl)`クラスを作成する。Web アプリのビジネスロジックのトランザクション境界として実装され、リトライ制御や補償トランザクションなどの役割を持つ。
  - リソースが DB なのかバックエンドのサービスからの取得になるのかはリポジトリ側で隠ぺいするので、サービスは意識しない
- リポジトリはバックエンドのユーザサービスから（REST API で）リソースを取得する。
  - リソースの取得には、`webflux`の`WebClient`を利用している
  - pom に webflux の dependency を追加した

### WebClient の設定

- `application.yaml`にバックエンドを探すための DNS を追加
- `DevConfig`を作成し、WebClient のアクセス先を`application.yaml`から読み込むように設定

## 第七回 Web アプリケーションからマイクロサービスを呼び出す(2)

第３，４回で作成したフロントエンドを修正して、バックエンドからユーザリソースを使って認証処理を行う。
（２）では SpringSecurity の修正をする

### `CustomUserDetailsService`の修正

- `OrchestrationService`を利用して、バックエンドのサービスから`UserResource`を取得する。
- `CustomUserDetails`を作成する。この際、取得したユーザリソースが保持する権限に応じて適切な権限を付与する。

### `CustomUserDetails`からのユーザ情報取得の修正

前回まではサンプルで固定のユーザ名/パスワードを取得していたので修正する。また、パスワードエンコーダの設定も行う。

#### `CustomUserDetails`の処理修正

- `getPassword`メソッドで`{noop}xxx`でサンプルデータの返却から、`UserResource`から取得したパスワードを返却するように修正する。
- `getUserName`メソッドも上記同様に、固定値ではなくユーザリソースから取得するように修正する

#### `SecurityConfig`の`PasswordEncoder` Bean の修正

- デフォルトのエンコーダから terrasoluna 推奨の PBKDF2 のパスワードエンコーダを利用する
- 実装は terrasoluna の[ガイド参照](http://terasolunaorg.github.io/guideline/current/ja/Security/Authentication.html#delegatingpasswordencoder)
  ```java
  @Bean
  public PasswordEncoder passwordEncoder() {
    Map<String, PasswordEncoder> encoders = new HashMap<>();
    encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
    encoders.put("bcrypt", new BCryptPasswordEncoder());
    return new DelegatingPasswordEncoder("pbkdf2", encoders);
  }
  ```
- 上記の例は terasoluna 5.7.1 の推奨設定。ただし、`DelegatingPasswordEncoder`を利用する場合、パスワードのハッシュ値に平文で`{pbkdf2}`等のプレフィックスがついている必要がある。
  - 既に運用されているシステムなど、PW にプレフィックスがついていない場合は、`DelegatingPasswordEncoder`ではなく、直接`Pbkdf2PasswordEncoder`などを Bean 定義してあげる必要がある

##### _*PBKDF2 について*_

- 計算コストを変動させることが可能であり、暗号化する際に、総当たり攻撃に対する脆弱性を軽減することを目的として使用される鍵導出関数のこと。
- 2017 年に公開された RFC 8018 (PKCS #5 v2.1)は、パスワードのハッシュ化には、PBKDF2 を利用することを推奨している。

### Controller の修正

- `CustomUserDetails`を引数のパラメータとして受け取り、画面へモデルとして渡すように修正する
- `@AuthenticationPrincipal`にて、コントローラのメソッドの引数として取得できる

### `SetMenuInterceptor`の作成

Controller の処理実行と、View のレンダリング処理の間に処理を挟み込み、ユーザリソースの権限に応じて描画する画面を変えるための Interceptor を作成する

#### Interceptor の概要

- Interceptor は Controller の前後で共通処理を追加するための仕組み（[参考](https://b1san-blog.com/post/spring/spring-interceptor/)）
- Interceptor の実装には、`HandlerInterceptor`を implements する
- `preHandler`はコントローラ実行前に処理を行う
  - リクエストのログ出力や認可処理などを実施する
  - 戻り値が`true`の場合はコントローラの処理を実行し、`false`の場合はコントローラの処理を実行せずに`200`を返却する
- `postHandler`はコントローラ実行後に処理を行う
  - MVC の場合はテンプレートエンジンによるレンダリングの前の処理となる
  - RestAPI の場合はレスポンス送信前の処理となる

#### Interceptor の実装内容

- Controller クラスで利用するため、`${pkg}.app.web.interceptor`パッケージに作成する
  - ※Controller は`${pkg}.app.web`にある
- `CustomUserDetails`が保持するロールに応じて画面に表示させるメニューリストを生成するインターセプタを作成する
  - `UserDetails`は、`SecurityContextHolder`から任意の場所で取得することが可能
- 上記で参照するロールは、`CustomUserDetailsService`で`GrantedAuthority`のリストにセットしたものである

### `MvcConfig`の修正

- Interceptor の実行には、`WebMvcConfigurer`の実装クラスに登録する必要があるため、設定を追加する
  - `SetMenuInterceptor`の Bean 定義を行う
  - `InterceptorRegistry`へ上記 Bean を追加する設定を加える
- Interceptor は複数登録することも可能
  ```java
  registry.addInterceptor(myInterceptor1());
  registry.addInterceptor(myInterceptor2());
  ```

# Tips

## Eclipse のプロセスが生き残って AP 実行できない場合

```cmd
netstat -ao | find "[port]"
taskkill /PID [pid]
```

## Eclipse のショートカット

https://yulii.github.io/eclipse-shortcut-keys-20120814.html
