package com.abc.senki.security.dto;

import com.abc.senki.common.AppUserRole;
import com.abc.senki.model.entity.RoleEntity;
import com.abc.senki.model.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AppUserDetail implements UserDetails {
    private static final long serialVersionUID = 1L;
    @JsonIgnore
    private UUID id;
    private String username;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private Collection<? extends GrantedAuthority> authorities;
    private Collection<String> roles;
    public AppUserDetail(UUID id, String username,  String password,
                         Collection<? extends GrantedAuthority> authorities, Collection<String> roles, Boolean active, Boolean isAccountNonLocked) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.roles = roles;
    }
    public static AppUserDetail build(UserEntity user) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        Set<String> roleNames = new HashSet<>();

        for(RoleEntity role : user.getRoles()){
            roleNames.add(role.getName());
            for(AppUserRole item : AppUserRole.values()){
                if(role.getName().equals(item.name())){
                    authorities.addAll(item.getGrantedAuthorities());
                }
            }
        }


        return new AppUserDetail(
                user.getId(),
                user.getPhone(),
                user.getPassword(),
                authorities,
                roleNames,
                user.isActive(),
                user.isStatus());
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    public Collection<String> getRoles() {
        return roles;
    }

    public UUID getId() {
        return id;
    }
}
