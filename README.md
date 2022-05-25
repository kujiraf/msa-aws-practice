# msa-aws-practice

## 第三回 詳細なアーキテクチャと利用するサービス/ライブラリ

-
- `WebSecurityConfigurerAdapter` が非推奨となっている。代替案は以下
  - https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter

## 第四回 SpringSecurity を使ったカスタム設定

### 認証情報の検証・生成 (UserDetails, UserDetailsService)

- SprintSecurity の UserDetails を implements した CustomUserDetails を作成する
  - 認可情報の AuhorityList を保持する
- SprintSecurity の UserDetailsService を implements した CustomUserDetailsService を作成する
  - SprintSecurity にはリクエストパラメータの ID/PW の検証ロジックが組み込まれている。
    - 検証対象のモデルは、上記の UserDetails の実装クラスである
  - 認証の検証は、本 IF を実装することで可能。認証情報のモデルオブジェクトを SpringSecurity デフォルトで利用することはないので、UserDetails と UserDetailsService の実装は実質必須となる。

### ログイン成功時のハンドラ (LoginSuccessHandler)
