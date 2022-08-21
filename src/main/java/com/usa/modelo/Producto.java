package com.usa.modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 *
 * @author Anderson Londoño
 */

@Table("productos")
public class Producto {
    
    @Id
    @Column("codigo")     // si al @Id se le pone @Column este en la base de datos debe ser autoIncremental  
    private int codigo;   // si se quiere poner el codigo debo crear otro atributo id para que este sea 
    @Column("nombre")     // autoIncremental y no me de conflito cuando quiero ingresar el codigo
    private String nombre;
    @Column("precio")
    private double precio;
    @Column("inventario")
    private int inventario;

    public Producto() {
    }

    public Producto(int codigo, String nombre, double precio, int inventario) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.inventario = inventario;
    }

    public Producto(String nombre, double precio, int inventario) {
        this.nombre = nombre;
        this.precio = precio;
        this.inventario = inventario;
    }
        
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getInventario() {
        return inventario;
    }

    public void setInventario(int inventario) {
        this.inventario = inventario;
    }
}
