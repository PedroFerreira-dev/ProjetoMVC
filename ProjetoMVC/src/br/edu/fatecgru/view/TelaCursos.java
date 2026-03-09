package br.edu.fatecgru.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import br.edu.fatecgru.dao.CampusDAO;
import br.edu.fatecgru.dao.CursoDAO;
import br.edu.fatecgru.dao.CursoDisciplinaDAO;
import br.edu.fatecgru.dao.DisciplinaDAO;
import br.edu.fatecgru.model.Campus;
import br.edu.fatecgru.model.Curso;
import br.edu.fatecgru.model.CursoDisciplina;
import br.edu.fatecgru.model.Disciplina;
import br.edu.fatecgru.util.CarregarComboBox;
import br.edu.fatecgru.util.TabelaFactory;

public class TelaCursos extends JPanel {

	private static final long serialVersionUID = 1L;
	private DefaultTableModel modeloTabela;
	private DefaultTableModel modeloTabelaGrade;
	private DefaultTableModel modeloTabelaAdicionarGrade;	
	private JTable tableCursos;
	private JTable tableDisciplinas;
	private JTable tableDisciplinasGrade;
	private JTable tableAdicionarDisciplinas;
	private JTextField txtNomeCursos;
	private JTextField txtIdCursos;
	private JTextField txtNomeCursoAlterar;
	private JTextField txtIdCursoAlterar;
	private JComboBox cbCampusAlterar;
	private JComboBox cbCampus;
	private JComboBox cbPeriodo;
	private JSpinner spDuracao;
	private JComboBox cbPeriodoAlterar;
	private JComboBox cbSelecionarSemestreAdicionar;
	private JSpinner spDuracaoAlterar;
	private JComboBox<Campus> cbCampusFiltrar;
	private JComboBox<String> cbPeriodoFiltrar;
	private JTextField txtCursoFiltrar;      // campo de busca por nome
	private TableRowSorter<DefaultTableModel> sorter;
	private TableRowSorter<DefaultTableModel> sorterGrade;
	
	
	//Variaveis de classe --> Não é recomendado cria-las aqui 
	private CursoDAO dao;
	private Curso curso;
	private DisciplinaDAO disciplinaDAO;
	private Disciplina disciplina;
	private JTextField txtCursoSelecionadoGrade;
	private JTextField txtNomeDoCursoGrade;
	private JTabbedPane tabbedPane;
	
	private int cursoSelecionadoId;
	
