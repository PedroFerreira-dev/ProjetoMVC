package br.edu.fatecgru.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import br.edu.fatecgru.dao.DisciplinaDAO;
import br.edu.fatecgru.model.Disciplina;
import br.edu.fatecgru.util.TabelaFactory;

public class TelaDisciplina extends JPanel {

	private static final long serialVersionUID = 1L;
	private DefaultTableModel modeloTabela;
	private JTable tableDisciplinas;
	private JTextField txtDisciplinasFiltrar;
	private TableRowSorter<DefaultTableModel> sorter;
	private JTextField txtNomeAdicionar;
	private JTextField txtIdAdicionar;
	
	//Variáveis de classes
	private Disciplina disciplina;
	private DisciplinaDAO dao;
	private JTextField txtIdDisciplinaAlterar;
	private JTextField txtNomeDisciplinaAlterar;

	/**
	 * Create the panel.
	 */
	public TelaDisciplina() {
		setLayout(null);
		
		//Variaveis utilitarias
		TabelaFactory criarTabela = new TabelaFactory();
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(44, 55, 739, 431);
		add(tabbedPane);
		
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Visualizar", null, panel, null);
		panel.setLayout(null);
	    
		//Criar tabela
	    String[] colunasDisciplina = { "ID", "Nome da Disciplina" };

	    TabelaFactory.TabelaComponent tabelaDisciplinaComp = criarTabela.criarTabela(colunasDisciplina, 20, 55, 690, 220);

	    panel.add(tabelaDisciplinaComp.scrollPane);

	    // Referências para usar no resto da classe
	    tableDisciplinas = tabelaDisciplinaComp.tabela;
	    modeloTabela = tabelaDisciplinaComp.modelo;
	    sorter = tabelaDisciplinaComp.sorter;

	    
		//Carrega sempre que abre a página
		carregarTabelaDisciplinas();
		
		
		JButton btnAdicionar = new JButton("Adicionar");
		btnAdicionar.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		btnAdicionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				tabbedPane.setEnabledAt(1, true);    // habilita a aba "Adicionar"
			    tabbedPane.setSelectedIndex(1);      // muda automaticamente para a aba "Adicionar"
			    
			    // Desabilita a aba "Visualizar", retornando apenas no botão
			    tabbedPane.setEnabledAt(0, false);
			    
			 // ✅ Limpar campos ao abrir aba
		        txtIdAdicionar.setText("");
		        txtNomeAdicionar.setText("");
			}
		});
		btnAdicionar.setBounds(90, 315, 130, 60);
		panel.add(btnAdicionar);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Adicionar", null, panel_1, null);
		panel_1.setLayout(null);
		
		txtNomeAdicionar = new JTextField();
		txtNomeAdicionar.setBounds(115, 100, 545, 26);
		panel_1.add(txtNomeAdicionar);
		txtNomeAdicionar.setColumns(10);
		
		txtIdAdicionar = new JTextField();
		txtIdAdicionar.setBounds(115, 200, 196, 26);
		panel_1.add(txtIdAdicionar);
		txtIdAdicionar.setColumns(10);
		
		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Salvar
			    disciplina = new Disciplina();
			    
			    //Valida se tem algum campo vazio
			    if (txtIdAdicionar.getText().isEmpty() || txtNomeAdicionar.getText().isEmpty()){

			    		//Avisa caso estiver algum vazio
			    	    JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
			    	    return;
			    }
							
				//Popular variavel curso
				disciplina.setId(Integer.parseInt(txtIdAdicionar.getText()));
				disciplina.setNome(txtNomeAdicionar.getText());
					
				//Chama o DAO para salvar 
				try {
					
					//Chamar CursoDAO --> Abre o BD
					dao = new DisciplinaDAO();
					
					dao.salvar(disciplina);
					
					JOptionPane.showMessageDialog(null, "Salvo com sucesso");
					
					//Atualiza a tabela de Disciplinas
					carregarTabelaDisciplinas();
					
					// Habilita a aba "Visualizar" volta para ela
					tabbedPane.setEnabledAt(0, true);
					tabbedPane.setSelectedIndex(0);	
					
					
				}
				catch (Exception erro) {
					
					JOptionPane.showMessageDialog(null,"Aconteceu erro: " + erro.getMessage());
				}	
			}
		});
		btnSalvar.setBounds(180, 300, 130, 60);
		panel_1.add(btnSalvar);
		
		JLabel lblNewLabel = new JLabel("ID:");
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		lblNewLabel.setBounds(50, 200, 44, 25);
		panel_1.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Nome:");
		lblNewLabel_1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(50, 100, 60, 25);
		panel_1.add(lblNewLabel_1);
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Habilita a aba "Visualizar" e volta para ela
				tabbedPane.setEnabledAt(0, true);
				tabbedPane.setSelectedIndex(0);
				
				// Desabilita a aba "Adicionar" novamente
				tabbedPane.setEnabledAt(1, false);
				
			}
		});
		btnVoltar.setBounds(410, 300, 130, 60);
		panel_1.add(btnVoltar);
		
		txtDisciplinasFiltrar = new JTextField();
		txtDisciplinasFiltrar.addKeyListener(new KeyAdapter() {
			
			//Sempre que uma tecla é solta
			@Override
			public void keyReleased(KeyEvent e) {
				
				  aplicarFiltros(); 
		    }
		});
		txtDisciplinasFiltrar.setBounds(196, 15, 515, 26);
		panel.add(txtDisciplinasFiltrar);
		txtDisciplinasFiltrar.setColumns(10);
		
		JButton btnAlterar = new JButton("Alterar");
		btnAlterar.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		btnAlterar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int linhaSelecionada = tableDisciplinas.getSelectedRow();
				
				//Verifica se alguma linha foi selecionada
				if (linhaSelecionada == -1)
				{
					JOptionPane.showMessageDialog(null, "Selecione uma disciplina para alterar.");
					return;
				}
				
				//Pega os dados da linha selecionada e preenche os campos na aba Alterar
			    String id = tableDisciplinas.getValueAt(linhaSelecionada, 0).toString();
			    String nome = tableDisciplinas.getValueAt(linhaSelecionada, 1).toString();
	            
	            // Habilita a aba "Alterar" e muda para ela
		        tabbedPane.setEnabledAt(2, true);
		        tabbedPane.setSelectedIndex(2);
		       
		        tabbedPane.setEnabledAt(0, false); // // Desabilita a aba "Visualizar", voltando só no botão
		        
		        //Preenche os campos na aba "Alterar"
		        txtIdDisciplinaAlterar.setText(id);
		        txtIdDisciplinaAlterar.setEditable(false); // ID não pode ser alterado
		        txtNomeDisciplinaAlterar.setText(nome);
			}
		});
		btnAlterar.setBounds(290, 315, 130, 60);
		panel.add(btnAlterar);
		
		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Excluir
				disciplina = new Disciplina();
				
				//Pegar o ID
				int linhaSelecionada = tableDisciplinas.getSelectedRow();
				
				//Verifica se alguma linha foi selecionada
				if (linhaSelecionada == -1)
				{
					JOptionPane.showMessageDialog(null, "Selecione uma disciplina para excluir.");
					return;
				}
				
				// Confirmação de exclusão
		        int confirmacao = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir esta disciplina?", "Confirmação", JOptionPane.YES_NO_OPTION);

		        if (confirmacao != JOptionPane.YES_OPTION) return;; // Sai se o usuário não confirmar
				
				//Pega o ID da linha selecionada
		        int id = Integer.parseInt(tableDisciplinas.getValueAt(linhaSelecionada, 0).toString());
				
				try {
					dao = new DisciplinaDAO();
					disciplina = new Disciplina();
					disciplina.setId(id); // Define o ID da disciplina a ser excluído
					dao.excluir(disciplina);
					JOptionPane.showMessageDialog(null,"Excluído com sucesso!");
					
					//Atualiza a tabela de Campus
					carregarTabelaDisciplinas();
					
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Erro ao excluir: " + e1.getMessage());
				}
				
			}
		});
		btnExcluir.setBounds(490, 315, 130, 60);
		panel.add(btnExcluir);
		
		JLabel lblFiltro = new JLabel("Selecione uma disciplina:");
		lblFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		lblFiltro.setBounds(21, 15, 181, 25);
		panel.add(lblFiltro);
		
		// ======== Eventos ========
				// Atualiza tabela sempre que a aba "Visualizar" for selecionada
				tabbedPane.addChangeListener(e -> {
							
				if (tabbedPane.getSelectedIndex() == 0)
				{ 
				
				// Índice 0 corresponde à aba "Visualizar"	
				carregarTabelaDisciplinas();
				
				 // Desabilita as abas "Adicionar" e "Alterar" ao voltar para a aba "Visualizar"
		        tabbedPane.setEnabledAt(1, false);
		        tabbedPane.setEnabledAt(2, false);
				
				}
				});
				
				//Desativa a aba de Adicionar no início
				tabbedPane.setEnabledAt(1, false);
				
				
				Panel panel_2 = new Panel();
				panel_2.setBackground(new Color(240, 240, 240));
				tabbedPane.addTab("Alterar", null, panel_2, null);
				panel_2.setLayout(null);
				
				//Desativa a aba de Alterar no início
				tabbedPane.setEnabledAt(2, false);
				
				JLabel lblNewLabel_2 = new JLabel("ID:");
				lblNewLabel_2.setFont(new Font("Segoe UI", Font.PLAIN, 20));
				lblNewLabel_2.setBounds(50, 200, 46, 25);
				panel_2.add(lblNewLabel_2);
				
				JLabel lblNewLabel_3 = new JLabel("Nome:");
				lblNewLabel_3.setFont(new Font("Segoe UI", Font.PLAIN, 16));
				lblNewLabel_3.setBounds(50, 100, 46, 25);
				panel_2.add(lblNewLabel_3);
				
				txtIdDisciplinaAlterar = new JTextField();
				txtIdDisciplinaAlterar.setBounds(115, 200, 196, 26);
				panel_2.add(txtIdDisciplinaAlterar);
				txtIdDisciplinaAlterar.setColumns(10);
				
				txtNomeDisciplinaAlterar = new JTextField();
				txtNomeDisciplinaAlterar.setBounds(115, 100, 545, 26);
				panel_2.add(txtNomeDisciplinaAlterar);
				txtNomeDisciplinaAlterar.setColumns(10);
				
				JButton btnSalvar_1 = new JButton("Salvar");
				btnSalvar_1.setFont(new Font("Segoe UI", Font.PLAIN, 18));
				btnSalvar_1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						// Alterar
						disciplina = new Disciplina();
						
						// Populando o objeto disciplina
						disciplina.setId(Integer.parseInt(txtIdDisciplinaAlterar.getText()));
						disciplina.setNome(txtNomeDisciplinaAlterar.getText());
						
						// Chama CursoDAO >> abre o banco de dados
						try {
							dao = new DisciplinaDAO();
							dao.alterar(disciplina);
							JOptionPane.showMessageDialog(null,"Alterado com sucesso!");
							
							//Atualiza a tabela de Campus
							carregarTabelaDisciplinas();
							
							// Habilita a aba "Visualizar" volta para ela
							tabbedPane.setEnabledAt(0, true);
							tabbedPane.setSelectedIndex(0);	
							
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null,"Aconteceu erro: " + e1.getMessage());
						}	
						
					}
				});
				btnSalvar_1.setBounds(180, 300, 130, 60);
				panel_2.add(btnSalvar_1);
				
				JButton btnVoltar_1 = new JButton("Voltar");
				btnVoltar_1.setFont(new Font("Segoe UI", Font.PLAIN, 18));
				btnVoltar_1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// Habilita a aba "Visualizar" e volta para ela
						tabbedPane.setEnabledAt(0, true);
						tabbedPane.setSelectedIndex(0);
						
						// Desabilita a aba "Alterar" novamente
						tabbedPane.setEnabledAt(2, false);
					}
				});
				btnVoltar_1.setBounds(410, 300, 130, 60);
				panel_2.add(btnVoltar_1);
				

	}
	
	//Método para unir filtros 
			private void aplicarFiltros() {
				
			    List<RowFilter<Object,Object>> filtros = new ArrayList<>();

			    // Filtro por nome do curso
			    String busca = txtDisciplinasFiltrar.getText().trim();
			    if (!busca.isEmpty()) {
			        filtros.add(RowFilter.regexFilter("(?i)" + busca, 1)); // coluna 1 = Nome da Disciplina
			    }

			    // Junta todos os filtros (AND)
			    if (filtros.isEmpty()) {
			        sorter.setRowFilter(null);
			    } else {
			        sorter.setRowFilter(RowFilter.andFilter(filtros));
			    }
			}
			
			//Método para carregar a tabela de Disciplinas
			private void carregarTabelaDisciplinas() {
				try {
					dao = new DisciplinaDAO();
					DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
					List<Disciplina> lista = dao.listarTodos();

					modeloTabela.setRowCount(0); // limpa a tabela

					for (Disciplina d : lista) {
						modeloTabela.addRow(new Object[] {
							d.getId(),
							d.getNome(),
						});
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Erro ao carregar tabela: " + e.getMessage());
				}
			}
}