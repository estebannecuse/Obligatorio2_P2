package logica;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import modelo.Area;
import modelo.Empleado;
import modelo.Manager;
import modelo.Movimiento;

public class Sistema implements Serializable {
    private ArrayList<Area> areas;
    private ArrayList<Empleado> empleados;
    private ArrayList<Movimiento> movimientos;
    private ArrayList<Manager> managers;

    public Sistema() {
        this.areas = new ArrayList<>();
        this.empleados = new ArrayList<>();
        this.movimientos = new ArrayList<>();
        this.managers = new ArrayList<>();
    }

    // logica de Areas

    public boolean altaArea(String nombre, String descripcion, double presupuesto) {
        if (nombre == null || nombre.isEmpty() || presupuesto <= 0)
            return false;
        if (buscarAreaPorNombre(nombre) != null)
            return false;

        Area nuevaArea = new Area(nombre.trim(), descripcion == null ? "" : descripcion.trim(), presupuesto);
        areas.add(nuevaArea);
        ordenarAreasPorNombre();
        return true;

    }

    public Area buscarAreaPorNombre(String nombre) {
        if (nombre == null)
            return null;
        int i = 0;
        while (i < areas.size()) {
            if (areas.get(i).getNombre().equalsIgnoreCase(nombre)) {
                return areas.get(i);
            }
            i++;
        }
        return null;
    }

    private void ordenarAreasPorNombre() {
        int i = 0;
        while (i < areas.size()) {
            int min = i;
            int j = i + 1;
            while (j < areas.size()) {
                String a = areas.get(j).getNombre();
                String b = areas.get(min).getNombre();
                if (a.compareToIgnoreCase(b) < 0) {
                    min = j;
                }
                j++;
            }
            if (min != i) {
                Area tmp = areas.get(i);
                areas.set(i, areas.get(min));
                areas.set(min, tmp);
            }
            i++;
        }
    }

    public ArrayList<Area> listarAreasOrdenadasPorNombre() {
        ArrayList<Area> copia = new ArrayList<Area>();
        int i = 0;
        while (i < areas.size()) {
            copia.add(areas.get(i));
            i++;
        }
        // selección simple por nombre asc
        int j = 0;
        while (j < copia.size()) {
            int min = j;
            int k = j + 1;
            while (k < copia.size()) {
                String a = copia.get(k).getNombre();
                String b = copia.get(min).getNombre();
                if (a.compareToIgnoreCase(b) < 0) {
                    min = k;
                }
                k++;
            }
            if (min != j) {
                Area tmp = copia.get(j);
                copia.set(j, copia.get(min));
                copia.set(min, tmp);
            }
            j++;
        }
        return copia;
    }

    public ArrayList<Area> listarAreasSinEmpleados() {
        ArrayList<Area> res = new ArrayList<Area>();
        int i = 0;
        while (i < areas.size()) {
            if (areas.get(i).getEmpleados().isEmpty()) {
                res.add(areas.get(i));
            }
            i++;
        }
        return res;
    }

    public boolean modificarDescripcionArea(String nombreArea, String nuevaDescripcion) {
        Area a = buscarAreaPorNombre(nombreArea);
        if (a == null)
            return false;
        a.setDescripcion(nuevaDescripcion == null ? "" : nuevaDescripcion.trim());
        return true;
    }

    public boolean bajaArea(String nombreArea) {
        int i = 0;
        while (i < areas.size()) {
            Area a = areas.get(i);
            if (a.getNombre().equalsIgnoreCase(nombreArea) && a.getEmpleados().isEmpty()) {
                areas.remove(i);
                return true;
            }
            i++;
        }
        return false;
    }

    // logica de managers

    public boolean altaManager(String nombre, String ci, int celular, int antiguedad) {
        if (nombre == null || nombre.trim().isEmpty())
            return false;
        if (ci == null || ci.trim().isEmpty())
            return false;
        if (antiguedad < 0)
            return false;
        if (ciExiste(ci.trim()))
            return false;

        Manager m = new Manager(nombre.trim(), ci.trim(), celular, antiguedad);
        managers.add(m);
        ordenarManagersPorAntiguedadDesc();
        return true;
    }

    private boolean ciExiste(String ci) {
        int i = 0;
        while (i < managers.size()) {
            if (managers.get(i).getCi().equalsIgnoreCase(ci)) {
                return true;
            }
            i++;
        }
        return false;
    }

