/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package mainPackage;
import javax.swing.SwingUtilities;

import ventanas.VentanaInicial;
import ventanas.VentanaIntegrantes;
/**
 *
 * @author jacqu
 */
public class SegundoObligatorioProgramacion2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
         SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                VentanaInicial ventana = new VentanaInicial();
                ventana.setVisible(true);
            }
    });
    }
    
}
