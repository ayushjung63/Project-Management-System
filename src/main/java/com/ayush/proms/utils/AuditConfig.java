package com.ayush.proms.utils;

import com.ayush.proms.model.User;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AuditConfig {

    @Autowired
    private AuthenticationUtil authenticationUtil;

    @Bean
    public AuditorAware auditorAware() {
        return new SpringSecurityAuditorAware();
    }

    public class SpringSecurityAuditorAware implements AuditorAware {
        @Override
        public Optional<Long> getCurrentAuditor() {
            User currentUser = authenticationUtil.getCurrentUser();
            return Optional.ofNullable(currentUser.getId());
        }

    }
}
