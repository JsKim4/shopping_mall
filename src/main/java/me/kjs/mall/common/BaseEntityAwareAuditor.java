package me.kjs.mall.common;

import me.kjs.mall.security.AccountAdapter;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BaseEntityAwareAuditor implements AuditorAware<BaseEntityDateTime> {
    @Override
    public Optional<BaseEntityDateTime> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().getClass() != AccountAdapter.class) {
            return Optional.ofNullable(null);
        }
        return Optional.ofNullable(((AccountAdapter) authentication.getPrincipal()).getMember());
    }
}
