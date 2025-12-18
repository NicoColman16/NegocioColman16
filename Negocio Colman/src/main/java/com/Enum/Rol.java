package com.Enum;

public enum Rol {
    
    USER("User"),
    ADMIN("Admin");

    private String rol;

    Rol (String rol){
        this.rol = rol;
    }

    public String getRol() {
        return rol;
    }

}
