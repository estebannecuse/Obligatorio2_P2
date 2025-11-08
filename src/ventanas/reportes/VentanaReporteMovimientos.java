package ventanas.reportes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

import logica.Sistema;
import modelo.Movimiento;
import modelo.Area;
import modelo.Empleado;


public class VentanaReporteMovimientos extends JDialog {

    private Sistema sistema;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public VentanaReporteMovimientos(Frame owner, Sistema sistema) {
        super(owner, "Reporte de Movimientos", true);
        this.sistema = sistema;

        initUI();
        cargarDatos();
        setSize(600, 380);
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        setLayout(new BorderLayout());

        
        String[] columnas = { "Mes", "Área Origen", "Área Destino", "Empleado" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
          
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modeloTabla);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

      
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        pnlBotones.add(btnCerrar);
        add(pnlBotones, BorderLayout.SOUTH);
    }

    public void cargarDatos() {
     
        while (modeloTabla.getRowCount() > 0) {
            modeloTabla.removeRow(0);
        }

     
        ArrayList<Movimiento> lista = sistema.listarMovimientosPorMesAsc();
        int i = 0;
        while (i < lista.size()) {
            Movimiento mov = lista.get(i);
            int mes = mov.getMes();

            Area ori = mov.getOrigen();
            Area des = mov.getDestino();
            Empleado emp = mov.getEmpleado();

            String nomOri = (ori == null) ? "-" : ori.getNombre();
            String nomDes = (des == null) ? "-" : des.getNombre();
            String nomEmp = (emp == null) ? "-" : (emp.getNombre() + " (" + emp.getCi() + ")");

            Object[] fila = { mes, nomOri, nomDes, nomEmp };
            modeloTabla.addRow(fila);

            i++;
        }
    }
}
