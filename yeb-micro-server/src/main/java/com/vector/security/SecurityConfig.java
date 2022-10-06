package com.vector.security;

import com.vector.handler.*;
import com.vector.security.username.UsernameFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // 构造注入自定义userDetailsService实现
    @Resource
    @Lazy
    private UserDetailsService userDetailsService;
    @Resource
    private DataSource dataSource;
    @Resource
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Resource
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    @Resource
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    @Resource
    private RestAuthenticationSuccess restAuthenticationSuccess;
    @Resource
    private RestAuthenticationFailure restAuthenticationFailure;
//    @Resource
//    private PermissionFilter permissionFilter;
//    @Resource
//    private AccessDecisionManager accessDecisionManager;

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

    @Override
    // 认证管理器 自定义AuthenticationManager
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    // 用来将自定义AuthenticationManager在工厂中进行暴露,可以在任何位置注入
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 自定义filter工厂
     *
     * @return LoginFilter
     */
    @Bean
    public UsernameFilter usernameFilter() throws Exception {
        UsernameFilter usernameFilter = new UsernameFilter();
        usernameFilter.setAuthenticationManager(authenticationManagerBean());
        usernameFilter.setAuthenticationSuccessHandler(restAuthenticationSuccess); // 认证成功处理
        usernameFilter.setAuthenticationFailureHandler(restAuthenticationFailure); // 认证失败处理
        return usernameFilter;
    }

    @Override
    public void configure(WebSecurity web) {
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
//                // 动态权限配置
//                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
//                    @Override
//                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
//                        object.setAccessDecisionManager(accessDecisionManager);
//                        object.setSecurityMetadataSource(permissionFilter);
//                        return object;
//                    }
//                })
                .and()
                .cors()
                .and()
                // 关闭csrf
                .csrf().disable()
                // 基于token,不需要session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers()
                .cacheControl();// 缓存关闭

        /**
         * at: 用来将某个 filter 替换过滤器链中哪一个 filter
         * before: 放在过滤器链哪一个filter之前
         * after: 放在过滤器链哪一个filter之后
         */
        http.addFilterAt(usernameFilter(), UsernamePasswordAuthenticationFilter.class);
        // 添加jwt 登录授权过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernameFilter.class);
        // 添加自定义未授权和未登录结果返回
        http.exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint) // 认证失败异常处理器
                .accessDeniedHandler(restfulAccessDeniedHandler); // 授权失败异常处理器
    }
}
