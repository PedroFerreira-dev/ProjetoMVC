package br.edu.fatecgru.util;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

//Criação mais automatizada das tabelas de visualização presentes em todo o projeto
public class TabelaFactory {
	
	  public class TabelaComponent {
	        public JTable tabela;
	        public JScrollPane scrollPane;
	        public DefaultTableModel modelo;
	        public TableRowSorter<DefaultTableModel> sorter;
	    }

	  public TabelaComponent criarTabela(String[] colunas, int x, int y, int width, int height) {

	        TabelaComponent comp = new TabelaComponent();

	        comp.modelo = new DefaultTableModel(new Object[][]{}, colunas);

	        comp.tabela = new JTable(comp.modelo);
	        comp.tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        comp.tabela.setDefaultEditor(Object.class, null);
	        comp.tabela.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	        comp.tabela.getTableHeader().setReorderingAllowed(false);
	        comp.tabela.getTableHeader().setResizingAllowed(true);

	        comp.sorter = new TableRowSorter<>(comp.modelo);
	        comp.tabela.setRowSorter(comp.sorter);

	        comp.scrollPane = new JScrollPane(comp.tabela);
	        comp.scrollPane.setBounds(x, y, width, height);

	        return comp;
	    }

}