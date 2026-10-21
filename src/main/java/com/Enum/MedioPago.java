package com.Enum;


public enum MedioPago {
    EFECTIVO("Efectivo"),
    MERCADO_PAGO("Mercado Pago"),
    DEBITO("Debito"),
    CREDITO("Credito"),
    TARJETA("Tarjeta"),
    TRANSFERENCIA("Transferencia"),
    RECARGA("Recarga");

     private final String label;

    MedioPago(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

