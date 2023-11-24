package skhu.gdsc.securitypractice.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import skhu.gdsc.securitypractice.jwt.JwtAccessDeniedHandler;
import skhu.gdsc.securitypractice.jwt.JwtAuthenticationEntryPoint;
import skhu.gdsc.securitypractice.jwt.TokenProvider;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
  private final TokenProvider tokenProvider;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

  /*
  * 어노테이션 Bean과 Configuration에 대해 잘 몰라서 찾아보았다.
  * java의 bean도 있으니 헷갈림 유의. 스프링 @Bean은 스프링 IoC 컨테이너에서 관리되는 java객체이다.
  * 즉 개발자가 아니라 스프링이 제어권을 쥔 객체라 할 수 있다.
  * @Configuration은 spring에 bean을 등록할 수 있도록 해주는 설정 어노테이션이다. @Bean이 있는데 굳이
  * 이 어노테이션을 쓰는 이유는, 등록한 Bean을 싱글톤으로 유지시켜주기 때문이다. 싱글톤으로 등록되면 객체를 한 번만 등록시켜주기 때문에
  * 원치 않는 중복호출이 일어나지 않는다.
  *
  * 다시 말해, 이 클래스는 데이터에 대한 보안 클래스이다.
  * */

  //Bcrypt 해싱 함수로 비밀번호를 인코딩해주는 메소드다. 사용자가 제출한 비밀번호와
  //저장소에 저장된 비밀번호의 일치여부도 확인시켜준다고 한다.
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /*
  * 뭔가 했더니 이전과는 보안 설정이 다르게 바뀌었다고 한다.
  * SecurityFilterChain을 반환하고 Bean으로 등록함으로써 컴포넌트 기반의 보안설정이 가능해진다.
  * 다시 말해서, 웹어플리케이션의 보안을 설정하는 부분이다.
  * */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf().disable() //csrf 보호 비활성화

            .exceptionHandling() //예외 처리 구성 시작
            .authenticationEntryPoint(jwtAuthenticationEntryPoint) //인증받지 않은 사용자가 접근 시 실행되는 entrypoint
            .accessDeniedHandler(jwtAccessDeniedHandler)//인증받은 사용자가 인가되지 않은 리소스에 접근 시 실행되는 핸들러

            .and()
            .sessionManagement() //세션관리 구성 시작
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션을 생성하지 않고 securitycontext를 http session에
            //저장하지 않도록함(stateless)

            .and()
            .authorizeHttpRequests()//HttpRequest에 대한 접근 제어 구성
            .requestMatchers("/login", "/signup/*").permitAll() //해당 경로의 모든 요청 허용
            .requestMatchers("/admin").hasAuthority("ROLE_ADMIN") // /admin경로의 요청은 role_admin 권한이 있어야 함.
            .anyRequest().authenticated()//다른 모든 요청은 인증된 사용자만 접근가능

            .and()
            .apply(new JwtSecurityConfig(tokenProvider)); //jwt 보안 구성을 적용

    return http.build(); //SecurityFilterChain 반환
  }
}
