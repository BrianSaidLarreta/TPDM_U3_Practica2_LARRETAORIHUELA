package com.example.tpdm_u3_practica2_larretaorihuela;

public class Productos {

    String codigo, nombre, descripcion;

    public Productos(){}

    public Productos(String c, String n, String d){
        codigo = c;
        nombre = n;
        descripcion = d;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
