package br.edu.fatecgru.util;

import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import br.edu.fatecgru.dao.AlunoDisciplinaDAO;
import br.edu.fatecgru.dao.CampusDAO;
import br.edu.fatecgru.dao.CursoDAO;
import br.edu.fatecgru.model.Campus;
import br.edu.fatecgru.model.Curso;
import br.edu.fatecgru.model.CursoDisciplina;


//Classe utilitária para carregar os comboBox de cada página 
public class CarregarComboBox {
	
	//Carregar as comboBox de campus
	public void carregarCampus(JComboBox combo, boolean incluirTodos) {
	        combo.removeAllItems();

	        if (incluirTodos) combo.addItem("Todos");
	        else combo.addItem("Selecione uma opção");

	        try {
	            CampusDAO campusDAO = new CampusDAO();
	            for (Campus c : campusDAO.listarTodos()) combo.addItem(c);
	        } catch (Exception ex) {
	            JOptionPane.showMessageDialog(null, "Erro ao carregar campus: " + ex.getMessage());
	        }

	        combo.setSelectedIndex(0);
	    }
	 
	
	//Carregar as comboBox de Cursos
	public void carregarCursos(JComboBox combo, int idCampus, boolean incluirTodos) {
	        combo.removeAllItems();

	        if (incluirTodos) combo.addItem("Todos");
	        else combo.addItem("Selecione uma opção");

	        try {
	            CursoDAO cursoDAO = new CursoDAO();
	            for (Curso c : cursoDAO.listarPorCampus(idCampus)) combo.addItem(c);
	        } catch (Exception ex) {
	            JOptionPane.showMessageDialog(null, "Erro ao carregar cursos: " + ex.getMessage());
	        }

	        combo.setSelectedIndex(0);
	    }
	
	public void carregarPeriodos(JComboBox combo, boolean incluirTodos) {
        try {
            combo.removeAllItems();

            if (incluirTodos) combo.addItem("Todos");
            else combo.addItem("Selecione uma opção");

            combo.addItem("Matutino");
            combo.addItem("Vespertino");
            combo.addItem("Noturno");

            combo.setSelectedIndex(0);
        } 
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar períodos: " + ex.getMessage());
        }
	}
	
	public void carregarDisciplinasPorAluno(JComboBox combo, String ra) {
	    try {
	    	combo.removeAllItems();
	    	
	    	// Adiciona primeira opção padrão
	        combo.addItem("Selecione uma disciplina");

	        AlunoDisciplinaDAO adDAO = new AlunoDisciplinaDAO();
	        List<CursoDisciplina> lista = adDAO.listarDisciplinasPorAluno(Integer.parseInt(ra));

	        for (CursoDisciplina cd : lista) {
	            combo.addItem(cd); // ✅ agora adiciona o objeto, não o nome
	        }
	        
	        combo.setSelectedIndex(0);
	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(null,
	            "Erro ao carregar disciplinas: " + e.getMessage());
	    }
	}

}