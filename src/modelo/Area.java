/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author jacqu
 */
public class Area implements Serializable {
   private String nombre;
   private String descripcion;
   private double presupuestoAnual;
   private ArrayList<Empleado> empleados;

    public Area(String nombre, String descripcion, double presupuestoAnual) {
         this.nombre = nombre;
         this.descripcion = descripcion;
         this.presupuestoAnual = presupuestoAnual;
         this.empleados = new ArrayList<>();
    }
    

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getPresupuestoAnual() { return presupuestoAnual; }
    public ArrayList<Empleado> getEmpleados() { return empleados; }

    public void agregarEmpleado(Empleado e) {
        if (e != null && !empleados.contains(e)) {
            empleados.add(e);
        }
    }

    public void removerEmpleado(Empleado e) {
        int i = 0;
        while (i < empleados.size()) {
            if (empleados.get(i) == e) {
                empleados.remove(i);
                break;
            }
            i++;
        }
    }

    public double salarioAnualAsignado() {
        double total = 0;
        int i = 0;
        while (i < empleados.size()) {
            total += empleados.get(i).getSalarioMensual() * 12.0;
            i++;
        }
        return total;
    }

    public double porcentajeAsignado() {
        if (presupuestoAnual <= 0) return 0;
        return (salarioAnualAsignado() * 100.0) / presupuestoAnual;
    }

    public double disponibleAnual() {
        double asignado = salarioAnualAsignado();
        double disp = presupuestoAnual - asignado;
        if (disp < 0) disp = 0;
        return disp;
    }

    public boolean tienePresupuestoPara(double montoAdicional) {
        return montoAdicional <= disponibleAnual();
    }

    @Override
    public String toString() {
        return nombre + " (Presupuesto: " + presupuestoAnual + " USD, Empleados: " + empleados.size() + ")";
    }


}
