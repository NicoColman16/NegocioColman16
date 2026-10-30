package com.Enum;
    
public enum TipoProducto {
  
    AGUILA("Aguila"),
    BARTOLITO("Bartolito"),
    BLASPARERA("Blas Parera"),
    BLOQUE("Bloque"),
    CIGARRILLOS("Cigarrillos"),
    FIAMBRERIA("Fiambreria"),
    GABILUCHO("Gabilucho"),
    GOLOSINA("Golosina"),
    VENETO("Veneto"),
    JYM("J y M Fiambre"),
    LACTEOS("Lacteo"),
    LIBRERIA("Libreria"),
    MEDICAMENTO("Medicamentos"),
    MURAT("Murat"),
    OA("O.A. Distribuciones"),
    PASTAS("Pastas"),
    PONCE ("Ponce"),
    TIOLUIS("Tio Luis");
    
    private final String label;

    TipoProducto(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}



