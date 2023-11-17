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

/*
@Configuration이라고 하면 설정파일을 만들기 위한 애노테이션 or Bean을 등록하기 위한 애노테이션
싱글톤이 유지 되도록 해줌
싱글톤 패턴: 생성자가 여러 차례 호출되더라도 실제로 생성되는 객체는 하나이고 최초 생성 이후에 호출된 생성자는 최초의 생성자가 생성한 객체를 리턴
    주로 공통된 객체를 여러개 생성해서 사용하는 DBCP(DataBase Connection Pool)와 같은 상황에서 많이 사용

    장점:
    -메모리 측면의 이점
    -속도 측면의 이점
    -데이터 공유가 쉽다
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
  private final TokenProvider tokenProvider;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

  /*@Bean, PasswordEncoder
  @Bean과 @Component 둘다 Spring(IOC) Container에 Bean을 등록하도록 하는 메타데이터를 기입하는 어노테이션
  @Bean: 개발자가 직접 제어가 불가능한 외부 라이브러리등을 Bean으로 만들려할 때 사용
  @Component: 경우 개발자가 직접 작성한 Class를 Bean으로 등록하기 위한 어노테이션

  PasswordEncoder: 스프링 시큐리티의 인터페이스 객체, 비밀번호를 암호화 하는 역할
    PasswordEncoder의 구현체를 대입해주고 이를 스프링 빈으로 등록하는 과정이 필요
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(); // 비밀번호를 암호화하는 데 사용할 수 있는 메서드를 가진 클래스 반환
  }

/* SecurityFilterChain
WebSecurityConfigurerAdapter는 컴포넌트 기반의 보안 설정을 권장한다는 이유로 Deprecated 됐다.
Spring Security는 대신에 SecurityFilterChain을 사용하기를 권장한다.
SecurityFilterChain을 반환하고 빈으로 등록함으로써 컴포넌트 기반의 보안 설정이 가능해진다.
 */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    /*CSRF
    Cross site Request forgery로 사이트 간 위조 요청(정상적인 사용자가 의도치 않은 위조요청을 보내는 것을 의미)
    CSRF protection은 spring security에서 default로 설정, 위조 요청을 방지
    CSRF를 disable한 이유:
    spring security documentation에 non-browser clients 만을 위한 서비스라면 csrf를 disable 하여도 좋다고 한다.
    이 이유는 rest api를 이용한 서버라면, session 기반 인증과는 다르게 stateless(무상태)하기 때문에 서버에 인증정보를 보관하지 않는다.
     */
    http.csrf().disable()
            // 예외처리(exception handling)
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 인증 예외
            .accessDeniedHandler(jwtAccessDeniedHandler) // 인가 예외

            .and()
            .sessionManagement() // 세션 설정 시작
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 생성 정책 설정

            // HTTP 요쳥에 대한 인가 설정을 구체적으로 지정할 수 있도록 도움
            .and()
            // 메서드의 역할, HTTP 요청에 대한 인가 설정을 구성하는데 사용, 다양한 인가 규칙 정의, 경로별 다른 권한 설정 가능
            .authorizeHttpRequests()
            // requestMatchers: 특정한 HTTP 요청 매처를 적용할 수 있게 해줌, 매처를 지정하여 특정 경로나 URL 패턴에 대한 인가 규칙을 설정할 수 있다.
            /* permitAll: 해당 경로에 대한 모든 요청을 인가함, 즉 인증된 사용자나 권한에 상관없이 모든 사용자가 접근할 수 있게 허용
            특정 경로를 예외로 처리하고자 할 때 주로 사용, ex) 로그인 페이지, 정적 리소스에 대한 접근 허용에 유용함
            */
            .requestMatchers("/login", "/signup/*").permitAll()
            .requestMatchers("/admin").hasAuthority("ROLE_ADMIN")
            .anyRequest().authenticated() // 모든 리소스를 의미하며 접근허용 리소스 및 인증후 특정 레벨의 권한을 가진 사용자만 접근가능한 리소스를 설정하고 그외 나머지 리소스들은 무조건 인증을 완료해야 접근이 가능하다는 의미

            .and()
            .apply(new JwtSecurityConfig(tokenProvider)); // JWT Filter 를 추가

    return http.build();
  }
}
