package com.Enum;
    
public enum TipoProducto {
    ALIMENTO("Alimento"),
    CONDIMENTO("Condimento"),
    HELADO("Helado"),
    ALFAJOR("Alfajor"), 
    LACTEOS("Lacteo"),
    BEBIDAS("Bebidas"),
    FIAMBRERIA("Fiambreria"),
    PANADERIA("Panaderia"),
    PERFUMERIA("Perfumeria"),
    BEBIDAS_ALCOHOLICAS("Bebidas alcoholicas"),
    GOLOSINA("Golosina"),
    MEDICAMENTO("Medicamentos"),
    MASITA("Masita"),
    PASTAS("Pastas"),
    HARINAS("Harinas"),
    CONSERVANTES("Conservantes"),
    DULCE("Dulce"), //Duda
    LIMPIEZA("Limpieza"),
    AEROSOL("Aerosol"),
    ADEREZO("Aderezo"),
    COPETIN("Copetin"),
    LIBRERIA("Libreria"),
    CIGARRILLOS("Cigarrillos"),
    BEBE("Bebe"),
    GENERAL("General");
    private final String label;

    TipoProducto(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}



