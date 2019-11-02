package com.app.medical.Medicinas;

public class Medicina_model {
    private String nombre;
    private String dosis;
    private int imagen;

    public Medicina_model() {
    }

    public Medicina_model(String nombre, String dosis, int imagen) {
        this.nombre = nombre;
        this.dosis = dosis;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }
}
