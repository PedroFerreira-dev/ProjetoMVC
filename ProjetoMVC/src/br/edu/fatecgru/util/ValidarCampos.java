package br.edu.fatecgru.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

//Métodos de válidação dos campos em toda a tabela
public class ValidarCampos {
	
	public  boolean campoVazio(JTextField campo, String nomeCampo) {
        if (campo.getText().trim().isEmpty() || campo.getText().trim().equals("_")) {
            JOptionPane.showMessageDialog(null, "Preencha o campo: " + nomeCampo);
            campo.requestFocus();
            return false;
        }
        return true;
    }

    public  boolean campoVazio(JFormattedTextField campo, String nomeCampo) {
        if (campo.getText().trim().replace("_", "").isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preencha o campo: " + nomeCampo);
            campo.requestFocus();
            return false;
        }
        return true;
    }
    
    public  boolean celular(String celular) {
        if (!celular.matches("^\\(\\d{2}\\) \\d{5}-\\d{4}$")) {
            JOptionPane.showMessageDialog(null, "Celular inválido. Formato correto: (XX) XXXXX-XXXX");
            return false;
        }
        return true;
    }
    
    public static boolean cpf(String cpf) {
        if (!cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
            JOptionPane.showMessageDialog(null, "CPF inválido. Formato correto: XXX.XXX.XXX-XX");
            return false;
        }
        return true;
    }
    
    public  boolean data(String dataStr) {
    	
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            Date data = sdf.parse(dataStr);

            // não aceita datas futuras
            if (data.after(new Date())) {
                JOptionPane.showMessageDialog(null, "Data de nascimento futurista inválida.");
                return false;
            }

            // idade mínima 10 anos
            Calendar hoje = Calendar.getInstance();
            Calendar nascimento = Calendar.getInstance();
            nascimento.setTime(data);

            int idade = hoje.get(Calendar.YEAR) - nascimento.get(Calendar.YEAR);
            if (hoje.get(Calendar.DAY_OF_YEAR) < nascimento.get(Calendar.DAY_OF_YEAR)) {
                idade--;
            }

            if (idade < 10) {
                JOptionPane.showMessageDialog(null, "Idade mínima: 10 anos.");
                return false;
            }

            return true;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Data inválida. Use o formato DD/MM/AAAA.");
            return false;
        }
    }
    
    public  boolean cep(String cep) {
        if (!cep.matches("\\d{5}-\\d{3}")) {
            JOptionPane.showMessageDialog(null, "CEP inválido. Formato correto: XXXXX-XXX");
            return false;
        }
        return true;
    }
    
    public boolean nome(String nome) {
        // Regex aceita apenas letras (maiúsculas/minúsculas) e espaços
        if (!nome.matches("^[A-Za-zÀ-ÖØ-öø-ÿ ]+$")) {
            JOptionPane.showMessageDialog(null, "Nome inválido. O campo Nome não pode conter números ou caracteres especiais.");
            return false;
        }
        return true;
    }


}
