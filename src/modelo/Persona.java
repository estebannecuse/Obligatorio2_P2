package modelo;

import java.io.Serializable;

public abstract class Persona implements Serializable {
   protected  String nombre;
   protected  String ci;
   protected  int celular;

    public Persona(String nombre, String ci, int celular) {
        this.nombre = nombre;
        this.ci = ci;
        this.celular = celular;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public void setCelular(int celular) {
        this.celular = celular;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCi() {
        return ci;
    }

    public int getCelular() {
        return celular;
    }

    @Override
    public String toString() {
        return nombre + " (" + ci + ")";
    }

}