	/**
	 * Create the panel.
	 */
	public TelaCursos() {
		setLayout(null);
		
		//Classes utilitárias
		CarregarComboBox carregarCB = new CarregarComboBox();
		TabelaFactory criarTabela = new TabelaFactory();
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(44, 55, 739, 431);
		add(tabbedPane);
		
		
		
		String[] colunasCursos = { "ID", "Campus", "Nome do Curso", "Período", "Duração" };

		TabelaFactory.TabelaComponent tabelaCursosComp = criarTabela.criarTabela(colunasCursos, 20, 70, 690, 220);

		// Pegar referências
		tableCursos = tabelaCursosComp.tabela;
		modeloTabela = tabelaCursosComp.modelo;
		sorter = tabelaCursosComp.sorter;
		
		//Adiciona o sorter para permitir a filtragem /Coloquei aqui pois será usado no cbCampusFiltrar
		sorter = new TableRowSorter<>(modeloTabela);
		tableCursos.setRowSorter(sorter);
		
		//Adicionar a opção "Todos" no combobox de filtro (Como um campus, pois a lista é de campus)
		Campus todos = new Campus();
		todos.setId(0); // ID 0 para representar "Todos"
		todos.setNome("Todos");
		
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Visualizar", null, panel, null);
		panel.setLayout(null);
		
				// Adicionar o scrollPane no painel Visualizar
				panel.add(tabelaCursosComp.scrollPane);
				
				
				//Botão Adicionar
				JButton btnAdicionarCurso = new JButton("Adicionar");
				btnAdicionarCurso.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						tabbedPane.setEnabledAt(1, true);    // habilita a aba "Adicionar"
					    tabbedPane.setSelectedIndex(1);  
					    
					    tabbedPane.setEnabledAt(0, false); // Desabilita a aba "Visualizar", voltando apenas no botão
					    
					    // ✅ Limpar os campos ao entrar na aba Adicionar
				        txtIdCursos.setText("");
				        txtNomeCursos.setText("");
				        cbCampus.setSelectedIndex(-1);
				        cbPeriodo.setSelectedIndex(0);
				        spDuracao.setValue(1);// muda automaticamente para a aba "Adicionar"
					}
				});
				btnAdicionarCurso.setBounds(136, 312, 107, 32);
				panel.add(btnAdicionarCurso);
				
				//Botão para ir para a aba Alterar
				JButton btnAlterarCurso = new JButton("Alterar");
				btnAlterarCurso.addActionListener(new ActionListener() {
					
					//Evento do botão Alterar
					public void actionPerformed(ActionEvent e) {
						
						int linhaSelecionada = tableCursos.getSelectedRow();
						
						//Verifica se alguma linha foi selecionada
						if (linhaSelecionada == -1)
						{
							JOptionPane.showMessageDialog(null, "Selecione um curso para alterar.");
							return;
						}
						
						//Pega os dados da linha selecionada e preenche os campos na aba Alterar
					    String id = tableCursos.getValueAt(linhaSelecionada, 0).toString();
					    String campus = tableCursos.getValueAt(linhaSelecionada, 1).toString();
					    String nome = tableCursos.getValueAt(linhaSelecionada, 2).toString();
	            String periodo = tableCursos.getValueAt(linhaSelecionada, 3).toString();
	            String duracao = tableCursos.getValueAt(linhaSelecionada, 4).toString();
	            
	            //Carrega os campus no combobox
	            carregarCB.carregarCampus(cbCampusAlterar, false);
	            
	            // Habilita a aba "Alterar" e muda para ela
				        tabbedPane.setEnabledAt(2, true);
				        tabbedPane.setSelectedIndex(2);
				        
				        tabbedPane.setEnabledAt(0, false); // Desabilita a aba "Visualizar", retornando apenas no botão
				        
				        //Preenche os campos na aba "Alterar"
				        txtIdCursoAlterar.setText(id);
				        txtIdCursoAlterar.setEditable(false); // ID não pode ser alterado
				        txtNomeCursoAlterar.setText(nome);
				        
				        //Popular o combobox de campus
				        
				        // Selecionar campus na combo pelo ID
				        for (int i = 0; i < cbCampusAlterar.getItemCount(); i++) {
				            String nomeCampus = cbCampusAlterar.getItemAt(i).toString();
				            if (nomeCampus.equals(campus)) {
				                cbCampusAlterar.setSelectedIndex(i);
				                break;
				            }
				        }
				        
				        //Popular o combobox de período
				        
				        cbPeriodoAlterar.setSelectedItem(periodo);
				        
				        spDuracaoAlterar.setValue(Integer.parseInt(duracao));
					}
				});
				btnAlterarCurso.setBounds(253, 312, 107, 32);
				panel.add(btnAlterarCurso);
				
				JButton btnExcluirCurso = new JButton("Excluir");
				btnExcluirCurso.addActionListener(new ActionListener() {
					
					//Evento do botão Excluir
					public void actionPerformed(ActionEvent e) {
						
						// Excluir
						curso = new Curso();
						
						//Pegar o ID
						int linhaSelecionada = tableCursos.getSelectedRow();
						
						//Verifica se alguma linha foi selecionada
						if (linhaSelecionada == -1)
						{
							JOptionPane.showMessageDialog(null, "Selecione um curso para excluir.");
							return;
						}
						
						// Confirmação de exclusão
				        int confirmacao = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir este curso?", "Confirmação", JOptionPane.YES_NO_OPTION);

				        if (confirmacao != JOptionPane.YES_OPTION) return;; // Sai se o usuário não confirmar
						
						//Pega o ID da linha selecionada
				        int id = Integer.parseInt(tableCursos.getValueAt(linhaSelecionada, 0).toString());
						
						try {
							dao = new CursoDAO();
							curso = new Curso();
							curso.setId_curso(id); // Define o ID do campus a ser excluído
							dao.excluir(curso);
							JOptionPane.showMessageDialog(null,"Excluído com sucesso!");
							
							//Atualiza a tabela de Campus
							carregarTabelaCursos();
							
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null,"Erro ao excluir: " + e1.getMessage());
						}
					}
				});
				btnExcluirCurso.setBounds(370, 312, 103, 32);
				panel.add(btnExcluirCurso);
				
				cbCampusFiltrar = new JComboBox();
				cbCampusFiltrar.addItem(todos);
				
				
				//Carregar os campus no combobox /Está aqui da criação da variavel e antes do listener
				carregarCB.carregarCampus(cbCampusFiltrar, true);
				
		//Deixar como Todos ao iniciar
		cbCampusFiltrar.setSelectedIndex(0);
		
		cbCampusFiltrar.addItemListener(new ItemListener() {
			
			//Ao mudar o item selecionado
			public void itemStateChanged(ItemEvent e) {
				
				// Só executa quando o item for SELECIONADO (não quando deselecionar)
		        if (e.getStateChange() == ItemEvent.SELECTED) {
		        	
		        	//Método que realiza a validação com os outros filtros 
		        	  aplicarFiltros(); 	        
		      }		
			}
		});
		cbCampusFiltrar.setBounds(71, 35, 172, 20);
		panel.add(cbCampusFiltrar);
		
		
		
		
		
		JLabel lblNewLabel = new JLabel("Campus:");
		lblNewLabel.setBounds(20, 39, 44, 12);
		panel.add(lblNewLabel);
		
		//Combobox de filtrar por período
		cbPeriodoFiltrar = new JComboBox();
		carregarCB.carregarPeriodos(cbPeriodoFiltrar, true);
		
		//Deixa como todos ao iniciar
		cbPeriodoFiltrar.setSelectedIndex(0);
		
				cbPeriodoFiltrar.addItemListener(new ItemListener() {
					
					//Evento ao trocar de item
					public void itemStateChanged(ItemEvent e) {
						
						// Só executa quando o item for SELECIONADO (não quando deselecionar)
				        if (e.getStateChange() == ItemEvent.SELECTED) {
				        	
				        	  aplicarFiltros(); 
				        	
				        }
				        
						
					}
				});
				
				cbPeriodoFiltrar.setBounds(323, 35, 101, 20);
				panel.add(cbPeriodoFiltrar);
				
				JLabel lblNewLabel_1 = new JLabel("Período:");
				lblNewLabel_1.setBounds(282, 39, 44, 12);
				panel.add(lblNewLabel_1);
				
				txtCursoFiltrar = new JTextField();
				txtCursoFiltrar.addKeyListener(new KeyAdapter() {
					
					//Sempre que uma tecla é solta
					@Override
					public void keyReleased(KeyEvent e) {
						
						  aplicarFiltros(); 
				    }
				});
				txtCursoFiltrar.setBounds(566, 36, 103, 20);
				panel.add(txtCursoFiltrar);
				txtCursoFiltrar.setColumns(10);
				
				JButton btnLimparFiltros = new JButton("Limpar Filtros");
				btnLimparFiltros.addActionListener(new ActionListener() {
					
					//Ao apertar o botão
					public void actionPerformed(ActionEvent e) {
						 // ✅ Reseta o combo do campus
				        cbCampusFiltrar.setSelectedIndex(0); // "Todos"

				        // ✅ Reseta o combo do período
				        cbPeriodoFiltrar.setSelectedIndex(0); // "Todos"

				        // ✅ Limpa a busca por nome
				        txtCursoFiltrar.setText("");

				        // ✅ Reaplica os filtros (vai mostrar tudo)
				        aplicarFiltros();
					}
				});
				btnLimparFiltros.setBounds(483, 312, 103, 32);
				panel.add(btnLimparFiltros);
				
				JLabel lblNewLabel_2 = new JLabel("Pesquisar Curso:");
				lblNewLabel_2.setBounds(483, 35, 87, 20);
				panel.add(lblNewLabel_2);
				
				//Botão que leva para o gerenciar grade
				JButton btnGerenciarGrade = new JButton("Gerenciar Grade");
				btnGerenciarGrade.addActionListener(new ActionListener() {
					//Quando apertado
					public void actionPerformed(ActionEvent e) {
						
						int linha = tableCursos.getSelectedRow();

				        if (linha == -1) {
				            JOptionPane.showMessageDialog(null, "Selecione um curso primeiro.");
				            return;
				        }

				        // Pegar nome do curso da tabela
				        String nomeCurso = tableCursos.getValueAt(linha, 2).toString();
				        
				        int idCurso = Integer.parseInt(tableCursos.getValueAt(linha, 0).toString());
				        cursoSelecionadoId = idCurso;

				        // Preencher campo na aba de Grade
				        txtCursoSelecionadoGrade.setText(nomeCurso);
				        txtCursoSelecionadoGrade.setEditable(false); // impede edição
				        txtCursoSelecionadoGrade.setFocusable(false);
				        
				        carregarDisciplinasDoCurso(idCurso);
				        
				        //Habilita a aba Gerenciar
				        tabbedPane.setEnabledAt(3, true);
				        // Ir para a aba "Gerenciar Grade"
				        tabbedPane.setSelectedIndex(3);
				        
				        tabbedPane.setEnabledAt(0, false); // Desabilita a aba "Visualizar", voltando só no botão
				    }
				});
				btnGerenciarGrade.setBounds(297, 360, 137, 32);
				panel.add(btnGerenciarGrade);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Adicionar", null, panel_1, null);
		panel_1.setLayout(null);
		
		txtNomeCursos = new JTextField();
		txtNomeCursos.setColumns(10);
		txtNomeCursos.setBounds(189, 119, 127, 20);
		panel_1.add(txtNomeCursos);
		
		JLabel lblNomeCursos = new JLabel("Nome:");
		lblNomeCursos.setBounds(133, 123, 44, 12);
		panel_1.add(lblNomeCursos);
		
		txtIdCursos = new JTextField();
		txtIdCursos.setColumns(10);
		txtIdCursos.setBounds(425, 75, 143, 15);
		panel_1.add(txtIdCursos);
		
		JLabel lblIDCursos = new JLabel("ID:");
		lblIDCursos.setBounds(393, 77, 28, 13);
		panel_1.add(lblIDCursos);
		
		cbCampus = new JComboBox();
		cbCampus.setBounds(189, 75, 127, 20);
		panel_1.add(cbCampus);
		
		//Carregar os campus no combobox
		carregarCB.carregarCampus(cbCampus, false);
		
		JLabel lblCampus = new JLabel("Campus:");
		lblCampus.setBounds(133, 77, 44, 12);
		panel_1.add(lblCampus);
		
		cbPeriodo = new JComboBox();
		carregarCB.carregarPeriodos(cbPeriodo, false);
		cbPeriodo.setBounds(425, 119, 143, 20);
		panel_1.add(cbPeriodo);
		
		JLabel lblPeriodo = new JLabel("Periodo:");
		lblPeriodo.setBounds(381, 123, 44, 12);
		panel_1.add(lblPeriodo);
		
		JLabel lblDuracao = new JLabel("Duração em Semestres:");
		lblDuracao.setBounds(133, 162, 120, 28);
		panel_1.add(lblDuracao);
		
		spDuracao = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1)); // Valor inicial 0, mínimo 0, máximo 20, passo 1
		((JSpinner.DefaultEditor) spDuracao.getEditor()).getTextField().setEditable(false); // Impede edição direta do campo de texto
		((JSpinner.DefaultEditor) spDuracao.getEditor()).getTextField().setFocusable(false); // Impede foco no campo de texto
		spDuracao.setBounds(251, 162, 65, 28);
		panel_1.add(spDuracao);
		
		//Botão Salvar
		JButton btnSalvarCurso = new JButton("Salvar");
		btnSalvarCurso.addActionListener(new ActionListener() {
			
			//Evento do botão salvar
			public void actionPerformed(ActionEvent e) {
				
				//Salvar
			    curso = new Curso();
			    
			    //Valida se tem algum campo vazio
			    if (cbCampus.getSelectedIndex() == -1 ||
			    	    txtIdCursos.getText().isEmpty() ||
			    	    txtNomeCursos.getText().isEmpty() ||
			    	    cbPeriodo.getSelectedIndex() == 0 ||
			    	    spDuracao.getValue().equals(0)) {

			    		//Avisa caso estiver algum vazio
			    	    JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
			    	    return;
			    	}
							
				//Popular variavel curso
				curso.setId_curso(Integer.parseInt(txtIdCursos.getText()));
				
				//Pegar o ID do campus selecionado
				Campus campusSelecionado = (Campus) cbCampus.getSelectedItem();
				curso.setId_campus(campusSelecionado.getId());
				curso.setDuracao_semestre((int) spDuracao.getValue());
				curso.setNome_curso(txtNomeCursos.getText());
				curso.setPeriodo(cbPeriodo.getSelectedItem().toString());
					
				//Chama o DAO para salvar 
				try {
					
					//Chamar CursoDAO --> Abre o BD
					dao = new CursoDAO();
					
					dao.salvar(curso);
					
					JOptionPane.showMessageDialog(null, "Salvo com sucesso");
					
					//Atualiza a tabela de Cursos
					carregarTabelaCursos();
					
					// Habilita e volta para a aba "Visualizar"
					tabbedPane.setEnabledAt(0, true);	
					tabbedPane.setSelectedIndex(0);	
					
					
				}
				catch (Exception erro) {
					
					JOptionPane.showMessageDialog(null,"Aconteceu erro: " + erro.getMessage());
				}	
			}
		});
		btnSalvarCurso.setBounds(171, 277, 109, 28);
		panel_1.add(btnSalvarCurso);
		
		//Botão Limpar
		JButton btnLimparCurso = new JButton("Limpar");
		btnLimparCurso.addActionListener(new ActionListener() {
			
			//Evento do botão limpar
			public void actionPerformed(ActionEvent e) {
				
				//Limpar os campos
				txtIdCursos.setText("");
				txtNomeCursos.setText("");
				cbCampus.setSelectedIndex(-1);
				cbPeriodo.setSelectedIndex(0);
				spDuracao.setValue(1);
			}
		});
		btnLimparCurso.setBounds(306, 277, 109, 28);
		panel_1.add(btnLimparCurso);
		
		//Botão Voltar
		JButton btnVoltarCursoAdicionar = new JButton("Voltar");
		btnVoltarCursoAdicionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Habilita e volta para a aba "Visualizar"
				tabbedPane.setEnabledAt(0, true);
				tabbedPane.setSelectedIndex(0);
				
				tabbedPane.setEnabledAt(1, false); // Desabilita a aba "Adicionar" novamente
			}
		});
		btnVoltarCursoAdicionar.setBounds(443, 277, 109, 28);
		panel_1.add(btnVoltarCursoAdicionar);
		
		// ======== Eventos ========
		// Atualiza tabela sempre que a aba "Visualizar" for selecionada
		tabbedPane.addChangeListener(e -> {
					
		if (tabbedPane.getSelectedIndex() == 0)
		{ 
		
		// Índice 0 corresponde à aba "Visualizar"	
		carregarTabelaCursos();
		
		 // Desabilita as abas "Adicionar" e "Alterar" ao voltar para a aba "Visualizar"
        tabbedPane.setEnabledAt(1, false);
        tabbedPane.setEnabledAt(tabbedPane.indexOfTab("Gerenciar Grade"), false);
        tabbedPane.setEnabledAt(tabbedPane.indexOfTab("Adicionar a Grade"), false);
		
		}
		
		if (tabbedPane.getSelectedIndex() == tabbedPane.indexOfTab("Gerenciar Grade"))
		{
			tabbedPane.setEnabledAt(tabbedPane.indexOfTab("Adicionar a Grade"), false);
		}
				});
		
		//Desativa a aba de Adicionar no início
		tabbedPane.setEnabledAt(1, false);
				
		
		//Tabela da Grade ao apertar Gerenciar Grade	
		String[] colunasGrade = { "ID", "Nome da Disciplina", "Semestre" };

		TabelaFactory.TabelaComponent tabelaGradeComp = criarTabela.criarTabela(colunasGrade, 100, 134, 377, 220);

		tableDisciplinasGrade = tabelaGradeComp.tabela;
		modeloTabelaGrade = tabelaGradeComp.modelo;
		sorterGrade = tabelaGradeComp.sorter;
		
		//Adicionar aqui
	    sorterGrade = new TableRowSorter<>(modeloTabelaGrade);
	    tableDisciplinasGrade.setRowSorter(sorterGrade);
	    
		//Carrega sempre que abre a página
		carregarTabelaDisciplinas();
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Alterar", null, panel_2, null);
		tabbedPane.setEnabledAt(2, false);
		
		//Desativa a aba de Alterar no início
		tabbedPane.setEnabledAt(2, false);
		panel_2.setLayout(null);
		
		txtNomeCursoAlterar = new JTextField();
		txtNomeCursoAlterar.setColumns(10);
		txtNomeCursoAlterar.setBounds(190, 121, 132, 18);
		panel_2.add(txtNomeCursoAlterar);
		
		JLabel lblNomeCursos_1 = new JLabel("Nome:");
		lblNomeCursos_1.setBounds(136, 124, 44, 12);
		panel_2.add(lblNomeCursos_1);
		
		txtIdCursoAlterar = new JTextField();
		txtIdCursoAlterar.setColumns(10);
		txtIdCursoAlterar.setBounds(425, 76, 142, 18);
		panel_2.add(txtIdCursoAlterar);
		
		JLabel lblIDCursos_1 = new JLabel("ID:");
		lblIDCursos_1.setBounds(391, 78, 34, 12);
		panel_2.add(lblIDCursos_1);
		
		JLabel lblCampus_1 = new JLabel("Campus:");
		lblCampus_1.setBounds(136, 78, 44, 12);
		panel_2.add(lblCampus_1);
		
		cbPeriodoAlterar = new JComboBox();
		carregarCB.carregarPeriodos(cbPeriodoAlterar, false);
		cbPeriodoAlterar.setBounds(425, 121, 142, 18);
		panel_2.add(cbPeriodoAlterar);
		
		JLabel lblPeriodo_1 = new JLabel("Periodo:");
		lblPeriodo_1.setBounds(381, 124, 44, 12);
		panel_2.add(lblPeriodo_1);
		
		JLabel lblDuracao_1 = new JLabel("Duração em Semestres:");
		lblDuracao_1.setBounds(136, 166, 120, 28);
		panel_2.add(lblDuracao_1);
		
		spDuracaoAlterar = new JSpinner();
		spDuracaoAlterar.setBounds(255, 169, 67, 25);
		panel_2.add(spDuracaoAlterar);
		
		//Botão Salvar na aba Alterar
		JButton btnSalvarCurso_1 = new JButton("Salvar");
		btnSalvarCurso_1.addActionListener(new ActionListener() {
			
			//Evento do botão salvar
			public void actionPerformed(ActionEvent e) {
				
				// Alterar
				curso = new Curso();
				
				// Populando o objeto curso
				curso.setId_curso(Integer.parseInt(txtIdCursoAlterar.getText()));
				curso.setNome_curso(txtNomeCursoAlterar.getText());
				curso.setPeriodo(cbPeriodoAlterar.getSelectedItem().toString());
				curso.setDuracao_semestre((int) spDuracaoAlterar.getValue());
				curso.setId_campus(((Campus)cbCampusAlterar.getSelectedItem()).getId());
				
				// Chama CursoDAO >> abre o banco de dados
				try {
					dao = new CursoDAO();
					dao.alterar(curso);
					JOptionPane.showMessageDialog(null,"Alterado com sucesso!");
					
					//Atualiza a tabela de Campus
					carregarTabelaCursos();
					
					// Habilita e volta para a aba "Visualizar"
					tabbedPane.setEnabledAt(0, true);	
					tabbedPane.setSelectedIndex(0);
					tabbedPane.setEnabledAt(2, false);
					
					
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Aconteceu erro: " + e1.getMessage());
				}	
			}
		});
		btnSalvarCurso_1.setBounds(174, 277, 106, 28);
		panel_2.add(btnSalvarCurso_1);
		
		//Botão Limpar
		JButton btnLimparCurso_1 = new JButton("Limpar");
		btnLimparCurso_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Limpar os campos
				txtNomeCursoAlterar.setText("");
				cbCampusAlterar.setSelectedIndex(-1);
				cbPeriodoAlterar.setSelectedIndex(0);
				spDuracaoAlterar.setValue(1);
			}
		});
		btnLimparCurso_1.setBounds(308, 277, 106, 28);
		panel_2.add(btnLimparCurso_1);
		
		//Botão Voltar
		JButton btnVoltarCursoAlterar = new JButton("Voltar");
		btnVoltarCursoAlterar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Volta para a aba "Visualizar"
				tabbedPane.setEnabledAt(0, true); // Habilita a aba "Visualizar"
				tabbedPane.setSelectedIndex(0); // Volta a aba "Visualizar"
				
				tabbedPane.setEnabledAt(2, false); // Desabilita a aba "Alterar"
			}
		});
		btnVoltarCursoAlterar.setBounds(441, 277, 106, 28);
		panel_2.add(btnVoltarCursoAlterar);
		
		cbCampusAlterar = new JComboBox();
		cbCampusAlterar.setBounds(190, 75, 132, 18);
		panel_2.add(cbCampusAlterar);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Gerenciar Grade", null, panel_3, null);
		
		//Desativa a aba de Gerenciar no início
		tabbedPane.setEnabledAt(3, false);
		panel_3.setLayout(null);
		
				panel_3.add(tabelaGradeComp.scrollPane);
				
				
				JLabel lblNewLabel_3 = new JLabel("Gerenciamento da Grade do Curso");
				lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 16));
				lblNewLabel_3.setBounds(100, 40, 244, 18);
				panel_3.add(lblNewLabel_3);
				
				JLabel lblNewLabel_4 = new JLabel("Curso Selecionado:");
				lblNewLabel_4.setBounds(100, 91, 96, 15);
				panel_3.add(lblNewLabel_4);
				
				txtCursoSelecionadoGrade = new JTextField();
				txtCursoSelecionadoGrade.setBounds(197, 88, 208, 18);
				panel_3.add(txtCursoSelecionadoGrade);
				txtCursoSelecionadoGrade.setColumns(10);
				
				JButton btnAdicionarDisciplinaGrade = new JButton("Adicionar Disciplina");
				btnAdicionarDisciplinaGrade.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						int linha = tableCursos.getSelectedRow();
						
						int idCurso = Integer.parseInt(tableCursos.getValueAt(linha, 0).toString());
						int duracao = Integer.parseInt(tableCursos.getValueAt(linha, 4).toString()); // coluna duração
						// Pega nome curso
						String nomeCurso = tableCursos.getValueAt(linha, 2).toString();
						txtNomeDoCursoGrade.setText(nomeCurso);
						
						// Popular os períodos conforme a duração
						cbSelecionarSemestreAdicionar.removeAllItems();
						for (int i = 0; i <= duracao; i++) {
							
							if (i==0)
							{
								cbSelecionarSemestreAdicionar.addItem("Selecione uma Opção: ");
							}
							else
							{
								cbSelecionarSemestreAdicionar.addItem(i + "º Semestre");
							}
							
						}

						
						txtNomeDoCursoGrade.setEditable(false); // impede edição
						txtNomeDoCursoGrade.setFocusable(false);
						
						cursoSelecionadoId = idCurso; // salva o ID do curso para usar no sal
						// Ativa aba e vai para ela
						tabbedPane.setEnabledAt(4, true);
						tabbedPane.setSelectedIndex(4);
						
						// Desabilita a aba "Gerenciar Grade"
						tabbedPane.setEnabledAt(3, false);
						
						carregarTabelaAdicionarDisciplinas();
					}
				});
				btnAdicionarDisciplinaGrade.setBounds(509, 161, 160, 31);
				panel_3.add(btnAdicionarDisciplinaGrade);
				
				//Botão de Remover a Disciplina
				JButton btnRemoverDisciplinaGrade = new JButton("Remover Disciplina");
				btnRemoverDisciplinaGrade.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						
						int linha = tableDisciplinasGrade.getSelectedRow();
				        
				        if (linha == -1) {
				            JOptionPane.showMessageDialog(null, "Selecione uma disciplina para remover.");
				            return;
				        }

				        int confirmacao = JOptionPane.showConfirmDialog(null, "Deseja remover essa disciplina do curso?","Confirmação",JOptionPane.YES_NO_OPTION
				        );

				        if (confirmacao != JOptionPane.YES_OPTION) return;

				        // Pega ID da disciplina na tabela da grade
				        int idDisciplina = Integer.parseInt(
				            tableDisciplinasGrade.getValueAt(linha, 0).toString()
				        );

				        try {
				            CursoDisciplinaDAO cdDAO = new CursoDisciplinaDAO();
				            cdDAO.excluirDisciplinaDoCurso(cursoSelecionadoId, idDisciplina);

				            JOptionPane.showMessageDialog(null, "Disciplina removida com sucesso!");

				            // recarregar grade
				            carregarDisciplinasDoCurso(cursoSelecionadoId);

				        } catch (Exception ex) {
				            JOptionPane.showMessageDialog(null, "Erro ao remover: " + ex.getMessage());
				        }
					}
				});
				btnRemoverDisciplinaGrade.setBounds(509, 227, 160, 31);
				panel_3.add(btnRemoverDisciplinaGrade);
				
				JButton btnVoltarGrade = new JButton("Voltar");
				btnVoltarGrade.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// Habilita a aba "Visualizar" novamente e vai para ela
						tabbedPane.setEnabledAt(0, true);
						tabbedPane.setSelectedIndex(0);
						
						tabbedPane.setEnabledAt(3, false);	// Desabilita a aba "Gerenciar Grade" novamente			
					}
				});
				btnVoltarGrade.setBounds(509, 291, 160, 31);
				panel_3.add(btnVoltarGrade);
		
		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("Adicionar a Grade", null, panel_4, null);
		panel_4.setLayout(null);
		
		String[] colunasAdicionar = { "Selecionar", "ID", "Disciplina" };

		TabelaFactory.TabelaComponent tabelaAddComp = criarTabela.criarTabela(colunasAdicionar, 20, 70, 690, 220);

		panel_4.add(tabelaAddComp.scrollPane);

		tableAdicionarDisciplinas = tabelaAddComp.tabela;
		modeloTabelaAdicionarGrade = tabelaAddComp.modelo;
		
		//Para as checkBox 
		tableAdicionarDisciplinas.getColumnModel().getColumn(0).setCellEditor(new javax.swing.DefaultCellEditor(new javax.swing.JCheckBox()));
		tableAdicionarDisciplinas.getColumnModel().getColumn(0).setCellRenderer(tableAdicionarDisciplinas.getDefaultRenderer(Boolean.class));
		
		txtNomeDoCursoGrade = new JTextField();
		txtNomeDoCursoGrade.setColumns(10);
		txtNomeDoCursoGrade.setBounds(78, 18, 286, 16);
		panel_4.add(txtNomeDoCursoGrade);
		
		JLabel lblNewLabel_5 = new JLabel("Cursos:");
		lblNewLabel_5.setBounds(24, 20, 44, 12);
		panel_4.add(lblNewLabel_5);
		
		cbSelecionarSemestreAdicionar = new JComboBox();
		cbSelecionarSemestreAdicionar.setBounds(78, 44, 93, 16);
		panel_4.add(cbSelecionarSemestreAdicionar);
		
		JLabel lblNewLabel_6 = new JLabel("Semestre:");
		lblNewLabel_6.setBounds(24, 46, 55, 12);
		panel_4.add(lblNewLabel_6);
		
		//Botão Salvar do Adicionar Grade
		JButton btnSalvarAdicionarAGrade = new JButton("Salvar");
		btnSalvarAdicionarAGrade.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int linha = tableCursos.getSelectedRow();
		        int totalLinhas = modeloTabelaAdicionarGrade.getRowCount();

		        int semestre = cbSelecionarSemestreAdicionar.getSelectedIndex();
		        
		        if (cbSelecionarSemestreAdicionar.getSelectedIndex() == 0) {
		            JOptionPane.showMessageDialog(null, 
		                "⚠️ Selecione um semestre para continuar!", 
		                "Aviso",
		                JOptionPane.WARNING_MESSAGE
		            );
		            return; // impede continuar
		        }
		        int idCurso = cursoSelecionadoId;
		        
		        List<Integer> disciplinasSelecionadas = new ArrayList<>();

		        for (int i = 0; i < totalLinhas; i++) {
		            Boolean selecionado = (Boolean) modeloTabelaAdicionarGrade.getValueAt(i, 0);
		            if (selecionado != null && selecionado) {
		                int idDisciplina = Integer.parseInt(modeloTabelaAdicionarGrade.getValueAt(i, 1).toString());
		                disciplinasSelecionadas.add(idDisciplina);
		            }
		        }

		        // ✅ Só valida aqui — fora do loop
		        if (disciplinasSelecionadas.isEmpty()) {
		            JOptionPane.showMessageDialog(null, "Selecione ao menos uma disciplina.");
		            return;
		        }

		        try {
		            CursoDisciplinaDAO cdDAO = new CursoDisciplinaDAO();

		            // ✅ Salva todas só aqui — fora do loop da tabela
		            for (int idDisciplina : disciplinasSelecionadas) {
		                CursoDisciplina cd = new CursoDisciplina();
		                cd.setId_curso(idCurso);
		                cd.setId_disciplina(idDisciplina);
		                cd.setSemestre(semestre);
		                cdDAO.salvar(cd);
		            }

		            JOptionPane.showMessageDialog(null, "Disciplinas adicionadas com sucesso!");
		            cbSelecionarSemestreAdicionar.setSelectedIndex(0);
		            
		            // Habilita a aba "Gerenciar Grade" e retorna a ela
		            tabbedPane.setEnabledAt(3, true);
		            tabbedPane.setSelectedIndex(3);
		            
		            // ✅ Recarregar a grade automaticamente
		            carregarDisciplinasDoCurso(idCurso);
		            
		            // Bloquear a aba adicionar
		            tabbedPane.setEnabledAt(4, false);

		        } catch (Exception ex) {
		            JOptionPane.showMessageDialog(null, "Erro ao salvar: " + ex.getMessage());
		        }
			}
		});
		btnSalvarAdicionarAGrade.setBounds(248, 321, 100, 35);
		panel_4.add(btnSalvarAdicionarAGrade);
		
		JButton btnVoltarAdicionarGrade = new JButton("Voltar");
		btnVoltarAdicionarGrade.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Habilita a aba "Gerenciar Grade" e retorna a ela
				tabbedPane.setEnabledAt(3, true);
				tabbedPane.setSelectedIndex(3);
				
				
				tabbedPane.setEnabledAt(4, false); // Desabilita a aba "Adicionar Grade" novamente
			}
		});
		btnVoltarAdicionarGrade.setBounds(374, 321, 100, 35);
		panel_4.add(btnVoltarAdicionarGrade);
		
		tabbedPane.setEnabledAt(4, false);
		
		//Carrega a tabela de Cursos ao iniciar a tela
		carregarTabelaCursos();
	}
	
	
	//Método para carregar a tabela de Cursos
		private void carregarTabelaCursos() {
			try {
				dao = new CursoDAO();
				CampusDAO campusDAO = new CampusDAO();
				List<Curso> lista = dao.listarTodos();

				modeloTabela.setRowCount(0); // limpa a tabela

				for (Curso c : lista) {
					
					// Buscar o nome do Campus
		            Campus campus = campusDAO.consultarPorId(c.getId_campus());
		            String nomeCampus = (campus != null ? campus.getNome() : "N/A");
					modeloTabela.addRow(new Object[] {
						c.getId_curso(),
						nomeCampus,
						c.getNome_curso(),
						c.getPeriodo(),
						c.getDuracao_semestre(),
					});
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Erro ao carregar tabela: " + e.getMessage());
			}
		}
		
		//Método para unir filtros 
		private void aplicarFiltros() {
			
		    List<RowFilter<Object,Object>> filtros = new ArrayList<>();

		    // Filtro por período
		    String periodo = cbPeriodoFiltrar.getSelectedItem().toString();
		    if (!periodo.equals("Todos")) {
		        filtros.add(RowFilter.regexFilter("^" + periodo + "$", 3)); // coluna 3 = Período
		    }

		    // Filtro por campus
		    String campus = cbCampusFiltrar.getSelectedItem().toString();
		    if (!campus.equals("Todos")) {
		        filtros.add(RowFilter.regexFilter("^" + campus + "$", 1)); // coluna 1 = Campus
		    }

		    // Filtro por nome do curso
		    String busca = txtCursoFiltrar.getText().trim();
		    if (!busca.isEmpty()) {
		        filtros.add(RowFilter.regexFilter("(?i)" + busca, 2)); // coluna 2 = Nome do Curso
		    }

		    // Junta todos os filtros (AND)
		    if (filtros.isEmpty()) {
		        sorter.setRowFilter(null);
		    } else {
		        sorter.setRowFilter(RowFilter.andFilter(filtros));
		    }
		}
		
		private void carregarTabelaDisciplinas() {
			try {
				disciplinaDAO = new DisciplinaDAO();
				DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
				List<Disciplina> lista = disciplinaDAO.listarTodos();

				modeloTabelaGrade.setRowCount(0); // limpa a tabela

				for (Disciplina d : lista) {
					modeloTabelaGrade.addRow(new Object[] {
						d.getId(),
						d.getNome(),
					});
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Erro ao carregar tabela: " + e.getMessage());
			}
		}
		
		private void carregarTabelaAdicionarDisciplinas() {
		    try {
		        disciplinaDAO = new DisciplinaDAO();
		        List<Disciplina> lista = disciplinaDAO.listarTodasNaoCadastradas(cursoSelecionadoId);

		        modeloTabelaAdicionarGrade.setRowCount(0); // Limpa tabela

		        for (Disciplina d : lista) {
		            modeloTabelaAdicionarGrade.addRow(new Object[] {
		                false, // checkbox inicia desmarcado
		                d.getId(),
		                d.getNome()
		            });
		        }
		    } catch (Exception e) {
		        JOptionPane.showMessageDialog(null, "Erro ao carregar disciplinas: " + e.getMessage());
		    }
		}
		
		private void carregarDisciplinasDoCurso(int idCurso) {
		    try {
		    	 CursoDisciplinaDAO cdDAO = new CursoDisciplinaDAO();
		         List<CursoDisciplina> lista = cdDAO.listarDisciplinasPorCurso(idCurso);

		         modeloTabelaGrade.setRowCount(0);

		         for (CursoDisciplina cd : lista) {
		             modeloTabelaGrade.addRow(new Object[] {
		                 cd.getDisciplina().getId(),
		                 cd.getDisciplina().getNome(),
		                 cd.getSemestre() + "ºSemestre"
		             });
		        }
		    } catch (Exception e) {
		        JOptionPane.showMessageDialog(null, "Erro ao carregar disciplinas do curso: " + e.getMessage());
		    }
		}
		
		
}
