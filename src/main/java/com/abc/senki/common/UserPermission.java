package com.abc.senki.common;

public enum UserPermission {
    USER_READ("user:read"),
    USER_WRITE("user:write"),
    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write"),
    SELLER_READ("seller:read"),
    SELLER_WRITE("seller:write");
    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
