package org.example.parcial2.models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Venta {
    private int idVenta;
    private Timestamp fechaVenta;
    private BigDecimal totalVenta;
    private int idUser;

    public Venta() {}

    public Venta(int idVenta, Timestamp fechaVenta, BigDecimal totalVenta, int idUser) {
        this.idVenta = idVenta;
        this.fechaVenta = fechaVenta;
        this.totalVenta = totalVenta;
        this.idUser = idUser;
    }

    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }

    public Timestamp getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(Timestamp fechaVenta) { this.fechaVenta = fechaVenta; }

    public BigDecimal getTotalVenta() { return totalVenta; }
    public void setTotalVenta(BigDecimal totalVenta) { this.totalVenta = totalVenta; }

    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }
}
