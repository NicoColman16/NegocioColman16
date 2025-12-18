package com.Enum;

public enum TipoVenta {
    
    EFECTIVO("Efectivo"),
    TARJETA("Tarjeta"),
    RECARGA("Recarga"),
    TRANSFERENCIA("Transferencia"),
    BILLETERA("Billetera");
    
    private final String tipo;
    TipoVenta(String tipo){
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
   
}
