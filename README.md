# msa-aws-practice

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
