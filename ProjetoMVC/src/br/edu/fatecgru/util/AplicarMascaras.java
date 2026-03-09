package br.edu.fatecgru.util;

import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.text.MaskFormatter;


//Classe de automação para aplicar as máscaras nos campos
public class AplicarMascaras {
	
	 public static void aplicar(JFormattedTextField campo, String tipo) {
	        String format = switch (tipo.toUpperCase()) {
	        case "CPF" -> "###.###.###-##";
            case "CELULAR" -> "(##) #####-####";
            case "CEP" -> "#####-###";
            case "DATA" -> "##/##/####";
            default -> null;
	        };
	        
	        if (format == null) {
	            JOptionPane.showMessageDialog(null, "Tipo de máscara inválido: " + tipo);
	            return;
	        }

	        try {
	            MaskFormatter mascara = new MaskFormatter(format);
	            mascara.setPlaceholderCharacter('_');
	            mascara.install(campo);
	        } catch (Exception e) {
	            JOptionPane.showMessageDialog(null, "Erro ao aplicar máscara: " + e.getMessage());
	        }
	 }
}
