package ventanas.reportes;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

import logica.Sistema;
import modelo.Area;
import modelo.Empleado;
import modelo.Manager;

public class VentanaReporteEstadoAreas extends JDialog {

    private Sistema sistema;
    private JTable tablaAreas;
    private DefaultTableModel modeloAreas;
    private JPanel pnlEmpleados;   // grilla de "botones" de empleados
    private JLabel lblDetalle;     // detalle del empleado al hacer click

    // Cache: lista de áreas mostradas en el orden actual
    private ArrayList<Area> cacheAreas;

    public VentanaReporteEstadoAreas(Frame owner, Sistema sistema) {
        super(owner, "Reporte de Estado de Áreas", true);
        this.sistema = sistema;
        this.cacheAreas = new ArrayList<Area>();

        initUI();
        cargarAreasOrdenadasPorPorcentajeDesc();
        setSize(820, 520);
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        setLayout(new BorderLayout(8, 8));

        // ----- Tabla de Áreas -----
        String[] columnas = { "Área", "% Asignado", "Presupuesto (USD)", "Asignado (USD)", "Empleados" };
        modeloAreas = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) return Double.class;
                if (columnIndex == 2 || columnIndex == 3) return Double.class;
                if (columnIndex == 4) return Integer.class;
                return String.class;
            }
        };
        tablaAreas = new JTable(modeloAreas);
        tablaAreas.setRowHeight(24);

        // Renderer para colorear filas según % asignado
        tablaAreas.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                double pct = 0;
                Object obj = table.getModel().getValueAt(row, 1);
                if (obj instanceof Double) {
                    pct = (Double) obj;
                }

                
                Color bg;
                if (pct > 90.0) {
                    bg = new Color(255, 150, 150); // rojo claro
                } else if (pct >= 70.0) {
                    bg = new Color(255, 245, 150); // amarillo claro
                } else {
                    bg = new Color(230, 230, 230); // gris claro
                }

                if (isSelected) {
                    // Mantener selección visible
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                } else {
                    c.setBackground(bg);
                    c.setForeground(Color.BLACK);
                }

                // Alinear numéricos
                if (column == 1 || column == 2 || column == 3 || column == 4) {
                    setHorizontalAlignment(RIGHT);
                } else {
                    setHorizontalAlignment(LEFT);
                }

                return c;
            }
        });

        // Listener de selección de área
        tablaAreas.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaAreas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tablaAreas.getSelectedRow();
                if (fila >= 0 && fila < cacheAreas.size()) {
                    Area a = cacheAreas.get(fila);
                    poblarGrillaEmpleados(a);
                }
            }
        });

        add(new JScrollPane(tablaAreas), BorderLayout.CENTER);

        // ----- Panel inferior: Empleados (grilla + detalle) -----
        JPanel panelInferior = new JPanel(new BorderLayout(6, 6));

        pnlEmpleados = new JPanel();                // lo configuramos dinámicamente
        pnlEmpleados.setBorder(BorderFactory.createTitledBorder("Empleados del área (click para detalle)"));
        panelInferior.add(new JScrollPane(pnlEmpleados), BorderLayout.CENTER);

        lblDetalle = new JLabel("Detalle: -");
        lblDetalle.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        panelInferior.add(lblDetalle, BorderLayout.SOUTH);

        add(panelInferior, BorderLayout.SOUTH);
    }

    private void cargarAreasOrdenadasPorPorcentajeDesc() {
        // Limpiar tabla y cache
        while (modeloAreas.getRowCount() > 0) {
            modeloAreas.removeRow(0);
        }
        cacheAreas.clear();

        // Traigo copia ordenada por nombre y calculo %; luego ordeno por % desc (selección)
        ArrayList<Area> todas = sistema.listarAreasOrdenadasPorNombre();

        // Ordenar por porcentaje desc (selección simple)
        ArrayList<Area> copia = new ArrayList<Area>();
        int i = 0;
        while (i < todas.size()) {
            copia.add(todas.get(i));
            i++;
        }

        int j = 0;
        while (j < copia.size()) {
            int max = j;
            int k = j + 1;
            while (k < copia.size()) {
                double pctK = porcentaje(copia.get(k));
                double pctM = porcentaje(copia.get(max));
                if (pctK > pctM) {
                    max = k;
                }
                k++;
            }
            if (max != j) {
                Area tmp = copia.get(j);
                copia.set(j, copia.get(max));
                copia.set(max, tmp);
            }
            j++;
        }

        // Cargar modelo y cache
        int t = 0;
        while (t < copia.size()) {
            Area a = copia.get(t);
            double pct = porcentaje(a);
            double presupuesto = a.getPresupuestoAnual();
            double asignado = a.salarioAnualAsignado();
            int cant = a.getEmpleados().size();

            Object[] fila = {
                a.getNombre(),
                redondear2(pct),
                redondear2(presupuesto),
                redondear2(asignado),
                cant
            };
            modeloAreas.addRow(fila);
            cacheAreas.add(a);
            t++;
        }

        // Seleccionar primera por defecto si existe
        if (!cacheAreas.isEmpty()) {
            tablaAreas.setRowSelectionInterval(0, 0);
            poblarGrillaEmpleados(cacheAreas.get(0));
        } else {
            pnlEmpleados.removeAll();
            pnlEmpleados.revalidate();
            pnlEmpleados.repaint();
            lblDetalle.setText("Detalle: -");
        }
    }

    private double porcentaje(Area a) {
        // Reutilizamos el cálculo del Área si lo tenés; acá replicamos por claridad
        if (a.getPresupuestoAnual() <= 0) return 0;
        double pct = (a.salarioAnualAsignado() * 100.0) / a.getPresupuestoAnual();
        return pct;
    }

    private double redondear2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    private void poblarGrillaEmpleados(Area area) {
        // Ordenar empleados por nombre (asc) con selección simple
        ArrayList<Empleado> emps = new ArrayList<Empleado>();
        int i = 0;
        while (i < area.getEmpleados().size()) {
            emps.add(area.getEmpleados().get(i));
            i++;
        }
        int j = 0;
        while (j < emps.size()) {
            int min = j;
            int k = j + 1;
            while (k < emps.size()) {
                String a = emps.get(k).getNombre();
                String b = emps.get(min).getNombre();
                if (a.compareToIgnoreCase(b) < 0) {
                    min = k;
                }
                k++;
            }
            if (min != j) {
                Empleado tmp = emps.get(j);
                emps.set(j, emps.get(min));
                emps.set(min, tmp);
            }
            j++;
        }

        // Escala negro→azul según salario relativo
        double maxSal = 0;
        int m = 0;
        while (m < emps.size()) {
            if (emps.get(m).getSalarioMensual() > maxSal) {
                maxSal = emps.get(m).getSalarioMensual();
            }
            m++;
        }
        if (maxSal <= 0) maxSal = 1.0; // evitar /0

        // Armar grilla de botones (2 o 3 columnas según cantidad)
        int columnas = (emps.size() > 6) ? 3 : 2;
        pnlEmpleados.removeAll();
        pnlEmpleados.setLayout(new GridLayout(0, columnas, 6, 6));

        int e = 0;
        while (e < emps.size()) {
            Empleado emp = emps.get(e);
            double ratio = emp.getSalarioMensual() / maxSal; // 0..1
            int blue = (int) (ratio * 255);
            if (blue < 0) blue = 0;
            if (blue > 255) blue = 255;

            JButton btn = new JButton(emp.getNombre() + " - $" + redondear2(emp.getSalarioMensual()));
            btn.setBackground(new Color(0, 0, blue));
            // Color de texto: blanco si fondo oscuro
            btn.setForeground(blue > 128 ? Color.WHITE : Color.BLACK);
            btn.setFocusPainted(false);

            // Al click: mostrar detalle completo del empleado
            btn.addActionListener(ev -> {
                Manager mng = emp.getManager();
                String nomMng = (mng == null) ? "-" : (mng.getNombre() + " (" + mng.getCi() + ")");
                String ruta = (emp.getRutaCV() == null || emp.getRutaCV().isEmpty()) ? "-" : emp.getRutaCV();

                String detalle =
                        "Nombre: " + safe(emp.getNombre()) + "   |   CI: " + safe(emp.getCi()) + "\n" +
                        "Celular: " + emp.getCelular() + "   |   Salario: $" + redondear2(emp.getSalarioMensual()) + "\n" +
                        "Área: " + safe(area.getNombre()) + "   |   Manager: " + nomMng + "\n" +
                        "CV: " + ruta;

                lblDetalle.setText("Detalle: " + emp.getNombre() + " (" + emp.getCi() + ")");
                JOptionPane.showMessageDialog(
                        VentanaReporteEstadoAreas.this,
                        detalle,
                        "Empleado",
                        JOptionPane.INFORMATION_MESSAGE
                );
            });

            pnlEmpleados.add(btn);
            e++;
        }

        pnlEmpleados.revalidate();
        pnlEmpleados.repaint();
    }

    private String safe(String s) {
        return (s == null) ? "-" : s;
    }
}
