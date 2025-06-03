package org.example.parcial2.models;

import java.math.BigDecimal;

public class DetalleVenta {
    private int idDetalle;
    private int idVenta;
    private int idCancion;
    private BigDecimal precio;

    public DetalleVenta() {}

    public DetalleVenta(int idDetalle, int idVenta, int idCancion, BigDecimal precio) {
        this.idDetalle = idDetalle;
        this.idVenta = idVenta;
        this.idCancion = idCancion;
        this.precio = precio;
    }

    public int getIdDetalle() { return idDetalle; }
    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }

    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }

    public int getIdCancion() { return idCancion; }
    public void setIdCancion(int idCancion) { this.idCancion = idCancion; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
}
