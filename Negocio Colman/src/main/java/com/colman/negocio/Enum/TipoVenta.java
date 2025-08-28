package com.colman.negocio.Enum;

public enum TipoVenta {
    
    EFECTIVO("Efectivo"),
    TARJETA("Tarjeta"),
    VIRTUAL("Virtual"),
    BILLETERA("Billetera");
    
    private final String tipo;
    TipoVenta(String tipo){
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
   
}
