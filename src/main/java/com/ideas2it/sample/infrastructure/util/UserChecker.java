package com.ideas2it.sample.infrastructure.util;

import com.ideas2it.sample.infrastructure.helper.JwtHelper;
import com.ideas2it.sample.infrastructure.exception.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class UserChecker {
    public void ensureAdminAccess() {
        if (!JwtHelper.isAdmin()) {
            throw new AccessDeniedException("Access denied: Only admins are allowed to perform this operation.");
        }
    }

    public void ensureUserAccess() {
        if (!JwtHelper.isUser()) {
            throw new AccessDeniedException("Access denied: Only users are allowed to perform this operation.");
        }
    }
}
