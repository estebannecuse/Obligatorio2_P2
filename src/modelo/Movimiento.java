package modelo;

import java.io.Serializable;

public class Movimiento implements Serializable {
   private int mes;
   private Area origen;
    private Area destino;
    private Empleado empleado;
    
    
    public Movimiento(int mes, Area origen, Area destino, Empleado empleado) {
        this.mes = mes;
        this.origen = origen;
        this.destino = destino;
        this.empleado = empleado;
    }
    

    public int getMes() { return mes; }
    public Area getOrigen() { return origen; }
    public Area getDestino() { return destino; }
    public Empleado getEmpleado() { return empleado; }

    @Override
    public String toString() {
        String nomEmp = (empleado != null) ? empleado.getNombre() : "-";
        String nomOri = (origen != null) ? origen.getNombre() : "-";
        String nomDes = (destino != null) ? destino.getNombre() : "-";
        return "Mes " + mes + " | " + nomEmp + " : " + nomOri + " -> " + nomDes;
    }
}
