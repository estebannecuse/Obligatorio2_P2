/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author jacqu
 */
public class Empleado extends Persona {
    
    private double salarioMensual;
    private String rutaCV;
    private Manager manager;
    private Area area;


    public Empleado(String nombre, String apellido, int ci, double salarioMensaual, String rutaCV, Manager manager, Area area) {
        super(nombre, apellido, ci);
        this.salarioMensual = salarioMensaual;
        this.rutaCV = (rutaCV == null) ? "" : rutaCV;
        this.manager = manager;
        this.area = area;
    }

    public double getSalarioMensual() { return salarioMensual; }
    public Manager getManager() { return manager; }
    public Area getArea() { return area; }
    public void setArea(Area area) { this.area = area; }

    public String getRutaCV() { return rutaCV; }
    public void setRutaCV(String rutaCV) { this.rutaCV = (rutaCV == null) ? "" : rutaCV; }
}
