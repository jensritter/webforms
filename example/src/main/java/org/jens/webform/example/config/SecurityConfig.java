package org.jens.webform.example.config;

import org.jens.shorthand.security.config.ShorthandSecurityDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author Jens Ritter on 16/03/2021.
 */
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception {
        return ShorthandSecurityDefaults.ignoreUrls(http, "/**");
    }

}