    private Manager buscarManagerPorCI(String ci) {
        if (ci == null)
            return null;
        int i = 0;
        while (i < managers.size()) {
            if (managers.get(i).getCi().equalsIgnoreCase(ci)) {
                return managers.get(i);
            }
            i++;
        }
        return null;
    }

    public boolean modificarTelefonoManager(String ci, int nuevoCelular) {
        Manager m = buscarManagerPorCI(ci);
        if (m == null)
            return false;
        m.setCelular(nuevoCelular);
        return true;
    }

    public boolean bajaManager(String ci) {
        int i = 0;
        while (i < managers.size()) {
            Manager m = managers.get(i);
            if (m.getCi().equalsIgnoreCase(ci) && m.getEmpleadosACargo().isEmpty()) {
                managers.remove(i);
                return true;
            }
            i++;
        }
        return false;
    }

    public ArrayList<Manager> listarManagersPorAntiguedadDesc() {
        ArrayList<Manager> copia = new ArrayList<Manager>();
        int i = 0;
        while (i < managers.size()) {
            copia.add(managers.get(i));
            i++;
        }

        int j = 0;
        while (j < copia.size()) {
            int max = j;
            int k = j + 1;
            while (k < copia.size()) {
                if (copia.get(k).getAntiguedad() > copia.get(max).getAntiguedad()) {
                    max = k;
                }
                k++;
            }
            if (max != j) {
                Manager tmp = copia.get(j);
                copia.set(j, copia.get(max));
                copia.set(max, tmp);
            }
            j++;
        }
        return copia;
    }

    private void ordenarManagersPorAntiguedadDesc() {
        int i = 0;
        while (i < managers.size()) {
            int max = i;
            int j = i + 1;
            while (j < managers.size()) {
                if (managers.get(j).getAntiguedad() > managers.get(max).getAntiguedad()) {
                    max = j;
                }
                j++;
            }
            if (max != i) {
                Manager tmp = managers.get(i);
                managers.set(i, managers.get(max));
                managers.set(max, tmp);
            }
            i++;
        }
    }

    // logica empleados

    public boolean altaEmpleado(String nombre, String ci, int celular, String textoCV,
            double salarioMensual, Manager manager, Area area) {
        if (nombre == null || nombre.trim().isEmpty())
            return false;
        if (ci == null || ci.trim().isEmpty())
            return false;
        if (manager == null || area == null)
            return false;
        if (salarioMensual < 0)
            return false;
        if (ciExiste(ci.trim()))
            return false;

        double costoAnual = salarioMensual * 12.0;
        if (!area.tienePresupuestoPara(costoAnual)) {
            return false;
        }

        // crea carpeta cvs y archivo CVxxxxxxxx.txt
        String rutaCV = crearCV(ci.trim(), textoCV == null ? "" : textoCV);

        Empleado e = new Empleado(nombre.trim(), ci.trim(),
                celular == 0 ? 0 : celular,
                salarioMensual,
                rutaCV,
                manager,
                area);

        empleados.add(e);
        area.agregarEmpleado(e);
        manager.agregarEmpleado(e);

        ordenarEmpleadosPorSalarioAsc();
        return true;
    }

    public ArrayList<Empleado> listarEmpleadosPorSalarioAsc() {
        ArrayList<Empleado> copia = new ArrayList<Empleado>();
        int i = 0;
        while (i < empleados.size()) {
            copia.add(empleados.get(i));
            i++;
        }
        // selección por salario asc
        int j = 0;
        while (j < copia.size()) {
            int min = j;
            int k = j + 1;
            while (k < copia.size()) {
                if (copia.get(k).getSalarioMensual() < copia.get(min).getSalarioMensual()) {
                    min = k;
                }
                k++;
            }
            if (min != j) {
                Empleado tmp = copia.get(j);
                copia.set(j, copia.get(min));
                copia.set(min, tmp);
            }
            j++;
        }
        return copia;
    }

    private void ordenarEmpleadosPorSalarioAsc() {
        int i = 0;
        while (i < empleados.size()) {
            int min = i;
            int j = i + 1;
            while (j < empleados.size()) {
                if (empleados.get(j).getSalarioMensual() < empleados.get(min).getSalarioMensual()) {
                    min = j;
                }
                j++;
            }
            if (min != i) {
                Empleado tmp = empleados.get(i);
                empleados.set(i, empleados.get(min));
                empleados.set(min, tmp);
            }
            i++;
        }
    }

