package com.Enum;
    
public enum TipoProducto {
    ADEREZO("Aderezo"),
    AEROSOL("Aerosol"),
    AGUILA("Aguila"),
    ALFAJOR("Alfajor"), 
    ALIMENTO("Alimento"),
    BARTOLITO("Bartolito"),
    BEBE("Bebe"),
    BEBIDAS("Bebidas"),
    BEBIDAS_ALCOHOLICAS("Bebidas alcoholicas"),
    BLASPARERA("Blas Parera"),
    BLOQUE("Bloque"),
    CIGARRILLOS("Cigarrillos"),
    CONDIMENTO("Condimento"),
    CONSERVANTES("Conservantes"),
    COPETIN("Copetin"),
    DULCE("Dulce"), //Duda
    FIAMBRERIA("Fiambreria"),
    GABILUCHO("Gabilucho"),
    GENERAL("General"),
    GOLOSINA("Golosina"),
    HARINAS("Harinas"),
    HELADO("Helado"),
    JYM("J y M Fiambre"),
    LACTEOS("Lacteo"),
    LIBRERIA("Libreria"),
    LIMPIEZA("Limpieza"),
    MASITA("Masita"),
    MEDICAMENTO("Medicamentos"),
    MURAT("Murat"),
    OA("O.A. Distribuciones"),
    PANADERIA("Panaderia"),
    PASTAS("Pastas"),
    PERFUMERIA("Perfumeria"),
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



