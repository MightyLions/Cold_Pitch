package com.ColdPitch.config.Auditing;

import com.ColdPitch.domain.entity.User;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()
            || authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.empty();
        }

        return Optional.of(authentication.getName());
    }
}
