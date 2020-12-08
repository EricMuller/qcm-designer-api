package com.emu.apps.shared.security;

public final class AuthentificationContextHolder {

    private static ThreadLocal <String> context = new ThreadLocal <>();

    private AuthentificationContextHolder() {
    }

    public static  String getPrincipal() {
        return context.get();
    }

    public static void setPrincipal(String user) {
        context.set(user);
    }
}
