package br.edu.fatecgru.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import br.edu.fatecgru.dao.AlunoDAO;
import br.edu.fatecgru.dao.AlunoDisciplinaDAO;
import br.edu.fatecgru.dao.AlunoNotaFaltaDAO;
import br.edu.fatecgru.dao.CursoDAO;
import br.edu.fatecgru.dao.CursoDisciplinaDAO;
import br.edu.fatecgru.dao.DisciplinaDAO;
import br.edu.fatecgru.model.Aluno;
import br.edu.fatecgru.model.AlunoNotaFalta;
import br.edu.fatecgru.model.Campus;
import br.edu.fatecgru.model.Curso;
import br.edu.fatecgru.model.CursoDisciplina;
import br.edu.fatecgru.model.Endereco;
import br.edu.fatecgru.service.BoletimService;
import br.edu.fatecgru.util.AplicarMascaras;
import br.edu.fatecgru.util.BuscaPorAPI;
import br.edu.fatecgru.util.CarregarComboBox;
import br.edu.fatecgru.util.TabelaFactory;
import br.edu.fatecgru.util.ValidarCampos;

public class TelaAlunos extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JTextField txtRA;
	private JTextField txtNome;
	private JFormattedTextField txtCelular;
	private JFormattedTextField txtDataNasc;
	private JFormattedTextField txtCpf;
	private JTextField txtEmail;
	private JTextField txtEndereco;
	private JFormattedTextField txtCep;
	private JComboBox cbCidade;
	private JComboBox cbEstado;
	private JComboBox cbCampus;
	private JComboBox cbCurso;
	private JComboBox cbDisciplinasNotas;
	private JComboBox cbCampusVisualizarAlunos;
	private JComboBox cbCursosVisualizarAlunos;
	private JButton btnSalvarAluno;
	private JButton btnSalvar;
	private JButton btnAlterar;
	private JTextField txtBuscarNomeAluno;
	private JTextField txtUF;
	private JTextField txtNumero;
	private JTable tableAlunosVisualizar;
	private JTable tableDisciplinas;
	private DefaultTableModel modeloTabelaAlunosVisualizar;
	private DefaultTableModel modeloDisciplinas;
	private TableRowSorter<DefaultTableModel> sorter;
	private int idCursoSelecionado;
	
	
	//Variaveis de classe --> Não é recomendado cria-las aqui 
	private AlunoDAO dao;
	private Aluno aluno;
	private DisciplinaDAO disciplinaDAO;

	private JTextField txtRANotas;
	private JTextField txtFaltas;
	private JTextField txtNomeNotas;
	private JTabbedPane tbAluno; 
	private JTextField txtNomeCampusNotas;
	private JTextField txtNota1;
	private JTextField txtNota2;
	private JTextField txtNomeCursoNotas;
	private JEditorPane editorBoletim;
	

	/**
	 * Create the panel.
	 */
	public TelaAlunos() {					
				setLayout(null);
				setBorder(new EmptyBorder(5, 5, 5, 5));
				setLayout(null);
				
				
				//Variavel para utilidades
				CarregarComboBox carregarCB = new CarregarComboBox();
				AplicarMascaras aplicarMascara = new AplicarMascaras();
				ValidarCampos validarCampos = new ValidarCampos();
				TabelaFactory criarTabela = new TabelaFactory();
				
				tbAluno = new JTabbedPane(JTabbedPane.TOP);
				tbAluno.setBounds(44, 55, 739, 431);
				add(tbAluno);
				
				//Aba de Visualizar Alunos
				JPanel panel_4 = new JPanel();
				tbAluno.addTab("Visualizar", null, panel_4, null);
				panel_4.setLayout(null);
				
				// Listener para limpar os filtros toda vez que voltar para a aba "Visualizar"
				tbAluno.addChangeListener(new ChangeListener() {
				    @Override
				    public void stateChanged(ChangeEvent e) {
				        int index = tbAluno.getSelectedIndex();

				        // Se a aba selecionada for "Visualizar"
				        if (index == 0) {
				            limparFiltros();
				        }
				    }
				});
				
				//Criação da Tabela 
				
				// Definição das colunas
				String[] colunasAlunos = {"RA", "Nome", "Email", "Celular", "CPF", "Campus", "Curso"};

				TabelaFactory.TabelaComponent tabelaComp = criarTabela.criarTabela(colunasAlunos, 21, 125, 690, 170);

				// Adiciona o scroll ao painel
				panel_4.add(tabelaComp.scrollPane);

				// Pega referências para usar depois
				tableAlunosVisualizar = tabelaComp.tabela;
				modeloTabelaAlunosVisualizar = tabelaComp.modelo;
				sorter = tabelaComp.sorter;
				
				JButton btnAdicionarNovoAluno = new JButton("Adicionar Aluno");
				btnAdicionarNovoAluno.setFont(new Font("Segoe UI", Font.PLAIN, 11));
				btnAdicionarNovoAluno.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						tbAluno.setEnabledAt(1, true);    // habilita a aba "Dados Pessoais"
					    tbAluno.setSelectedIndex(1);      // muda automaticamente para a aba "Dados Pessoais"
					    
					    tbAluno.setEnabledAt(0, false);    // Desabilita a aba "Visualizar"
					
					    // Habilta o botão "Salvar" e desabilita o botão "Alterar"
					    btnSalvar.setVisible(true);
					    btnAlterar.setVisible(false);
					}
				});
				btnAdicionarNovoAluno.setBounds(85, 316, 150, 30);
				panel_4.add(btnAdicionarNovoAluno);
				
				txtBuscarNomeAluno = new JTextField();
				txtBuscarNomeAluno.addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						
						aplicarFiltros(); 
					}
				});
				txtBuscarNomeAluno.setBounds(159, 21, 379, 23);
				panel_4.add(txtBuscarNomeAluno);
				txtBuscarNomeAluno.setColumns(10);
				
				cbCursosVisualizarAlunos = new JComboBox();
				cbCursosVisualizarAlunos.setFont(new Font("Perpetua", Font.PLAIN, 17));
				cbCursosVisualizarAlunos.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						
						 if (e.getStateChange() == ItemEvent.SELECTED) {
					            aplicarFiltros();
					        }
					}
				});
				cbCursosVisualizarAlunos.setBounds(159, 92, 429, 23);
				panel_4.add(cbCursosVisualizarAlunos);
				
				cbCampusVisualizarAlunos = new JComboBox();
				cbCampusVisualizarAlunos.setFont(new Font("Perpetua", Font.PLAIN, 17));
				cbCampusVisualizarAlunos.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						
						cbCursosVisualizarAlunos.removeAllItems();

						if (!cbCampusVisualizarAlunos.getSelectedItem().toString().equals("Todos")) {

						    Campus campusSel = (Campus) cbCampusVisualizarAlunos.getSelectedItem();
						    carregarCB.carregarCursos(cbCursosVisualizarAlunos, campusSel.getId(), true); // true -> "Todos"

						} else {
						    // Se campus = Todos, mostra só "Todos"
						    cbCursosVisualizarAlunos.addItem("Todos");
						}

						aplicarFiltros();
					}
				});
				cbCampusVisualizarAlunos.setBounds(159, 59, 479, 23);
				panel_4.add(cbCampusVisualizarAlunos);
				
				carregarCB.carregarCampus(cbCampusVisualizarAlunos, true); //True para incluir "Todos"
				
				
				
				JLabel lblNewLabel = new JLabel("Campus:");
				lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
				lblNewLabel.setBounds(97, 57, 72, 25);
				panel_4.add(lblNewLabel);
				
				
				
				JLabel lblNewLabel_3 = new JLabel("Cursos:");
				lblNewLabel_3.setFont(new Font("Segoe UI", Font.PLAIN, 13));
				lblNewLabel_3.setBounds(97, 93, 44, 25);
				panel_4.add(lblNewLabel_3);
				
				JButton btnAlterarNovoAluno = new JButton("Alterar Aluno");
				btnAlterarNovoAluno.setFont(new Font("Segoe UI", Font.PLAIN, 11));
				btnAlterarNovoAluno.addActionListener(new ActionListener() {
					//Ao clicar no botão Alterar
					public void actionPerformed(ActionEvent e) {
						int linhaSelecionada = tableAlunosVisualizar.getSelectedRow();

				        // Verifica se alguma linha foi selecionada
				        if (linhaSelecionada == -1) {
				            JOptionPane.showMessageDialog(null, "Selecione um aluno para alterar.");
				            return;
				        }

				        try {
				            // Pega o RA (primeira coluna da tabela)
				            int ra = Integer.parseInt(tableAlunosVisualizar.getValueAt(linhaSelecionada, 0).toString());

				            // Busca os dados completos do aluno no banco
				            AlunoDAO dao = new AlunoDAO();
				            Aluno aluno = dao.consultarPorRA(ra); // cria esse método no DAO

				            if (aluno == null) {
				                JOptionPane.showMessageDialog(null, "Aluno não encontrado no banco de dados.");
				                return;
				            }

				            // Habilita e vai para a aba "Dados Pessoais"
				            tbAluno.setEnabledAt(1, true);
				            tbAluno.setSelectedIndex(1);

				            // Preenche os campos com os dados do aluno
				            txtRA.setText(String.valueOf(aluno.getRa()));
				            txtRA.setEditable(false); // RA não pode ser alterado
				            txtNome.setText(aluno.getNome());
				            txtDataNasc.setText(aluno.getDataNascimento());
				            txtCpf.setText(aluno.getCpf());
				            txtCelular.setText(aluno.getCelular());
				            txtEmail.setText(aluno.getEmail());
				            txtCep.setText(aluno.getCep());
				            txtEndereco.setText(aluno.getEndereco());
				            txtNumero.setText(String.valueOf(aluno.getNumero()));
				            cbEstado.setSelectedItem(aluno.getEstado());
				            cbCidade.setSelectedItem(aluno.getCidade());
				            txtUF.setText(aluno.getUf());
				            int idCurso = aluno.getId_curso();
				            
				            tbAluno.setEnabledAt(1, true);    // habilita a aba "Dados Pessoais"
						    tbAluno.setSelectedIndex(1);      // muda automaticamente para a aba "Dados Pessoais"
						    
						    tbAluno.setEnabledAt(0, false);    // Desabilita a aba "Visualizar"
						    
						    // Agora populando o campus e o curso na aba "Curso"
						    CursoDAO cursoDAO = new CursoDAO();
				            Curso cursoSelecionado = cursoDAO.consultar(idCurso);

				            if (cursoSelecionado != null) {
				                int idCampus = cursoSelecionado.getId_campus();

				                // Seleciona o Campus no ComboBox
				                for (int i = 0; i < cbCampus.getItemCount(); i++) {
				                    Object item = cbCampus.getItemAt(i);
				                    if (item instanceof Campus) {
				                        Campus campusObj = (Campus) item;
				                        if (campusObj.getId() == idCampus) {
				                            cbCampus.setSelectedIndex(i);
				                            break;
				                        }
				                    }
				                }

				                // Agora que o campus foi selecionado, o ComboBox de cursos já deve ser atualizado.
				                // Então seleciona o curso correspondente:
				                for (int i = 0; i < cbCurso.getItemCount(); i++) {
				                    Object item = cbCurso.getItemAt(i);
				                    if (item instanceof Curso) {
				                        Curso cursoObj = (Curso) item;
				                        if (cursoObj.getId_curso() == idCurso) {
				                            cbCurso.setSelectedIndex(i);
				                            break;
				                        }
				                    }
				                }
				            }
				           
				         // Habilta o botão "Salvar" e desabilita o botão "Alterar"
						    btnSalvar.setVisible(false);
						    btnAlterar.setVisible(true);


				        } catch (Exception ex) {
				            JOptionPane.showMessageDialog(null, "Erro ao carregar dados do aluno: " + ex.getMessage());
				            ex.printStackTrace();
				        }

				}
				});
				btnAlterarNovoAluno.setBounds(280, 315, 150, 30);
				panel_4.add(btnAlterarNovoAluno);
				
				JButton btnExcluirNovoAluno = new JButton("Excluir Aluno");
				btnExcluirNovoAluno.setFont(new Font("Segoe UI", Font.PLAIN, 11));
				btnExcluirNovoAluno.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						// Excluir
						aluno = new Aluno();
						
						//Pegar o ID
						int linhaSelecionada = tableAlunosVisualizar.getSelectedRow();
						
						//Verifica se alguma linha foi selecionada
						if (linhaSelecionada == -1)
						{
							JOptionPane.showMessageDialog(null, "Selecione um aluno para excluir.");
							return;
						}
						
						// Confirmação de exclusão
				        int confirmacao = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir este aluno?", "Confirmação", JOptionPane.YES_NO_OPTION);

				        if (confirmacao != JOptionPane.YES_OPTION) return;; // Sai se o usuário não confirmar
						
						//Pega o ID da linha selecionada
				        int ra = Integer.parseInt(tableAlunosVisualizar.getValueAt(linhaSelecionada, 0).toString());
						
						try {
							dao = new AlunoDAO();
							aluno = new Aluno();
							aluno.setRa(ra); // Define o RA do aluno a ser excluído
							dao.excluirCompleto(ra);
							JOptionPane.showMessageDialog(null,"Excluído com sucesso!");
							
							//Atualiza a tabela de Campus
							carregarTabelaAlunos();
							
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null,"Erro ao excluir: " + e1.getMessage());
						}
						
					}
				});
				btnExcluirNovoAluno.setBounds(475, 315, 150, 30);
				panel_4.add(btnExcluirNovoAluno);
				
				
				
				
				
				JLabel lblNewLabel_8 = new JLabel("Nome:");
				lblNewLabel_8.setFont(new Font("Segoe UI", Font.PLAIN, 13));
				lblNewLabel_8.setBounds(97, 20, 44, 25);
				panel_4.add(lblNewLabel_8);
				
				JButton btnLimparFiltros = new JButton("Limpar Filtros");
				btnLimparFiltros.setFont(new Font("Segoe UI", Font.PLAIN, 11));
				btnLimparFiltros.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						// ✅ Reseta o combo do campus
						cbCampusVisualizarAlunos.setSelectedIndex(0); // "Todos"

				        // ✅ Reseta o combo do período
				        cbCursosVisualizarAlunos.setSelectedIndex(0); // "Todos"

				        // ✅ Limpa a busca por nome
				        txtBuscarNomeAluno.setText("");

				        // ✅ Reaplica os filtros (vai mostrar tudo)
				        aplicarFiltros();
					}
				});
				btnLimparFiltros.setBounds(280, 355, 150, 30);
				panel_4.add(btnLimparFiltros);
				
				JButton btnNotasFaltas = new JButton("Gerenciar Notas/Faltas");
				btnNotasFaltas.setFont(new Font("Segoe UI", Font.PLAIN, 11));
				btnNotasFaltas.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						int linha = tableAlunosVisualizar.getSelectedRow();

				        if (linha == -1) {
				            JOptionPane.showMessageDialog(null, "Selecione um aluno primeiro!");
				            return;
				        }
				        
				        // Pegando os valores da linha selecionada
				        String ra = tableAlunosVisualizar.getValueAt(linha, 0).toString();
				        String nome = tableAlunosVisualizar.getValueAt(linha, 1).toString();
				        String campus = tableAlunosVisualizar.getValueAt(linha, 5).toString();
				        String curso = tableAlunosVisualizar.getValueAt(linha, 6).toString();
				        
				        // Preenche os campos da aba de notas
				        txtRANotas.setText(ra);
				        txtNomeNotas.setText(nome);
				        txtNomeCampusNotas.setText(campus);
				        txtNomeCursoNotas.setText(curso);
				        
				        // Habilita e abre a aba de Notas e Faltas
				        tbAluno.setEnabledAt(3, true);
				        tbAluno.setSelectedIndex(3);
				        
				     // Desabilita a aba Visualizar, só deixando voltar no botão
				        tbAluno.setEnabledAt(0, false);
				        
				        // Preenche o comboBox de disciplinas
				        carregarCB.carregarDisciplinasPorAluno(cbDisciplinasNotas, ra);

					}
				});
				btnNotasFaltas.setBounds(85, 355, 150, 30);
				panel_4.add(btnNotasFaltas);
				
				JButton btnGerarBoletim = new JButton("Gerar Boletim");
				btnGerarBoletim.setFont(new Font("Segoe UI", Font.PLAIN, 11));
				btnGerarBoletim.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						int linha = tableAlunosVisualizar.getSelectedRow();

				        if (linha == -1) {
				            JOptionPane.showMessageDialog(null, "Selecione um aluno primeiro!");
				            return;
				        }
				        
				        int raAluno = Integer.parseInt(tableAlunosVisualizar.getValueAt(linha, 0).toString());

				        try {
				            BoletimService boletimService = new BoletimService();
				            String html = boletimService.gerarBoletimHtml(raAluno);
				            editorBoletim.setText(html);

				            tbAluno.setEnabledAt(4, true);
				            tbAluno.setSelectedIndex(4);
				            editorBoletim.setText(html);
				            
				            // Habilitar aba Boletim
					        tbAluno.setEnabledAt(4, true);

					        // Ir para a aba Boletim
					        tbAluno.setSelectedIndex(4);
					        
					        // Desabilita aba Visualizar
					        tbAluno.setEnabledAt(0, false);

				        } catch (Exception ex) {
				            JOptionPane.showMessageDialog(null, "Erro ao gerar boletim: " + ex.getMessage());
				            ex.printStackTrace();
				        }
				        
					   
				        
				        
					}
				});
				btnGerarBoletim.setBounds(475, 355, 150, 30);
				panel_4.add(btnGerarBoletim);
				
				//Atualizar a tabela 
				carregarTabelaAlunos();
				
				//Tab de Dados pessoais e o que tem dentro dela
				JPanel panel = new JPanel();
				tbAluno.addTab("Dados Pessoais", null, panel, null);
				panel.setLayout(null);
				
				tbAluno.setEnabledAt(1, false);    // Desabilita a aba "Dados Pessoais" no início
				
				JLabel lblRAAluno = new JLabel("RA:");
				lblRAAluno.setFont(new Font("Segoe UI", Font.PLAIN, 20));
				lblRAAluno.setBounds(70, 85, 46, 25);  
				panel.add(lblRAAluno);
				
				txtRA = new JTextField();
				txtRA.setFont(new Font("Perpetua", Font.PLAIN, 17));  
				txtRA.setBounds(126, 85, 276, 26);  
				panel.add(txtRA);
				txtRA.setColumns(10);
				
				JLabel lblDataDeNascimentoAluno = new JLabel("Data de Nascimento:");
				lblDataDeNascimentoAluno.setFont(new Font("Segoe UI", Font.PLAIN, 14));  
				lblDataDeNascimentoAluno.setBounds(418, 35, 137, 25);  
				panel.add(lblDataDeNascimentoAluno);
				
				JLabel lblNomeAluno = new JLabel("Nome:");
				lblNomeAluno.setFont(new Font("Segoe UI", Font.PLAIN, 16));  
				lblNomeAluno.setBounds(70, 35, 51, 25); 
				panel.add(lblNomeAluno);
				
				txtNome = new JTextField();
				
				txtNome.setColumns(10);
				txtNome.setFont(new Font("Segoe UI", Font.PLAIN, 17));
				txtNome.setBounds(126, 35, 276, 26);
				panel.add(txtNome);
				
				JLabel lblCpfAluno = new JLabel("CPF:");
				lblCpfAluno.setFont(new Font("Segoe Ui", Font.PLAIN, 20));  
				lblCpfAluno.setBounds(418, 85, 51, 25);  
				panel.add(lblCpfAluno);
				
				JLabel lblCelularAluno = new JLabel("Celular:");  
				lblCelularAluno.setFont(new Font("Segoe UI", Font.PLAIN, 14));
				lblCelularAluno.setBounds(418, 135, 72, 25);  
				panel.add(lblCelularAluno);
				
				JLabel lblEmailAluno = new JLabel("Email:");
				lblEmailAluno.setFont(new Font("Segoe UI", Font.PLAIN, 15));  
				lblEmailAluno.setBounds(70, 135, 56, 25);  
				panel.add(lblEmailAluno);
				
				JLabel lblEnderecoAluno = new JLabel("Endereço:");
				lblEnderecoAluno.setFont(new Font("Segoe UI", Font.PLAIN, 16)); 
				lblEnderecoAluno.setBounds(70, 235, 118, 25);  
				panel.add(lblEnderecoAluno);
				
				txtEmail = new JTextField();
				txtEmail.setColumns(10);
				txtEmail.setFont(new Font("Perpetua", Font.PLAIN, 15)); 
				txtEmail.setBounds(126, 135, 276, 25);
				panel.add(txtEmail);
				
				txtEndereco = new JTextField();
				txtEndereco.setFont(new Font("Perpetua", Font.PLAIN, 15)); 
				txtEndereco.setColumns(10);
				txtEndereco.setBounds(152, 235, 250, 26);
				panel.add(txtEndereco);
				
				JLabel lblCidadeAluno = new JLabel("Cidade:");
				lblCidadeAluno.setFont(new Font("Segoe Ui", Font.PLAIN, 15)); 
				lblCidadeAluno.setBounds(332, 285, 96, 25);  
				panel.add(lblCidadeAluno);
				
				JLabel lblUfAluno = new JLabel("UF:");
				lblUfAluno.setFont(new Font("Segoe Ui", Font.PLAIN, 20)); 
				lblUfAluno.setBounds(418, 235, 44, 25);
				panel.add(lblUfAluno);
				
			    txtDataNasc = new JFormattedTextField();
			    txtDataNasc.setFont(new Font("Perpetua", Font.PLAIN, 17));  
			    txtDataNasc.setBounds(556, 35, 115, 26);  
				panel.add(txtDataNasc);
				
				//Adiciona a mascára de data
				AplicarMascaras.aplicar(txtDataNasc, "DATA");
				
			    txtCpf = new JFormattedTextField();
			    txtCpf.setFont(new Font("Perpetua", Font.PLAIN, 17)); 
			    txtCpf.setBounds(473, 85, 198, 26);  
				panel.add(txtCpf);
				
				//Adiciona a mascára de CPF
				AplicarMascaras.aplicar(txtCpf, "CPF");
				
				
			    txtCelular = new JFormattedTextField();
			    txtCelular.setFont(new Font("Perpetua", Font.PLAIN, 18)); 
			    txtCelular.setBounds(473, 135, 198, 26);
				panel.add(txtCelular);
				
				//Adiciona a mascára de Celular
				AplicarMascaras.aplicar(txtCelular, "CELULAR");
				
					
				//Mais itens que estão na tela
				JLabel lblEstadoAluno = new JLabel("Estado:");
				lblEstadoAluno.setFont(new Font("Segoe UI", Font.PLAIN, 16));  
				lblEstadoAluno.setBounds(70, 285, 72, 25); 
				panel.add(lblEstadoAluno);
				
			    cbCidade = new JComboBox();
			    cbCidade.setFont(new Font("Perpetua", Font.PLAIN, 15));  
			    cbCidade.setBounds(395, 285, 276, 26); 
				panel.add(cbCidade);
					
			    cbEstado = new JComboBox();
			    cbEstado.setFont(new Font("Perpetua", Font.PLAIN, 18));
			    
			    //Adicionei os estados manualmente na ComboBox para evitar chamar a API
				cbEstado.setModel(new DefaultComboBoxModel(new String[] {"Selecione uma opção:", "Acre", "Alagoas", "Amapá", "Amazonas", "Bahia", "Ceará", "Distrito Federal", "Espírito Santo", "Goiás", "Maranhão", "Mato Grosso do Sul", "Mato Grosso", "Minas Gerais", "Pará", "Paraíba", "Paraná", "Pernambuco", "Piauí", "Rio de Janeiro", "Rio Grande do Norte", "Rio Grande do Sul", "Rondônia", "Roraima", "Santa Catarina", "São Paulo", "Sergipe", "Tocantins"}));
				cbEstado.addItemListener(new ItemListener() {
					
					
					//Quando o item for selecionado
					public void itemStateChanged(ItemEvent e) {
						
						
						//Classe que tem os métodos para buscar na API
						BuscaPorAPI buscaApi = new BuscaPorAPI();
						
						if (e.getStateChange() == ItemEvent.SELECTED) {
							
							//Pega o estado selecionado pelo usuário
				            String estadoSelecionado = e.getItem().toString();
				            
				            //Se for diferente de "Selecione uma Opção:", faz a busca
				            if (!estadoSelecionado.equals("Selecione uma Opção:")) {
				                try  {
				                	
				                	//Define a Sigla
				                	txtUF.setText(buscaApi.buscarSiglaPorEstado(estadoSelecionado));
				                	
				                	cbCidade.removeAllItems(); // Limpa as cidades anteriores
				                	
				                	//Define as cidades com base na sigla
				                	cbCidade.setModel(new DefaultComboBoxModel(buscaApi.buscarCidadesPorSigla(txtUF.getText()).toArray()));
				                }
				                catch (Exception ex) {
				                	
				                    ex.printStackTrace();		                	
				                }
				            }
						}
					}
				});
				
				
				//Mais itens que estão na tela 
				cbEstado.setBounds(138, 285, 184, 26);
				panel.add(cbEstado);
					
				txtUF = new JTextField();
				txtUF.setFont(new Font("Perpetua", Font.PLAIN, 18)); 
				txtUF.setEditable(false); //Proibe o usuário de digitar
				txtUF.setBounds(457, 235, 214, 26);
				panel.add(txtUF);		
				txtUF.setColumns(10);
					
				
					
				JLabel lblCepAluno = new JLabel("CEP:");
				lblCepAluno.setFont(new Font("Segoe UI", Font.PLAIN, 20)); 
				lblCepAluno.setBounds(70, 185, 72, 25);  
				panel.add(lblCepAluno);
				
				txtCep = new JFormattedTextField();
				txtCep.setFont(new Font("Perpetua", Font.PLAIN, 18)); 
				txtCep.setBounds(126, 185, 276, 26);  
				panel.add(txtCep);
				
				//Adiciona a mascára de CEP
				AplicarMascaras.aplicar(txtCep, "CEP");
				
				
				//Botão de BuscarCEP	
				JButton btnBuscarCep = new JButton("Buscar");
				btnBuscarCep.setFont(new Font("Segoe UI", Font.PLAIN, 11));
				
				btnBuscarCep.addActionListener(new ActionListener() {
						
					//Quando Clicado
					public void actionPerformed(ActionEvent e) {
						
						//Classe que tem os métodos para buscar na API
						BuscaPorAPI buscaApi = new BuscaPorAPI();
						
						//Classe para representar o Endereço na  busca por API
						Endereco endereco = new Endereco();
						
						try {
							//Pega o CEP digitado
							String cepDigitado = txtCep.getText().replace("-", "").trim();
							
							if (!validarCampos.cep(txtCep.getText())) return;
							
							//Busca o endereço com base no CEP
							endereco = buscaApi.buscarEnderecoPorCEP(cepDigitado);
							
							//Caso não encontre o CEP
							if (endereco == null) {
								JOptionPane.showMessageDialog(null, "CEP não encontrado. Verifique e tente novamente.");
								return;
							}
							
							//Popular os campos com o endereço retornado
							txtEndereco.setText(endereco.getLogradouro() + " - " + endereco.getBairro());
							txtEndereco.enable(false); //Proibe o usuário de alterar o endereço após buscar pelo CEP
							txtUF.setText(endereco.getUf());
							cbEstado.setSelectedItem(endereco.getEstado());
							cbEstado.enable(false); //Proibe o usuário de alterar o estado após buscar pelo CEP
							cbCidade.setSelectedItem(endereco.getCidade());
							cbCidade.enable(false);		
						} 
						catch (Exception ex) {
							JOptionPane.showMessageDialog(null, "Erro ao buscar o CEP: " + ex.getMessage());
						}				
					}				
				});
					
					
				btnBuscarCep.setBounds(418, 187, 84, 25);
				panel.add(btnBuscarCep);
					
				JLabel lblNumeroAluno = new JLabel("Número:");
				lblNumeroAluno.setFont(new Font("Segoe UI", Font.PLAIN, 15)); 
				lblNumeroAluno.setBounds(511, 185, 72, 25);  
				panel.add(lblNumeroAluno);
					
				txtNumero = new JTextField();
				txtNumero.setFont(new Font("Perpetua", Font.PLAIN, 20)); 
				txtNumero.setColumns(10);  
				txtNumero.setBounds(580, 185, 91, 26);
				panel.add(txtNumero);
				
				btnSalvarAluno = new JButton("Salvar");
				btnSalvarAluno.setFont(new Font("Segoe UI", Font.PLAIN, 18)); 
				btnSalvarAluno.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//Salvar
					    aluno = new Aluno();
					    
					    //Valida se tem algum campo vazio
					    if (!validarCampos.campoVazio(txtRA, "RA")) return;
					    if (!validarCampos.campoVazio(txtNome, "Nome")) return;
					    if (!validarCampos.campoVazio(txtCelular, "Celular")) return;
					    if (!validarCampos.campoVazio(txtCpf, "CPF")) return;
					    if (!validarCampos.campoVazio(txtEmail, "Email")) return;
					    if (!validarCampos.campoVazio(txtEndereco, "Endereço")) return;
					    if (!validarCampos.campoVazio(txtNumero, "Número")) return;
					    if (!validarCampos.campoVazio(txtCep, "CEP")) return;
					    if (!validarCampos.campoVazio(txtDataNasc, "Data Nascimento")) return;
					    if (!validarCampos.campoVazio(txtUF, "UF")) return;
					    if (cbCidade.getSelectedIndex() == -1) { JOptionPane.showMessageDialog(null, "Selecione a cidade."); return; }
					    if (cbEstado.getSelectedIndex() == -1) { JOptionPane.showMessageDialog(null, "Selecione o estado."); return; }

					    		
					    	
					    
					    if (!validarCampos.celular(txtCelular.getText())) return;
					    if (!validarCampos.data(txtDataNasc.getText())) return;
					    if (!validarCampos.cpf(txtCpf.getText())) return;
					    if (!validarCampos.nome(txtNome.getText())) return;
						
									
						//Popular variavel aluno
						aluno.setRa(Integer.parseInt(txtRA.getText()));
						aluno.setNome(txtNome.getText());
						aluno.setCelular(txtCelular.getText());
						aluno.setDataNascimento(txtDataNasc.getText());
						aluno.setCpf(txtCpf.getText());
						aluno.setEmail(txtEmail.getText());
						aluno.setEndereco(txtEndereco.getText());
						aluno.setNumero(Integer.parseInt(txtNumero.getText()));
						aluno.setCep(txtCep.getText());
						aluno.setCidade(cbCidade.getSelectedItem().toString());
						aluno.setEstado(cbEstado.getSelectedItem().toString());
						aluno.setUf(txtUF.getText());
						aluno.setNumero(Integer.parseInt(txtNumero.getText()));
							
						//Chama o DAO para salvar 
						try {
							
							//Chamar alunoDAO --> Abre o BD
							//dao = new AlunoDAO();
							
							//dao.salvar(aluno);
							
							JOptionPane.showMessageDialog(null, "Dados Pessoais Salvos! Agora Escolha o Curso: ");
							
							// ir para aba Curso
							tbAluno.setEnabledAt(2, true); // habilita a aba curso
							tbAluno.setSelectedIndex(2); // índice 2 = terceira aba - curso
							
							tbAluno.setEnabledAt(1, false); // Desabilita a aba dados pessoais, só sendo possível voltar pelo botão
							
						}
						catch (Exception erro) {
							
							JOptionPane.showMessageDialog(null,"Aconteceu erro: " + erro.getMessage());
						}	
					}
				});
				btnSalvarAluno.setBounds(145, 340, 102, 38);
				panel.add(btnSalvarAluno);
				
				JButton btnLimparAluno = new JButton("Limpar");
				btnLimparAluno.setFont(new Font("Segoe UI", Font.PLAIN, 18)); 
				btnLimparAluno.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// Limpar campos
						txtRA.setText("");
						txtNome.setText("");
						txtCelular.setText("");
						txtDataNasc.setText("");
						txtCpf.setText("");
						txtEmail.setText("");
						txtEndereco.setText("");
						txtNumero.setText("");
						txtCep.setText("");
						cbCidade.setSelectedIndex(-1);
						cbEstado.setSelectedIndex(-1);
						txtUF.setText("");
					}
				});
				btnLimparAluno.setBounds(305, 340, 102, 38);
				panel.add(btnLimparAluno);
				
				JButton btnVoltar = new JButton("Voltar");
				btnVoltar.setFont(new Font("Segoe UI", Font.PLAIN, 18)); 
				btnVoltar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
					tbAluno.setEnabledAt(0, true); // Habilita novamente a aba "Visualizar"
					tbAluno.setSelectedIndex(0); // Vai para a aba de Índice 0 = primeira aba - visualizar
					
					tbAluno.setEnabledAt(1, false); // Bloqueia a aba de dados pessoais novamente, até que o botão de adicionar aluno seja pressionado
					
					// Limpando campos quando voltar
					txtRA.setText("");
					txtNome.setText("");
					txtCelular.setText("");
					txtDataNasc.setText("");
					txtCpf.setText("");
					txtEmail.setText("");
					txtEndereco.setText("");
					txtNumero.setText("");
					txtCep.setText("");
					cbCidade.setSelectedIndex(-1);
					cbEstado.setSelectedIndex(-1);
					txtUF.setText("");
					}
				});
				btnVoltar.setBounds(465, 340, 102, 38);
				panel.add(btnVoltar);
					
				JPanel panel_1 = new JPanel();
				tbAluno.addTab("Curso", null, panel_1, null);
				panel_1.setLayout(null);
				
				tbAluno.setEnabledAt(2, false);    // Desabilita a aba "Curso" no início
				
				JLabel lblNewLabel_1 = new JLabel("Curso:");
				lblNewLabel_1.setFont(new Font("Segoe UI", Font.PLAIN, 13));
				lblNewLabel_1.setBounds(97, 55, 44, 25);
				panel_1.add(lblNewLabel_1);
				
				JLabel lblNewLabel_2 = new JLabel("Campus:");
				lblNewLabel_2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
				lblNewLabel_2.setBounds(97, 20, 72, 25);
				panel_1.add(lblNewLabel_2);
				
				JComboBox cbSemestreCursos = new JComboBox();
				cbSemestreCursos.setBounds(159, 91, 379, 23);
				panel_1.add(cbSemestreCursos);
				
				// Método que filtra as disciplinas por semestre
				cbSemestreCursos.addItemListener(new ItemListener() {
				    @Override
				    public void itemStateChanged(ItemEvent e) {
				    	if (e.getStateChange() != ItemEvent.SELECTED) return; if (cbCurso.getSelectedIndex() <= 0) return; // ensure a course is selected

				        int selectedIndex = cbSemestreCursos.getSelectedIndex();
				        if (selectedIndex == 0) {
				            // "Todos" selected — load all disciplines for the course
				            carregarTabelaDisciplinasDoCurso(idCursoSelecionado);
				            return;
				        }

				        // parse semester number (handles strings like "1" or "1ºSemestre")
				        String semestreStr = cbSemestreCursos.getSelectedItem().toString();
				        try {
				        	int semestre = Integer.parseInt(semestreStr.replaceAll("\\D+", ""));
				            carregarTabelaDisciplinasDoCursoESemestre(idCursoSelecionado, semestre);
				        } catch (NumberFormatException ex) {
				            System.out.println("Erro ao converter semestre: " + ex.getMessage());
				        }
				    }
				});

				
				JList list = new JList();
				list.setBounds(138, 59, 137, 0);
				panel_1.add(list);
				
				cbCurso = new JComboBox();
				cbCurso.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED && cbCurso.getSelectedIndex() > 0) {

							// Pega o curso selecionado
				            Curso curso = (Curso) cbCurso.getSelectedItem();
				            idCursoSelecionado = curso.getId_curso(); // salva o ID

				            // Limpa o combo de semestre antes de preencher
				            cbSemestreCursos.removeAllItems();

				            // Preenche com base na duração do curso
				            int totalSemestres = curso.getDuracao_semestre();
				            cbSemestreCursos.addItem("Todos");

				            for (int i = 1; i <= totalSemestres; i++) {
				                cbSemestreCursos.addItem(String.valueOf(i));
				            }
				            cbSemestreCursos.setSelectedIndex(0); // default to "Todos"

				            // Atualiza as disciplinas também
				            carregarTabelaDisciplinasDoCurso(idCursoSelecionado);
				        }				
					}
				});
				cbCurso.setBounds(159, 56, 429, 23);
				panel_1.add(cbCurso);
				
				cbCampus = new JComboBox();
				cbCampus.addItemListener(new ItemListener() {
					
					//Evento ao selecionar item 
					public void itemStateChanged(ItemEvent e) {
						
						if (e.getStateChange() == ItemEvent.SELECTED) {
							
							// Se o usuário escolher "Selecione uma opção", limpamos o cursos
				            if (cbCampus.getSelectedIndex() == 0) {
				                cbCurso.removeAllItems();
				                cbCurso.addItem("Selecione uma opção");
				                cbCurso.setSelectedIndex(0);
				                return;
				            }
				            
				            Campus campusSelecionado = (Campus) cbCampus.getSelectedItem();
				            if (campusSelecionado != null) {
				            	carregarCB.carregarCursos(cbCurso, campusSelecionado.getId(), false); // false -> "Selecione uma opção"
				            }
				        }

					}
				});
				cbCampus.setBounds(159, 21, 479, 23);
				panel_1.add(cbCampus);
				
				// Carrega os campus ao abrir a tela
				carregarCB.carregarCampus(cbCampus, false); //Falso é para adicionar "Selecionar uma Opção:
				
				//Criação da Tabela das disciplinas
				
				// Definir colunas
				String[] colunas = { "Selecionar", "ID", "Disciplina", "Semestre", "ID"};

				// Criar componente com a factory
				TabelaFactory.TabelaComponent tabelaDisciplinasComp = criarTabela.criarTabela(colunas, 21, 125, 690, 170);

				// Adicionar no painel
				panel_1.add(tabelaDisciplinasComp.scrollPane);

				// Guardar referências
				tableDisciplinas = tabelaDisciplinasComp.tabela;
				modeloDisciplinas = tabelaDisciplinasComp.modelo;
				TableRowSorter<DefaultTableModel> sorterDisciplinas = tabelaDisciplinasComp.sorter;
				
				//Para as checkBox 
				tableDisciplinas.getColumnModel().getColumn(0).setCellEditor(new javax.swing.DefaultCellEditor(new javax.swing.JCheckBox()));
				tableDisciplinas.getColumnModel().getColumn(0).setCellRenderer(tableDisciplinas.getDefaultRenderer(Boolean.class));
				// Ocultar coluna 4 (ID curso_disciplina)
				tableDisciplinas.getColumnModel().getColumn(4).setMinWidth(0);
				tableDisciplinas.getColumnModel().getColumn(4).setMaxWidth(0);
				tableDisciplinas.getColumnModel().getColumn(4).setWidth(0);
				tableDisciplinas.getColumnModel().getColumn(4).setPreferredWidth(0);
				
				//Botão Salvar da Aba Cursos
				btnSalvar = new JButton("Salvar");
				btnSalvar.setFont(new Font("Segoe UI", Font.PLAIN, 18)); 
				btnSalvar.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
					    if (cbCampus.getSelectedIndex() == 0) {
					        JOptionPane.showMessageDialog(null, "Selecione um campus.");
					        return;
					    }
					    if (cbCurso.getSelectedIndex() == 0) {
					        JOptionPane.showMessageDialog(null, "Selecione um curso.");
					        return;
					    }

					    Curso cursoSelecionado = (Curso) cbCurso.getSelectedItem();
					    aluno.setId_curso(cursoSelecionado.getId_curso());

					    try {
					        dao = new AlunoDAO();
					        dao.salvar(aluno);

					        AlunoDisciplinaDAO adDAO = new AlunoDisciplinaDAO();
					        AlunoNotaFaltaDAO nfDAO = new AlunoNotaFaltaDAO();

					        int linhas = modeloDisciplinas.getRowCount();

					        for (int i = 0; i < linhas; i++) {
					            Boolean selecionado = (Boolean) modeloDisciplinas.getValueAt(i, 0);
					            if (Boolean.TRUE.equals(selecionado)) {
					                int idCursoDisciplina = Integer.parseInt(modeloDisciplinas.getValueAt(i, 4).toString());
					                int idDisciplina = Integer.parseInt(modeloDisciplinas.getValueAt(i, 1).toString());

					                adDAO.salvar(aluno.getRa(), idCursoDisciplina);

					                // Preferido: buscar por aluno_ra + id_curso_disciplina
					                Integer idAlunoDisciplina = adDAO.buscarIdPorAlunoECursoDisciplina(aluno.getRa(), idCursoDisciplina);
					                if (idAlunoDisciplina == null) {
					                    // fallback: tentar buscar por disciplina id
					                    idAlunoDisciplina = adDAO.buscarIdAlunoDisciplina(aluno.getRa(), idDisciplina);
					                }

					                if (idAlunoDisciplina != null) {
					                    nfDAO.salvarNotaInicial(idAlunoDisciplina);
					                } else {
					                    System.err.println("Atenção: Não foi possível obter o ID da relação Aluno-Disciplina após a inserção para o CursoDisciplina: " + idCursoDisciplina);
					                }
					            }
					        }

					        JOptionPane.showMessageDialog(null, "Aluno e disciplinas cadastrados com sucesso!");
					        carregarTabelaAlunos();
					        tbAluno.setEnabledAt(0, true);
					        tbAluno.setSelectedIndex(0);
					        tbAluno.setEnabledAt(2, false);

					    } catch (Exception erro) {
					        JOptionPane.showMessageDialog(null, "Erro ao salvar aluno: " + erro.getMessage());
					        erro.printStackTrace();
					    }
					}

				});
				btnSalvar.setBounds(173, 320, 137, 60);
				panel_1.add(btnSalvar);
				
				JButton btnVoltar_1 = new JButton("Voltar");
				btnVoltar_1.setFont(new Font("Segoe UI", Font.PLAIN, 18)); 
				btnVoltar_1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						tbAluno.setEnabledAt(1, true); // Habilita novamente a aba "Dados Pessoais"
						tbAluno.setSelectedIndex(1); // Vai para a aba de Índice 1 = segunda aba - dados pessoais
						
						tbAluno.setEnabledAt(2, false); // Bloqueia a aba de curso novamente, até que os dados pessoais sejam preenchidos e salvos
						
						// Limpando campos após voltar
						cbCampus.setSelectedIndex(-1);
						cbCurso.setSelectedIndex(-1);
						cbSemestreCursos.setSelectedIndex(-1);
					}
				});
				btnVoltar_1.setBounds(410, 320, 130, 60);
				panel_1.add(btnVoltar_1);
				
				btnAlterar = new JButton("Alterar");
				btnAlterar.setBounds(173, 320, 137, 60);
				panel_1.add(btnAlterar);
				btnAlterar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					    if (cbCampus.getSelectedIndex() == 0) {
					        JOptionPane.showMessageDialog(null, "Selecione um campus.");
					        return;
					    }
					    if (cbCurso.getSelectedIndex() == 0) {
					        JOptionPane.showMessageDialog(null, "Selecione um curso.");
					        return;
					    }

					    Curso cursoSelecionado = (Curso) cbCurso.getSelectedItem();
					    aluno.setId_curso(cursoSelecionado.getId_curso());

					    try {
					        AlunoDAO alunoDAO = new AlunoDAO();
					        alunoDAO.alterar(aluno);

					        AlunoDisciplinaDAO adDAO = new AlunoDisciplinaDAO();
					        AlunoNotaFaltaDAO nfDAO = new AlunoNotaFaltaDAO();

					        // Delete dependent records in correct order
					        nfDAO.excluirPorAluno(aluno.getRa());    // delete notes first
					        adDAO.excluirPorRaAluno(aluno.getRa());  // then delete aluno_disciplina rows

					        // Insert new relations and create initial notes
					        int linhas = modeloDisciplinas.getRowCount();
					        for (int i = 0; i < linhas; i++) {
					            Boolean selecionado = (Boolean) modeloDisciplinas.getValueAt(i, 0);
					            if (Boolean.TRUE.equals(selecionado)) {
					                int idCursoDisciplina = Integer.parseInt(modeloDisciplinas.getValueAt(i, 4).toString());

					                Integer idAlunoDisciplina = adDAO.alterarAlunoDisciplina(aluno.getRa(), idCursoDisciplina);

					                if (idAlunoDisciplina != null) {
					                    nfDAO.salvarNotaInicial(idAlunoDisciplina);
					                } else {
					                    System.err.println("Atenção: Não foi possível obter o ID da relação Aluno-Disciplina após a inserção para o CursoDisciplina: " + idCursoDisciplina);
					                }
					            }
					        }

					        JOptionPane.showMessageDialog(null, "Aluno e disciplinas alteradas com sucesso!");
					        carregarTabelaAlunos();
					        tbAluno.setEnabledAt(0, true);
					        tbAluno.setSelectedIndex(0);
					        tbAluno.setEnabledAt(2, false);

					    } catch (Exception ex) {
					        JOptionPane.showMessageDialog(null, "Erro ao alterar aluno: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
					        ex.printStackTrace();
					    }
					}

				});

				
				JLabel lblNewLabel_9 = new JLabel("Semestre:");
				lblNewLabel_9.setFont(new Font("Segoe UI", Font.PLAIN, 12));
				lblNewLabel_9.setBounds(97, 90, 57, 25);
				panel_1.add(lblNewLabel_9);
				
				JPanel panel_2 = new JPanel();
				tbAluno.addTab("Notas e Faltas", null, panel_2, null);
				panel_2.setLayout(null);
				
				tbAluno.setEnabledAt(3, false);    // Desabilita a aba "Notas e Faltas" no início
				
				JLabel lblNewLabel_4 = new JLabel("RA:");
				lblNewLabel_4.setFont(new Font("Segoe UI", Font.PLAIN, 18));
				lblNewLabel_4.setBounds(481, 57, 46, 25);
				panel_2.add(lblNewLabel_4);
				
				txtRANotas = new JTextField();
				txtRANotas.setEnabled(false);
				txtRANotas.setBounds(525, 57, 120, 26);
				panel_2.add(txtRANotas);
				txtRANotas.setColumns(10);
				
				JLabel lblNewLabel_5_1 = new JLabel("Nota 1:");
				lblNewLabel_5_1.setBounds(82, 233, 46, 14);
				panel_2.add(lblNewLabel_5_1);
				
				JLabel lblNewLabel_5_2 = new JLabel("Faltas:");
				lblNewLabel_5_2.setFont(new Font("Segoe UI", Font.PLAIN, 15));
				lblNewLabel_5_2.setBounds(531, 192, 46, 25);
				panel_2.add(lblNewLabel_5_2);
				
				txtFaltas = new JTextField();
				txtFaltas.setColumns(10);
				txtFaltas.setBounds(587, 192, 58, 26);
				panel_2.add(txtFaltas);
				
				JLabel lblNewLabel_5_3 = new JLabel("Disciplina");
				lblNewLabel_5_3.setBounds(82, 192, 46, 25);
				panel_2.add(lblNewLabel_5_3);
				
				cbDisciplinasNotas = new JComboBox();
				cbDisciplinasNotas.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						
						if (e.getStateChange() == ItemEvent.SELECTED && cbDisciplinasNotas.getSelectedIndex() >= 0) {
							
							 if (cbDisciplinasNotas.getSelectedIndex() == 0) {
					                txtNota1.setText("");
					                txtNota2.setText("");
					                txtFaltas.setText("");
					                return;
					            }

				            try {
				                CursoDisciplina disc = (CursoDisciplina) cbDisciplinasNotas.getSelectedItem();

				                // Buscar ID aluno_disciplina
				                AlunoDisciplinaDAO adDAO = new AlunoDisciplinaDAO();
				                int ra = Integer.parseInt(txtRANotas.getText());
				                Integer idAlunoDisciplina = adDAO.buscarIdAlunoDisciplina(ra, disc.getDisciplina().getId());

				                if (idAlunoDisciplina == null) {
				                    txtNota1.setText("");
				                    txtNota2.setText("");
				                    txtFaltas.setText("");
				                    return;
				                }

				                // Buscar notas e faltas
				                AlunoNotaFaltaDAO nfDAO = new AlunoNotaFaltaDAO();
				                
				                AlunoNotaFalta dados = nfDAO.buscarNotasFaltas(idAlunoDisciplina);

				                	txtNota1.setText(String.valueOf(dados.getNota1()));
				                	txtNota2.setText(String.valueOf(dados.getNota2()));
				                	txtFaltas.setText(String.valueOf(dados.getFaltas()));
				               

				            } catch (Exception ex) {
				                JOptionPane.showMessageDialog(null, "Erro ao carregar notas: " + ex.getMessage());
				            }
				        }
				    }
				});
				cbDisciplinasNotas.setBounds(153, 192, 328, 26);
				panel_2.add(cbDisciplinasNotas);
				
				txtNomeNotas = new JTextField();
				txtNomeNotas.setEditable(false);
				txtNomeNotas.setBounds(153, 57, 252, 26);
				panel_2.add(txtNomeNotas);
				txtNomeNotas.setColumns(10);
				
				JButton btnAlterarNF = new JButton("Alterar Notas e Faltas");
				btnAlterarNF.setFont(new Font("Segoe UI", Font.PLAIN, 18)); 
				btnAlterarNF.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						try {
				            if (cbDisciplinasNotas.getSelectedIndex() <= 0) {
				                JOptionPane.showMessageDialog(null, "Selecione uma disciplina!");
				                return;
				            }

				            int ra = Integer.parseInt(txtRANotas.getText());
				            CursoDisciplina disc = (CursoDisciplina) cbDisciplinasNotas.getSelectedItem();

				            // Buscar id aluno_disciplina
				            AlunoDisciplinaDAO adDAO = new AlunoDisciplinaDAO();
				            Integer idAlunoDisciplina = adDAO.buscarIdAlunoDisciplina(ra, disc.getDisciplina().getId());

				            if (idAlunoDisciplina == null) {
				                JOptionPane.showMessageDialog(null, "Erro: relação aluno-disciplina não encontrada!");
				                return;
				            }

				            // Capturar valores
				            Double nota1 = txtNota1.getText().isEmpty() ? null : Double.parseDouble(txtNota1.getText());
				            Double nota2 = txtNota2.getText().isEmpty() ? null : Double.parseDouble(txtNota2.getText());
				            Integer faltas = txtFaltas.getText().isEmpty() ? 0 : Integer.parseInt(txtFaltas.getText());

				            // Salvar
				            AlunoNotaFaltaDAO nfDAO = new AlunoNotaFaltaDAO();
				            nfDAO.atualizarNotasFaltas(idAlunoDisciplina, nota1, nota2, faltas);
				            
				            // Após salvar, habilita novamente a aba "Visualizar" e retorna a ela
				            tbAluno.setEnabledAt(0, true); 
							tbAluno.setSelectedIndex(0); 
							
							tbAluno.setEnabledAt(3, false); // Bloqueia a aba de "Notas e Faltas" novamente	

				            JOptionPane.showMessageDialog(null, "Notas e faltas atualizadas com sucesso!");

				        } catch (Exception ex) {
				            JOptionPane.showMessageDialog(null, "Erro ao salvar: " + ex.getMessage());
				        }
					}
				});
				btnAlterarNF.setBounds(80, 300, 255, 60);
				panel_2.add(btnAlterarNF);
				
				JButton btnVoltarNotaseFaltas = new JButton("Voltar");
				btnVoltarNotaseFaltas.setFont(new Font("Segoe UI", Font.PLAIN, 18)); 
				btnVoltarNotaseFaltas.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						tbAluno.setEnabledAt(0, true); // Habilita novamente a aba "Visualizar"
						tbAluno.setSelectedIndex(0); // Vai para a aba de Índice 0 = primeira aba - visualizar
						
						tbAluno.setEnabledAt(3, false); // Bloqueia a aba de "Notas e Faltas" novamente	
						
						
					}
				});
				btnVoltarNotaseFaltas.setBounds(390, 300, 255, 60);
				panel_2.add(btnVoltarNotaseFaltas);
				
				JLabel lblNewLabel_10 = new JLabel("Nome:");
				lblNewLabel_10.setFont(new Font("Segoe UI", Font.PLAIN, 20));  
				lblNewLabel_10.setBounds(82, 57, 72, 25);  
				panel_2.add(lblNewLabel_10);
				
				JLabel lblNewLabel_11 = new JLabel("Campus:");
				lblNewLabel_11.setFont(new Font("Segoe Ui", Font.PLAIN, 14));  
				lblNewLabel_11.setBounds(82, 102, 118, 25);  
				panel_2.add(lblNewLabel_11);
				
				txtNomeCampusNotas = new JTextField();
				txtNomeCampusNotas.setEnabled(false);
				txtNomeCampusNotas.setFont(new Font("Perpetua", Font.PLAIN, 18));  
				txtNomeCampusNotas.setBounds(153, 102, 276, 26);
				panel_2.add(txtNomeCampusNotas);
				txtNomeCampusNotas.setColumns(10);
				
				txtNota1 = new JTextField();
				txtNota1.setBounds(215, 246, 96, 26);
				panel_2.add(txtNota1);
				txtNota1.setColumns(10);
				
				JLabel lblNewLabel_5 = new JLabel("Nota 2:");
				lblNewLabel_5.setBounds(82, 272, 44, 12);
				panel_2.add(lblNewLabel_5);
				
				txtNota2 = new JTextField();
				txtNota2.setBounds(402, 246, 96, 26);
				panel_2.add(txtNota2);
				txtNota2.setColumns(10);
				
				JLabel lblNewLabel_12 = new JLabel("Curso:");
				lblNewLabel_12.setFont(new Font("Segoe UI", Font.PLAIN, 16));  
				lblNewLabel_12.setBounds(82, 147, 72, 25);
				panel_2.add(lblNewLabel_12);
				
				txtNomeCursoNotas = new JTextField();
				txtNomeCursoNotas.setEnabled(false);
				txtNomeCursoNotas.setFont(new Font("Perpetua", Font.PLAIN, 18)); 
				txtNomeCursoNotas.setBounds(153, 147, 184, 26);
				panel_2.add(txtNomeCursoNotas);
				txtNomeCursoNotas.setColumns(10);
				
				JPanel panel_3 = new JPanel();
				tbAluno.addTab("Boletim", null, panel_3, null);
				panel_3.setLayout(null);
				
				// JEditorPane para pré-visualização do boletim
				editorBoletim = new JEditorPane();
				editorBoletim.setContentType("text/html");
				editorBoletim.setEditable(false);

				// Scroll e adicionar o editor dentro dele
				JScrollPane scrollBoletim = new JScrollPane(editorBoletim);
				scrollBoletim.setBounds(83, 10, 514, 294);
				panel_3.add(scrollBoletim);
				
				JButton btnBaixarBoletim = new JButton("Baixar Boletim");
				btnBaixarBoletim.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						
						
						try { 
						    int linha = tableAlunosVisualizar.getSelectedRow(); 
						    if (linha == -1) { 
						        JOptionPane.showMessageDialog(null, "Volte e selecione um aluno primeiro!"); 
						        return; 
						    } 
						    
						    int raAluno = Integer.parseInt(tableAlunosVisualizar.getValueAt(linha, 0).toString()); 

						    // Caminho da pasta Downloads
						    String downloads = System.getProperty("user.home") + "\\Downloads\\";

						    java.awt.Window parent = SwingUtilities.getWindowAncestor(TelaAlunos.this);
						    java.awt.FileDialog dialog = new java.awt.FileDialog((java.awt.Frame) parent, "Salvar Boletim", java.awt.FileDialog.SAVE);

						    dialog.setDirectory(downloads); // <<< AQUI definimos a pasta padrão
						    dialog.setFile("Boletim_RA_" + raAluno + ".pdf");
						    dialog.setVisible(true);

						    String dir = dialog.getDirectory();
						    String file = dialog.getFile();

						    if (file != null) { 
						        String caminho = dir + file; 
						        
						        if (!caminho.toLowerCase().endsWith(".pdf")) { 
						            caminho += ".pdf"; 
						        } 
						        
						        BoletimService boletimService = new BoletimService(); 
						        boletimService.gerarBoletimPDF(raAluno, caminho); 
						        
						        JOptionPane.showMessageDialog(null, "PDF gerado com sucesso!"); 
						        
						        try { 
						            java.awt.Desktop.getDesktop().open(new java.io.File(caminho)); 
						        } catch (Exception ex) { 
						            ex.printStackTrace(); 
						        } 
						    } 
						} catch (Exception ex) { 
						    ex.printStackTrace(); 
						    JOptionPane.showMessageDialog(null, "Erro ao salvar PDF: " + ex.getMessage()); 
						}
					}
				});
				btnBaixarBoletim.setBounds(242, 330, 120, 20);
				panel_3.add(btnBaixarBoletim);
				
				JButton btnVoltarBoletim = new JButton("Voltar");
				btnVoltarBoletim.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						tbAluno.setEnabledAt(0, true); // Habilita novamente a aba "Visualizar"
						tbAluno.setSelectedIndex(0); // Vai para a aba de Índice 0 = primeira aba - visualizar
						
						tbAluno.setEnabledAt(4, false); // Bloqueia a aba de boletim novamente	
					}
				});
				btnVoltarBoletim.setBounds(411, 329, 89, 23);
				panel_3.add(btnVoltarBoletim);

				
				tbAluno.setEnabledAt(4, false);

			}		
			
			// Construtor que permite trocar de telas
			public TelaAlunos(int abaInicial) {
				this(); // chama o construtor padrão
				tbAluno.setSelectedIndex(abaInicial);
			}			
			
			//Método para carregar a tabela de Campus
			private void carregarTabelaAlunos() {
			    try {
			        dao = new AlunoDAO();
			        ResultSet rs = dao.listarTodos(); // ← vamos criar esse método

			        modeloTabelaAlunosVisualizar.setRowCount(0);

			        while (rs.next()) {
			            modeloTabelaAlunosVisualizar.addRow(new Object[] {
			                rs.getInt("RA"),
			                rs.getString("Nome"),
			                rs.getString("email"),
			                rs.getString("celular"), // era celular
			                rs.getString("cpf"),
			                rs.getString("campus"),   // ✅ direto do SELECT
			                rs.getString("curso")     // ✅ direto do SELECT
			            });
			        }

			        rs.close();
			    } catch (Exception e) {
			        JOptionPane.showMessageDialog(null, "Erro ao carregar tabela: " + e.getMessage());
			    }
			}
			
			//Método para unir filtros 
			private void aplicarFiltros() {
				
			    List<RowFilter<Object,Object>> filtros = new ArrayList<>();

			    // Filtro por período
			    String campus = cbCampusVisualizarAlunos.getSelectedItem().toString();
			    if (!campus.equals("Todos")) {
			        filtros.add(RowFilter.regexFilter("^" + campus + "$", 5)); // coluna 5 = Campus
			    }
			    
			    // Filtro por Curso
			    String curso = cbCursosVisualizarAlunos.getSelectedItem() != null 
			                   ? cbCursosVisualizarAlunos.getSelectedItem().toString() 
			                   : "Todos";

			    if (!curso.equals("Todos")) {
			        filtros.add(RowFilter.regexFilter("^" + curso + "$", 6)); // coluna 6 = Curso
			    }
			    
			    // Filtro por nome do curso
			    String busca = txtBuscarNomeAluno.getText().trim();
			    if (!busca.isEmpty()) {
			        filtros.add(RowFilter.regexFilter("(?i)" + busca, 1 )); // coluna 1 = Nome do Aluno
			    }

			    // Junta todos os filtros (AND)
			    if (filtros.isEmpty()) {
			        sorter.setRowFilter(null);
			    } else {
			        sorter.setRowFilter(RowFilter.andFilter(filtros));
			    }
			}
			
			// Método que limpa os filtros
			private void limparFiltros() {
			    // Zera os campos
			    cbCampusVisualizarAlunos.setSelectedIndex(0); // “Todos” deve ser o primeiro item
			    cbCursosVisualizarAlunos.setSelectedIndex(0); // idem
			    txtBuscarNomeAluno.setText("");

			    // Remove o RowFilter
			    sorter.setRowFilter(null);
			}
			
			private void carregarTabelaDisciplinasDoCurso(int idCurso) {
			    try {
			    	 CursoDisciplinaDAO cdDAO = new CursoDisciplinaDAO();
			         List<CursoDisciplina> lista = cdDAO.listarDisciplinasPorCurso(idCurso);

			         modeloDisciplinas.setRowCount(0);

			         for (CursoDisciplina cd : lista) {
			        	 modeloDisciplinas.addRow(new Object[] {
			        		 false, // ✅ coluna Selecionar (checkbox)
			                 cd.getDisciplina().getId(),
			                 cd.getDisciplina().getNome(),
			                 cd.getSemestre() + "ºSemestre",
			                 cd.getId()
			             });
			        }
			    } catch (Exception e) {
			        JOptionPane.showMessageDialog(null, "Erro ao carregar disciplinas do curso: " + e.getMessage());
			    }
			}
			
			// Recarrega a tabela e lista por semestre
			private void carregarTabelaDisciplinasDoCursoESemestre(int idCurso, int semestre) {
						    try {
						        CursoDisciplinaDAO dao = new CursoDisciplinaDAO();
						        List<CursoDisciplina> lista = dao.listarDisciplinasPorCursoESemestre(idCurso, semestre);

						        DefaultTableModel modelo = (DefaultTableModel) tableDisciplinas.getModel();
						        modelo.setRowCount(0); // limpa a tabela

						        for (CursoDisciplina cd : lista) {
						            modelo.addRow(new Object[]{
						                false,
						                cd.getDisciplina().getId(),
						                cd.getDisciplina().getNome(),
						                cd.getSemestre() + "ºSemestre",
						                cd.getId()
						            });
						        }

						    } catch (Exception e) {
						        JOptionPane.showMessageDialog(null, "Erro ao carregar disciplinas: " + e.getMessage());
						    }
						}

	}			