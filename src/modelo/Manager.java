/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;

/**
 *
 * @author jacqu
 */
public class Manager extends Persona {
    private int antiguedad;
    private ArrayList<Empleado> empleadosACargo;
    


    public Manager(String nombre, String apellido, int ci, int antiguedad) {
        super(nombre, apellido, ci);
        this.antiguedad = antiguedad;
        this.empleadosACargo = new ArrayList<>();
    }

    public int getAntiguedad() { return antiguedad; }
    public ArrayList<Empleado> getEmpleadosACargo() { return empleadosACargo; }

    public void agregarEmpleado(Empleado e) {
        empleadosACargo.add(e);
    }
}
