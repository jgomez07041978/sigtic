package mx.gob.tabasco.cmaig.sigtic.sas.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
/*@EnableGlobalMethodSecurity(prePostEnabled=true)*/
public class WebSecurityConfig {


    private final UserDetailsServiceImp userDetailsService;
    private final AccessDecisionManager sigticAccessDecisionManager;
    private final AccessDeniedHandler sigticAccessDeniedHandler;
    private final AuthenticationFailureHandler sigticAutenticationFailureHandler;
    private final AuthenticationSuccessHandler sigticAutenticationSuccessHandler;
    private final FilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource;

    String[] resources = new String[]{
            "/include/**","/js/**"
    };

    public WebSecurityConfig(UserDetailsServiceImp userDetailsService, AccessDecisionManager sigticAccessDecisionManager, AccessDeniedHandler sigticAccessDeniedHandler, AuthenticationFailureHandler sigticAutenticationFailureHandler, AuthenticationSuccessHandler sigticAutenticationSuccessHandler, FilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource) {
        this.userDetailsService = userDetailsService;
        this.sigticAccessDecisionManager = sigticAccessDecisionManager;
        this.sigticAccessDeniedHandler = sigticAccessDeniedHandler;
        this.sigticAutenticationFailureHandler = sigticAutenticationFailureHandler;
        this.sigticAutenticationSuccessHandler = sigticAutenticationSuccessHandler;
        this.urlFilterInvocationSecurityMetadataSource = urlFilterInvocationSecurityMetadataSource;
    }


    /*@Bean
    public AuthenticationFailureHandler sigticAutenticationFailureHandler(){
        return new SigticAutenticationFailureHandler();
    }*/

    /*
    @Bean
    public AuthenticationSuccessHandler sigticAutenticationSuccessHandler(){
        return new SigticAutenticationSuccessHandler();
    }*/

  /*  @Bean
    public AccessDeniedHandler sigticAccessDeniedHandler(){
        return new SigticAccessDeniedHandler();
    }*/
/*
    @Bean
    public AccessDecisionManager sigticAccessDecisionManager() {
        return new SigticAccessDecisionManager();
    }
*/


    @Bean
    public FilterSecurityInterceptor filterSecurityInterceptor(){
        FilterSecurityInterceptor filterSecurityInterceptor  = new FilterSecurityInterceptor();
        filterSecurityInterceptor .setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource);
        filterSecurityInterceptor .setAccessDecisionManager(sigticAccessDecisionManager);
        return filterSecurityInterceptor ;
    }

   /* @Bean
    public FilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource() {
        return new UrlFilterInvocationSecurityMetadataSource();
    }*/


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.anonymous().disable();
        http.headers().frameOptions().sameOrigin();
        http.csrf(csrf -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/api/v1/notificacionMovil/save"),
                                new AntPathRequestMatcher("/api/v1/notificacionMovil/sendMessage")
                                )
                );

        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(filterSecurityInterceptor(), FilterSecurityInterceptor.class);
        http.exceptionHandling().accessDeniedHandler(sigticAccessDeniedHandler);

        http.formLogin()
                //.defaultSuccessUrl("/inicio")
                .failureUrl("/login?error")
                .failureHandler(sigticAutenticationFailureHandler)
                .loginPage("/login")
                .permitAll()
                .successHandler(sigticAutenticationSuccessHandler)
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login").permitAll();


        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(9);
//El numero 4 representa que tan fuerte quieres la encriptacion.
//Se puede en un rango entre 4 y 31.
//Si no pones un numero el programa utilizara uno aleatoriamente cada vez
//que inicies la aplicacion, por lo cual tus contrasenas encriptadas no funcionaran bien
        return bCryptPasswordEncoder;
    }

    //Registra el service para usuarios y el encriptador de contrasena
    @Autowired
    public void configureGlobal (AuthenticationManagerBuilder auth) throws Exception {
        // Setting Service to find User in the database.
        // And Setting PassswordEncoder
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}
