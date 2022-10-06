package com.vector.config;

import com.vector.handler.JwtAuthenticationTokenFilter;
import com.vector.handler.PermissionFilter;
import com.vector.handler.RestAuthenticationEntryPoint;
import com.vector.handler.RestfulAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author WangJiaHui
 * @description: security配置类
 * @ClassName SercurityConfig
 * @date 2022/9/7 20:30
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    @Lazy
    private UserDetailsService userDetailsService;

    private final DataSource dataSource;
    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    private final PermissionFilter permissionFilter;
    private final AccessDecisionManager accessDecisionManager;

    public SecurityConfig(DataSource dataSource,
                          JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter,
                          RestAuthenticationEntryPoint restAuthenticationEntryPoint,
                          RestfulAccessDeniedHandler restfulAccessDeniedHandler,
                          PermissionFilter permissionFilter,
                          AccessDecisionManager accessDecisionManager){
        this.dataSource = dataSource;
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.restfulAccessDeniedHandler = restfulAccessDeniedHandler;
        this.permissionFilter = permissionFilter;
        this.accessDecisionManager = accessDecisionManager;
    }
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Bean
    // 注入BCryptPasswordEncoder编码器
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Override
    // 认证管理器
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web){
        // 放行路径,不走过滤器链
        web.ignoring().antMatchers(
                "/login",
                "/logout",
                "css/**",
                "js/**",
                "/index.html",
                "favicon.ico",
                "/captcha");
    }
    // 重写configure(HttpSecurity http) 自定义登录页面
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated() // 除上述放行的url,其余全部鉴权认证
                // 动态权限配置
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setAccessDecisionManager(accessDecisionManager);
                        object.setSecurityMetadataSource(permissionFilter);
                        return object;
                    }
                })
                .and()
                // 关闭csrf
                .csrf().disable()
                // 基于token,不需要session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers()
                .cacheControl();// 缓存关闭
        // 添加jwt 登录授权过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 添加自定义未授权和未登录结果返回
        http.exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint) // 认证失败异常处理器
                .accessDeniedHandler(restfulAccessDeniedHandler); // 授权失败异常处理器
    }
}