    // movimientos de empleados entre areas

    public boolean moverEmpleado(int mes, Empleado e, Area destino) {
        if (e == null || destino == null)
            return false;
        if (mes < 1 || mes > 12)
            return false;

        Area origen = e.getArea();
        if (origen == null)
            return false;
        if (origen == destino)
            return false;

        int mesesRestantes = 13 - mes;
        double montoNecesario = e.getSalarioMensual() * mesesRestantes;

        if (!destino.tienePresupuestoPara(montoNecesario)) {
            return false;
        }

        origen.removerEmpleado(e);

        destino.agregarEmpleado(e);

        e.setArea(destino);

        movimientos.add(new Movimiento(mes, origen, destino, e));

        return true;
    }

    public ArrayList<Movimiento> listarMovimientosPorMesAsc() {
        ArrayList<Movimiento> copia = new ArrayList<Movimiento>();
        int i = 0;
        while (i < movimientos.size()) {
            copia.add(movimientos.get(i));
            i++;
        }
        int j = 0;
        while (j < copia.size()) {
            int min = j;
            int k = j + 1;
            while (k < copia.size()) {
                if (copia.get(k).getMes() < copia.get(min).getMes()) {
                    min = k;
                }
                k++;
            }
            if (min != j) {
                Movimiento tmp = copia.get(j);
                copia.set(j, copia.get(min));
                copia.set(min, tmp);
            }
            j++;
        }
        return copia;
    }

    // precarga de datos

    public void cargarDatosPrueba() {

        altaArea("Personal", "Reclutamiento de personal, promociones, gestión de cargos", 100000.00);
        altaArea("RRHH", "Relacionamiento en la empresa, organigrama, gestión de equipos", 80000.00);
        altaArea("Seguridad",
                "Seguridad física, vigilancia, seguridad informática, protocolos y políticas de seguridad", 120000.00);
        altaArea("Comunicaciones",
                "Comunicaciones internas, reglas y protocolos, comunicaciones con proveedores y clientes", 20000.00);
        altaArea("Marketing",
                "Acciones planificadas, publicidad en medios masivos, publicidad en redes, gestión de redes", 95000.00);

        altaManager("Ana Martínez", "4.568.369-1", 99123456, 10);
        altaManager("Ricardo Morales", "3.214.589-3", 94121212, 4);
        altaManager("Laura Torales", "3.589.257-5", 99654321, 1);
        altaManager("Juan Pablo Zapata", "4.555.197-7", 99202020, 5);
    }

    // persistencia

    public static void guardar(Sistema s) {
        // TODO: implementar en persistencia.PersistenciaSistema
        // Por ahora dejamos la firma para no romper llamadas desde UI.
    }

    public static Sistema cargar() {
        // TODO: implementar en persistencia.PersistenciaSistema
        return null;
    }

    // chequeos

    private boolean ciExiste(int ci) {
        int i = 0;
        while (i < empleados.size()) {
            if (empleados.get(i).getCi().equals(ci))
                return true;
            i++;
        }
        int j = 0;
        while (j < managers.size()) {
            if (managers.get(j).getCi().equals(ci))
                return true;
            j++;
        }
        return false;
    }

    private String crearCV(String ci, String texto) {
        String folder = "cvs";
        File dir = new File(folder);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String nombreArchivo = "CV" + ci.replace(" ", "").replace("-", "") + ".txt";
        String ruta = folder + File.separator + nombreArchivo;

        FileWriter fw = null;
        try {
            fw = new FileWriter(ruta);
            fw.write(texto);
            fw.flush();
        } catch (IOException ex) {
            // En caso de error, igual devolvemos la ruta para mostrarla; podés manejar un
            // mensaje en UI.
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                }
            }
        }
        return ruta;
    }

    // getters para las ventanas, para cuando necesitemos llamar a los metodos

    public ArrayList<Area> getAreas() {
        return areas;
    }

    public ArrayList<Manager> getManagers() {
        return managers;
    }

    public ArrayList<Empleado> getEmpleados() {
        return empleados;
    }

    public ArrayList<Movimiento> getMovimientos() {
        return movimientos;
    }

}
