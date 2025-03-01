package com.abc.senki.common;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.abc.senki.common.UserPermission.*;


public enum AppUserRole {
    USER(Sets.newHashSet(USER_READ, USER_WRITE)),
    ADMIN(Sets.newHashSet(ADMIN_READ, ADMIN_WRITE, USER_READ, USER_WRITE,SHIPPER_READ,SHIPPER_WRITE)),

    SELLER(Sets.newHashSet(SELLER_READ, SELLER_WRITE, USER_READ, USER_WRITE)),

    SHIPPER(Sets.newHashSet(SHIPPER_READ,SHIPPER_WRITE,USER_READ,USER_WRITE));

    private final Set<UserPermission> permissions;

    AppUserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        //permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}