package br.edu.fatecgru.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import br.edu.fatecgru.dao.CampusDAO;
import br.edu.fatecgru.model.Campus;
import br.edu.fatecgru.model.Endereco;
import br.edu.fatecgru.util.AplicarMascaras;
import br.edu.fatecgru.util.BuscaPorAPI;
import br.edu.fatecgru.util.TabelaFactory;
import br.edu.fatecgru.util.ValidarCampos;

public class TelaCampus extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable tableCampus;
	private JTextField txtNomeCampus;
	private JTextField txtEnderecoCampus;
	private JTextField txtUFCampus;
	private JTextField txtNumeroCampus;
	private JTextField txtIdCampus;
	private JFormattedTextField txtCepCampus;
	private JComboBox cbCidadeCampus;
	private JComboBox cbEstadoCampus;
	private DefaultTableModel modeloTabela;
			
	
	private Campus campus;
	private CampusDAO dao;
	
	private BuscaPorAPI buscaApi;
	
	private JTextField txtNomeCampusAlterar;
	private JTextField txtEnderecoCampusAlterar;
	private JTextField txtUFCampusAlterar;
	private JTextField txtNumeroCampusAlterar;
	private JFormattedTextField txtCepCampusAlterar;
	private JTextField txtIdCampusAlterar;
	private JComboBox cbCidadeCampusAlterar;
	private JComboBox cbEstadoCampusAlterar;
	

	/**
	 * Create the panel.
	 */
	public TelaCampus() {
		setLayout(null);
		
		//Variaveis utilitarias
		ValidarCampos validarCampos = new ValidarCampos();
		TabelaFactory criarTabela = new TabelaFactory();
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(44, 55, 739, 431);
		add(tabbedPane);
		
		
		JPanel pnVisualizar = new JPanel();
		tabbedPane.addTab("Visualizar", null, pnVisualizar, null);
		pnVisualizar.setLayout(null);
		
		//Criar a tabela
		String[] colunasCampus = { "ID", "Nome", "CEP", "Endereço", "Número", "Cidade", "Estado", "UF" };

		TabelaFactory.TabelaComponent tabelaCampusComp = criarTabela.criarTabela(colunasCampus, 20, 55, 690, 220);

		pnVisualizar.add(tabelaCampusComp.scrollPane);

		// Pegar referências
		tableCampus = tabelaCampusComp.tabela;
		modeloTabela = tabelaCampusComp.modelo;
		
		//Botão adicionar Campus
		JButton btnAdicionarCampus = new JButton("Adicionar");
		btnAdicionarCampus.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		btnAdicionarCampus.addActionListener(new ActionListener() {
			
			//Ao clicar no botão Adicionar
			public void actionPerformed(ActionEvent e) {
				
				tabbedPane.setEnabledAt(1, true);    // habilita a aba "Adicionar"
			    tabbedPane.setSelectedIndex(1);      // muda automaticamente para a aba "Adicionar"
			    
			    tabbedPane.setEnabledAt(0, false); // Desabilita a aba "Visualizar", para voltar apenas no botão
			    
			    limparCamposCampus();
			}
		});
		btnAdicionarCampus.setBounds(90, 315, 130, 60);
		pnVisualizar.add(btnAdicionarCampus);
		
		
		//Botão excluir Campus
		JButton btnExcluirCampus = new JButton("Excluir");
		btnExcluirCampus.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		btnExcluirCampus.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				// Excluir
				campus = new Campus();
				
				//Pegar o ID
				int linhaSelecionada = tableCampus.getSelectedRow();
				
				//Verifica se alguma linha foi selecionada
				if (linhaSelecionada == -1)
				{
					JOptionPane.showMessageDialog(null, "Selecione um campus para excluir.");
					return;
				}
				
				// Confirmação de exclusão
		        int confirmacao = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir este campus?", "Confirmação", JOptionPane.YES_NO_OPTION);

		        if (confirmacao != JOptionPane.YES_OPTION) return;; // Sai se o usuário não confirmar
				
				//Pega o ID da linha selecionada
		        int id = Integer.parseInt(tableCampus.getValueAt(linhaSelecionada, 0).toString());
				
				try {
					dao = new CampusDAO();
					campus = new Campus();
					campus.setId(id); // Define o ID do campus a ser excluído
					dao.excluir(campus);
					JOptionPane.showMessageDialog(null,"Excluído com sucesso!");
					
					//Atualiza a tabela de Campus
					carregarTabelaCampus();
					
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Erro ao excluir: " + e1.getMessage());
				}
			}
		});
		btnExcluirCampus.setBounds(490, 315, 130, 60);
		pnVisualizar.add(btnExcluirCampus);
		
		JPanel pnAdicionar = new JPanel();
		tabbedPane.addTab("Adicionar", null, pnAdicionar, null);
		pnAdicionar.setLayout(null);
		
		tabbedPane.setEnabledAt(1, false); // Desabilita a aba "Adicionar" inicialmente
		
		txtNomeCampus = new JTextField();
		txtNomeCampus.setFont(new Font("Perpetua", Font.PLAIN, 18)); 
		txtNomeCampus.setBounds(126, 55, 545, 26);
		pnAdicionar.add(txtNomeCampus);
		txtNomeCampus.setColumns(10);
		
		JLabel lblNomeCampus = new JLabel("Nome:");
		lblNomeCampus.setFont(new Font("Segoe UI", Font.PLAIN, 16));  
		lblNomeCampus.setBounds(70, 55, 51, 25);  
		pnAdicionar.add(lblNomeCampus);
		
		JLabel lblEnderecoCampus = new JLabel("Endereço:");
		lblEnderecoCampus.setFont(new Font("Segoe UI", Font.PLAIN, 16));  
		lblEnderecoCampus.setBounds(70, 210, 118, 25);
		pnAdicionar.add(lblEnderecoCampus);
		
		txtEnderecoCampus = new JTextField();
		txtEnderecoCampus.setFont(new Font("Perpetua", Font.PLAIN, 15));  
		txtEnderecoCampus.setColumns(10); 
		txtEnderecoCampus.setBounds(152, 210, 250, 26);
		pnAdicionar.add(txtEnderecoCampus);
		
		JLabel lblCidadeCampus = new JLabel("Cidade:");
		lblCidadeCampus.setFont(new Font("Segoe Ui", Font.PLAIN, 15));  
		lblCidadeCampus.setBounds(332, 255, 96, 25);  
		pnAdicionar.add(lblCidadeCampus);
		
		JLabel lblUfCampus = new JLabel("UF:");
		lblUfCampus.setFont(new Font("Segoe Ui", Font.PLAIN, 20));  
		lblUfCampus.setBounds(418, 210, 44, 25);
		pnAdicionar.add(lblUfCampus);
		
		JLabel lblEstadoCampus = new JLabel("Estado:");
		lblEstadoCampus.setFont(new Font("Segoe UI", Font.PLAIN, 16));  
		lblEstadoCampus.setBounds(70, 255, 72, 25);  
		pnAdicionar.add(lblEstadoCampus);
		
		cbCidadeCampus = new JComboBox();
		cbCidadeCampus.setFont(new Font("Perpetua", Font.PLAIN, 15));  
		cbCidadeCampus.setBounds(395, 255, 276, 26); 
		pnAdicionar.add(cbCidadeCampus);
		
		cbEstadoCampus = new JComboBox();
		cbEstadoCampus.setFont(new Font("Perpetua", Font.PLAIN, 18)); 
		cbEstadoCampus.setBounds(138, 255, 184, 26);
		pnAdicionar.add(cbEstadoCampus);
		
		//Adicionei os estados manualmente na ComboBox para evitar chamar a API
				cbEstadoCampus.setModel(new DefaultComboBoxModel(new String[] {"Selecione uma opção:", "Acre", "Alagoas", "Amapá", "Amazonas", "Bahia", "Ceará", "Distrito Federal", "Espírito Santo", "Goiás", "Maranhão", "Mato Grosso do Sul", "Mato Grosso", "Minas Gerais", "Pará", "Paraíba", "Paraná", "Pernambuco", "Piauí", "Rio de Janeiro", "Rio Grande do Norte", "Rio Grande do Sul", "Rondônia", "Roraima", "Santa Catarina", "São Paulo", "Sergipe", "Tocantins"}));
				cbEstadoCampus.addItemListener(new ItemListener() {
					
					
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
				                	txtUFCampus.setText(buscaApi.buscarSiglaPorEstado(estadoSelecionado));
				                	
				                	cbCidadeCampus.removeAllItems(); // Limpa as cidades anteriores
				                	
				                	//Define as cidades com base na sigla
				                	cbCidadeCampus.setModel(new DefaultComboBoxModel(buscaApi.buscarCidadesPorSigla(txtUFCampus.getText()).toArray()));
				                }
				                catch (Exception ex) {
				                	
				                    ex.printStackTrace();		                	
				                }
				            }
						}
					}
				});
		
		//Outros campos
		txtUFCampus = new JTextField();
		txtUFCampus.setFont(new Font("Perpetua", Font.PLAIN, 18));
		txtUFCampus.setEditable(false);
		txtUFCampus.setColumns(10);
		txtUFCampus.setBounds(457, 210, 214, 26);
		pnAdicionar.add(txtUFCampus);
		
		JLabel lblCepCampus = new JLabel("CEP:");
		lblCepCampus.setFont(new Font("Segoe UI", Font.PLAIN, 20));  
		lblCepCampus.setBounds(70, 165, 72, 25);  
		pnAdicionar.add(lblCepCampus);
		
		txtCepCampus = new JFormattedTextField();
		txtCepCampus.setFont(new Font("Perpetua", Font.PLAIN, 18));  
		txtCepCampus.setBounds(126, 165, 276, 26);  
		pnAdicionar.add(txtCepCampus);
		
		//Adiciona a mascára de CEP
		AplicarMascaras.aplicar(txtCepCampus, "CEP");
		
		// Botão Buscar CEP
		JButton btnBuscarCepCampus = new JButton("Buscar");
		btnBuscarCepCampus.setFont(new Font("Segoe UI", Font.PLAIN, 11)); 
		btnBuscarCepCampus.addActionListener(new ActionListener() {
			
			// Ação ao clicar no botão Buscar CEP
			public void actionPerformed(ActionEvent e) {
				
				buscaApi = new BuscaPorAPI();
				
				//Classe que tem os métodos para buscar na API
				BuscaPorAPI buscaApi = new BuscaPorAPI();
				
				//Classe para representar o Endereço na  busca por API
				Endereco endereco = new Endereco();
				
				try {
					
					//Pega o CEP digitado
					String cepDigitado = txtCepCampus.getText().replace("-", "").trim();
					
					if (!validarCampos.cep(txtCepCampus.getText())) return;
					
					//Busca o endereço com base no CEP
					endereco = buscaApi.buscarEnderecoPorCEP(cepDigitado);
					
					//Caso não encontre o CEP
					if (endereco == null) {
						JOptionPane.showMessageDialog(null, "CEP não encontrado. Verifique e tente novamente.");
						return;
					}
					
					//Popular os campos com o endereço retornado
					txtEnderecoCampus.setText(endereco.getLogradouro() + " - " + endereco.getBairro());
					txtEnderecoCampus.enable(false); //Proibe o usuário de alterar o endereço após buscar pelo CEP
					txtUFCampus.setText(endereco.getUf());
					cbEstadoCampus.setSelectedItem(endereco.getEstado());
					cbEstadoCampus.enable(false); //Proibe o usuário de alterar o estado após buscar pelo CEP
					cbCidadeCampus.removeAllItems();
					List<String> cidades = buscaApi.buscarCidadesPorSigla(endereco.getUf());
					cbCidadeCampus.setModel(new DefaultComboBoxModel(cidades.toArray()));
					cbCidadeCampus.enable(false);
					
					//Normaliza a busca da cidade para evitar erros com acentuação
					String cidadeEndereco = buscaApi.normalizar(endereco.getCidade());
		            for (String cidadeLista : cidades) {
		                if (buscaApi.normalizar(cidadeLista).equals(cidadeEndereco)) {
		                    cbCidadeCampus.setSelectedItem(cidadeLista);
		                    break;
		                }
		            }	
				} 
				catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Erro ao buscar o CEP: " + ex.getMessage());
				}				
			}				
		});
		btnBuscarCepCampus.setBounds(418, 167, 84, 25);
		pnAdicionar.add(btnBuscarCepCampus);
		
		JLabel lblNumeroAluno = new JLabel("Número:");
		lblNumeroAluno.setFont(new Font("Segoe Ui", Font.PLAIN, 15)); 
		lblNumeroAluno.setBounds(511, 165, 72, 25);
		pnAdicionar.add(lblNumeroAluno);
		
		txtNumeroCampus = new JTextField();
		txtNumeroCampus.setColumns(10);
		txtNumeroCampus.setBounds(580, 165, 91, 26);
		pnAdicionar.add(txtNumeroCampus);
		
		
		// Botão Salvar Campus
		JButton btnSalvarCampus = new JButton("Salvar");
		btnSalvarCampus.setFont(new Font("Segoe UI", Font.PLAIN, 18)); 
		btnSalvarCampus.addActionListener(new ActionListener() {
			
			// Ação ao clicar no botão Salvar
			public void actionPerformed(ActionEvent e) {
				
				//Salvar
			    campus = new Campus();
			    
			    //Valida se tem algum campo vazio
			    if (txtIdCampus.getText().isEmpty() ||
			    	    txtNomeCampus.getText().isEmpty() ||
			    	    txtCepCampus.getText().isEmpty())
			    	{

			    		//Avisa caso estiver algum vazio
			    	    JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
			    	    return;
			    	}
			    
			    //Popular variavel campus
				campus.setId(Integer.parseInt(txtIdCampus.getText()));
				campus.setNome(txtNomeCampus.getText());
				campus.setCep(txtCepCampus.getText());
				campus.setEndereco(txtEnderecoCampus.getText());
				campus.setNumero(Integer.parseInt(txtNumeroCampus.getText()));
				campus.setCidade(cbCidadeCampus.getSelectedItem().toString());
				campus.setEstado(cbEstadoCampus.getSelectedItem().toString());
				campus.setUf(txtUFCampus.getText());
				
				//Chama o DAO para salvar 
				try {
					//Chamar CampusDAO --> Abre o BD
					dao = new CampusDAO();
					
					dao.salvar(campus);
					
					JOptionPane.showMessageDialog(null, "Salvo com sucesso");
					
					//Atualiza a tabela de Campus
					carregarTabelaCampus();
					
					// Limpa os campos do Campus
					limparCamposCampus();
					
					// Habilita a aba "Visualizar" e volta para ela
					tabbedPane.setEnabledAt(0, true);
					tabbedPane.setSelectedIndex(0);	
					
					
				}
				catch (Exception erro) {
					
					JOptionPane.showMessageDialog(null,"Aconteceu erro: " + erro.getMessage());
				}	
			}
		});
		btnSalvarCampus.setBounds(185, 330, 102, 55);
		pnAdicionar.add(btnSalvarCampus);
		
		//Botão alterar Campus
				JButton btnAlterarCampus = new JButton("Alterar");
				btnAlterarCampus.setFont(new Font("Segoe UI", Font.PLAIN, 18));
				btnAlterarCampus.addActionListener(new ActionListener() {
					
					//Ao clicar no botão Alterar
					public void actionPerformed(ActionEvent e) {
						
						int linhaSelecionada = tableCampus.getSelectedRow();
						
						//Verifica se alguma linha foi selecionada
						if (linhaSelecionada == -1)
						{
							JOptionPane.showMessageDialog(null, "Selecione um campus para alterar.");
							return;
						}
						
						//Pega os dados da linha selecionada e preenche os campos na aba Alterar
						 String id = tableCampus.getValueAt(linhaSelecionada, 0).toString();
					     String nome = tableCampus.getValueAt(linhaSelecionada, 1).toString();
					     String cep = tableCampus.getValueAt(linhaSelecionada, 2).toString();
			             String endereco = tableCampus.getValueAt(linhaSelecionada, 3).toString();
			             String numero = tableCampus.getValueAt(linhaSelecionada, 4).toString();
			    	     String cidade = tableCampus.getValueAt(linhaSelecionada, 5).toString();
					     String estado = tableCampus.getValueAt(linhaSelecionada, 6).toString();
				         String uf = tableCampus.getValueAt(linhaSelecionada, 7).toString();
				         
				         // Habilita a aba "Alterar" e muda para ela
				         tabbedPane.setEnabledAt(2, true);
				         tabbedPane.setSelectedIndex(2);
				         
				         //Preenche os campos na aba "Alterar"
				         txtIdCampusAlterar.setText(id);
				         txtNomeCampusAlterar.setText(nome);
				         txtCepCampusAlterar.setText(cep);
				         txtEnderecoCampusAlterar.setText(endereco);
				         txtEnderecoCampusAlterar.enable(false);
				         txtNumeroCampusAlterar.setText(numero);
				         txtUFCampusAlterar.setText(uf);
				         cbEstadoCampusAlterar.setSelectedItem(estado);
				         cbEstadoCampusAlterar.enable(false); //Proibe o usuário de alterar o estado
				         
				         //Popula o CB cidade para fazer a seleção correta
				         BuscaPorAPI buscaApi = new BuscaPorAPI();
				         try {
				             List<String> cidades = buscaApi.buscarCidadesPorSigla(uf);
				             cbCidadeCampusAlterar.setModel(new DefaultComboBoxModel(cidades.toArray()));

				             //Normaliza para garantir que acentos e maiúsculas não atrapalhem
				             String cidadeNormalizada = buscaApi.normalizar(cidade);
				             for (String c : cidades) {
				                 if (buscaApi.normalizar(c).equals(cidadeNormalizada)) {
				                     cbCidadeCampusAlterar.setSelectedItem(c);
				                     break;
				                 }
				             }

				             cbCidadeCampusAlterar.setEnabled(false);
				         } catch (Exception ex) {
				        	 	    ex.printStackTrace();
				         }
					}
		});
				
		btnAlterarCampus.setBounds(290, 315, 130, 60);
		pnVisualizar.add(btnAlterarCampus);
		
		// ======== Eventos ========
		// Atualiza tabela sempre que a aba "Visualizar" for selecionada
		tabbedPane.addChangeListener(e -> {
			
				if (tabbedPane.getSelectedIndex() == 0) 
				{ // Índice 0 corresponde à aba "Visualizar"
		
					carregarTabelaCampus();
					
					tabbedPane.setEnabledAt(tabbedPane.indexOfTab("Adicionar"), false);
			        tabbedPane.setEnabledAt(tabbedPane.indexOfTab("Alterar"), false);
		
				}
		});
		
		txtIdCampus = new JTextField();
		txtIdCampus.setFont(new Font("Perpetua", Font.PLAIN, 17));  
		txtIdCampus.setBounds(126, 100, 196, 26);  
		pnAdicionar.add(txtIdCampus);
		txtIdCampus.setColumns(10);
		
		JLabel lblIDCampus = new JLabel("ID:");
		lblIDCampus.setFont(new Font("Segoe UI", Font.PLAIN, 20)); 
		lblIDCampus.setBounds(70, 100, 46, 25);  
		pnAdicionar.add(lblIDCampus);
		
		//Botao limpar tela
		JButton btnLimpar = new JButton("Limpar");
		btnLimpar.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		btnLimpar.addActionListener(new ActionListener() {
			
			// Ação ao clicar no botão Limpar
			public void actionPerformed(ActionEvent e) {
				
				txtIdCampus.setText("");
				txtNomeCampus.setText("");
				txtCepCampus.setText("");
				txtEnderecoCampus.setText("");
				txtEnderecoCampus.enable(true);
				txtUFCampus.setText("");
				cbEstadoCampus.setSelectedIndex(0);
				cbEstadoCampus.enable(true);
				cbCidadeCampus.removeAllItems();
				cbCidadeCampus.enable(true);
				txtNumeroCampus.setText("");
			}
		});
		btnLimpar.setBounds(315, 330, 102, 55);
		pnAdicionar.add(btnLimpar);
		
		//Botão voltar para a aba Visualizar
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		btnVoltar.addActionListener(new ActionListener() {
			
			// Ação ao clicar no botão Voltar
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(0); // Volta para a aba "Visualizar"
				tabbedPane.setEnabledAt(1, false); // Desabilita a aba "Adicionar"
			}
		});
		btnVoltar.setBounds(445, 330, 102, 55);
		pnAdicionar.add(btnVoltar);
		
		//Painel de Alterar Campus 
		JPanel pnAlterar = new JPanel();
		tabbedPane.addTab("Alterar", null, pnAlterar, null);
		pnAlterar.setLayout(null);
		
		txtNomeCampusAlterar = new JTextField();
		txtNomeCampusAlterar.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		txtNomeCampusAlterar.setColumns(10); 
		txtNomeCampusAlterar.setBounds(126, 55, 545, 26); 
		pnAlterar.add(txtNomeCampusAlterar);
		
		JLabel lblNomeCampus_1 = new JLabel("Nome:");
		lblNomeCampus_1.setFont(new Font("Segoe UI", Font.PLAIN, 16));  
		lblNomeCampus_1.setBounds(70, 55, 51, 25);
		pnAlterar.add(lblNomeCampus_1);
		
		JLabel lblEnderecoCampus_1 = new JLabel("Endereço:");
		lblEnderecoCampus_1.setFont(new Font("Segoe UI", Font.PLAIN, 16));  
		lblEnderecoCampus_1.setBounds(70, 210, 118, 25);
		pnAlterar.add(lblEnderecoCampus_1);
		
		txtEnderecoCampusAlterar = new JTextField();
		txtEnderecoCampusAlterar.setFont(new Font("Perpetua", Font.PLAIN, 15));  
		txtEnderecoCampusAlterar.setColumns(10); 
		txtEnderecoCampusAlterar.setBounds(152, 210, 250, 26); 
		pnAlterar.add(txtEnderecoCampusAlterar);
		
		JLabel lblCidadeCampus_1 = new JLabel("Cidade:");
		lblCidadeCampus_1.setFont(new Font("Segoe Ui", Font.PLAIN, 15));  
		lblCidadeCampus_1.setBounds(332, 255, 96, 25); 
		pnAlterar.add(lblCidadeCampus_1);
		
		JLabel lblUfCampus_1 = new JLabel("UF:");
		lblUfCampus_1.setFont(new Font("Segoe Ui", Font.PLAIN, 20));  
		lblUfCampus_1.setBounds(418, 210, 44, 25); 
		pnAlterar.add(lblUfCampus_1);
		
		JLabel lblEstadoCampus_1 = new JLabel("Estado:");
		lblEstadoCampus_1.setFont(new Font("Segoe UI", Font.PLAIN, 16));  
		lblEstadoCampus_1.setBounds(70, 255, 72, 25);
		pnAlterar.add(lblEstadoCampus_1);
		
		cbCidadeCampusAlterar = new JComboBox();
		cbCidadeCampusAlterar.setFont(new Font("Perpetua", Font.PLAIN, 15));  
		cbCidadeCampusAlterar.setBounds(395, 255, 276, 26); 
		pnAlterar.add(cbCidadeCampusAlterar);
		
		cbEstadoCampusAlterar = new JComboBox();
		cbEstadoCampusAlterar.setFont(new Font("Perpetua", Font.PLAIN, 18)); 
		cbEstadoCampusAlterar.setBounds(138, 255, 184, 26);
		pnAlterar.add(cbEstadoCampusAlterar);
		
		cbEstadoCampusAlterar.setModel(new DefaultComboBoxModel(new String[] {"Selecione uma opção:", "Acre", "Alagoas", "Amapá", "Amazonas", "Bahia", "Ceará", "Distrito Federal", "Espírito Santo", "Goiás", "Maranhão", "Mato Grosso do Sul", "Mato Grosso", "Minas Gerais", "Pará", "Paraíba", "Paraná", "Pernambuco", "Piauí", "Rio de Janeiro", "Rio Grande do Norte", "Rio Grande do Sul", "Rondônia", "Roraima", "Santa Catarina", "São Paulo", "Sergipe", "Tocantins"}));
		
		cbEstadoCampus.addItemListener(new ItemListener() {
			
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
		                	txtUFCampus.setText(buscaApi.buscarSiglaPorEstado(estadoSelecionado));
		                	
		                	cbCidadeCampusAlterar.removeAllItems(); // Limpa as cidades anteriores
		                	
		                	//Define as cidades com base na sigla
		                	cbCidadeCampusAlterar.setModel(new DefaultComboBoxModel(buscaApi.buscarCidadesPorSigla(txtUFCampus.getText()).toArray()));
		                }
		                catch (Exception ex) {
		                	
		                    ex.printStackTrace();		                	
		                }
		            }
				}
			}
		});
		
		
		
		
		txtUFCampusAlterar = new JTextField();
		txtUFCampusAlterar.setFont(new Font("Perpetua", Font.PLAIN, 18));
		txtUFCampusAlterar.setEditable(false);
		txtUFCampusAlterar.setColumns(10);
		txtUFCampusAlterar.setBounds(457, 210, 214, 26);
		pnAlterar.add(txtUFCampusAlterar);
		
		JLabel lblCepCampus_1 = new JLabel("CEP:");
		lblCepCampus_1.setFont(new Font("Segoe UI", Font.PLAIN, 20));  
		lblCepCampus_1.setBounds(70, 165, 72, 25);  
		pnAlterar.add(lblCepCampus_1);
		
		txtCepCampusAlterar = new JFormattedTextField();
		txtCepCampusAlterar.setFont(new Font("Perpetua", Font.PLAIN, 18));  
		txtCepCampusAlterar.setBounds(126, 165, 276, 26);  
		pnAlterar.add(txtCepCampusAlterar);
		
		//Adiciona a mascára de CEP
		AplicarMascaras.aplicar(txtCepCampusAlterar, "CEP");
		
		JButton btnBuscarCepCampusAlterar = new JButton("Buscar");
		btnBuscarCepCampusAlterar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		btnBuscarCepCampusAlterar.addActionListener(new ActionListener() {
			
			// Ação ao clicar no botão Buscar CEP
			public void actionPerformed(ActionEvent e) {
				
				buscaApi = new BuscaPorAPI();
					
				//Classe que tem os métodos para buscar na API
				BuscaPorAPI buscaApi = new BuscaPorAPI();
					
				//Classe para representar o Endereço na  busca por API
				Endereco endereco = new Endereco();
					
				try {
					//Pega o CEP digitado
					String cepDigitado = txtCepCampusAlterar.getText().replace("-", "").trim();
					
					//Busca o endereço com base no CEP
					endereco = buscaApi.buscarEnderecoPorCEP(cepDigitado);
						
					//Caso não encontre o CEP
					if (endereco == null) {
						JOptionPane.showMessageDialog(null, "CEP não encontrado. Verifique e tente novamente.");
						return;
					}
						
					//Popular os campos com o endereço retornado
					
					txtEnderecoCampusAlterar.setText(endereco.getLogradouro() + " - " + endereco.getBairro());
					txtEnderecoCampusAlterar.enable(false); //Proibe o usuário de alterar o endereço após buscar pelo CEP
					txtUFCampusAlterar.setText(endereco.getUf());
					cbEstadoCampusAlterar.setSelectedItem(endereco.getEstado());
					cbEstadoCampusAlterar.enable(false); //Proibe o usuário de alterar o estado após buscar pelo CEP
					cbCidadeCampusAlterar.setSelectedItem(endereco.getCidade());	
					cbCidadeCampusAlterar.enable(false);		
					
					
					} 
					catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Erro ao buscar o CEP: " + ex.getMessage());
					}				
				}				
			});
		
		btnBuscarCepCampusAlterar.setBounds(418, 167, 84, 25);
		pnAlterar.add(btnBuscarCepCampusAlterar);
		
		
		
		JLabel lblNumeroAluno_1 = new JLabel("Número:");
		lblNumeroAluno_1.setFont(new Font("Segoe Ui", Font.PLAIN, 15));  
		lblNumeroAluno_1.setBounds(511, 165, 72, 25);  
		pnAlterar.add(lblNumeroAluno_1);
		
		txtNumeroCampusAlterar = new JTextField();
		txtNumeroCampusAlterar.setFont(new Font("Perpetua", Font.PLAIN, 20));  
		txtNumeroCampusAlterar.setColumns(10);  
		txtNumeroCampusAlterar.setBounds(580, 165, 91, 26); 
		pnAlterar.add(txtNumeroCampusAlterar);
		
		//Botão Salvar Alterar Campus
		JButton btnSalvarCampusAlterar = new JButton("Alterar");
		btnSalvarCampusAlterar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Alterar
				campus = new Campus();
				
				// Populando o objeto aluno
				campus.setId(Integer.parseInt(txtIdCampusAlterar.getText()));
				campus.setNome(txtNomeCampusAlterar.getText());
				campus.setCep(txtCepCampusAlterar.getText());
				campus.setEndereco(txtEnderecoCampusAlterar.getText());
				campus.setNumero(Integer.parseInt(txtNumeroCampusAlterar.getText()));
				campus.setCidade(cbCidadeCampusAlterar.getSelectedItem().toString());
				campus.setEstado(cbEstadoCampusAlterar.getSelectedItem().toString());
				campus.setUf(txtUFCampusAlterar.getText());
				
				
				// Chama CampusDAO >> abre o banco de dados
				try {
					dao = new CampusDAO();
					dao.alterar(campus);
					JOptionPane.showMessageDialog(null,"Alterado com sucesso!");
					
					//Atualiza a tabela de Campus
					carregarTabelaCampus();
					
					// Habilita a aba "Visualizar" e volta para ela
					tabbedPane.setEnabledAt(0, true);	
					tabbedPane.setSelectedIndex(0);	
					
					
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Aconteceu erro: " + e1.getMessage());
				}	
			}
		});
		btnSalvarCampusAlterar.setFont(new Font("Segoe UI", Font.PLAIN, 18)); 
		btnSalvarCampusAlterar.setBounds(185, 330, 102, 55);
		pnAlterar.add(btnSalvarCampusAlterar);
		
		txtIdCampusAlterar = new JTextField();
		txtIdCampusAlterar.setColumns(10);
		txtIdCampusAlterar.setFont(new Font("Perpetua", Font.PLAIN, 17));  
		txtIdCampusAlterar.setBounds(126, 100, 196, 26);  
		pnAlterar.add(txtIdCampusAlterar);
		
		txtIdCampusAlterar.setEditable(false); //ID não pode ser alterado
		txtIdCampusAlterar.setEnabled(false);
		
		JLabel lblIDCampus_1 = new JLabel("ID:");
		lblIDCampus_1.setFont(new Font("Segoe UI", Font.PLAIN, 20)); 
		lblIDCampus_1.setBounds(70, 100, 46, 25);  
		pnAlterar.add(lblIDCampus_1);
		
		//Botao limpar tela alterar
		JButton btnLimparAlterar = new JButton("Limpar");
		btnLimparAlterar.setFont(new Font("Segoe UI", Font.PLAIN, 18)); 
		btnLimparAlterar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtNomeCampusAlterar.setText("");
				txtCepCampusAlterar.setText("");
				txtEnderecoCampusAlterar.setText("");
				txtEnderecoCampusAlterar.enable(true);
				txtUFCampusAlterar.setText("");
				cbEstadoCampusAlterar.setSelectedIndex(0);
				cbEstadoCampusAlterar.enable(true);
				cbCidadeCampusAlterar.removeAllItems();
				cbCidadeCampusAlterar.enable(true);
				txtNumeroCampusAlterar.setText("");
			}
		});
		btnLimparAlterar.setBounds(315, 330, 102, 55);
		pnAlterar.add(btnLimparAlterar);
		
		//Botão voltar para a aba Visualizar na tela alterar
		JButton btnVoltarAlterar = new JButton("Voltar");
		btnVoltarAlterar.setFont(new Font("Segoe UI", Font.PLAIN, 18)); 
		btnVoltarAlterar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(0); // Volta para a aba "Visualizar"
				tabbedPane.setEnabledAt(2, false); // Bloqueia a aba "Alterar"
				
				tabbedPane.setEnabledAt(0, false); // Desabilita a aba "Visualizar", para voltar apenas no botão
			}
		});
		btnVoltarAlterar.setBounds(445, 330, 102, 55);
		pnAlterar.add(btnVoltarAlterar);
		
		tabbedPane.setEnabledAt(2, false); // Desabilita a aba "Alterar" inicialmente
		
		//Carrega a tabela de Campus ao iniciar a tela
		carregarTabelaCampus();
		
		
	}
	
	
	//Método para carregar a tabela de Campus
	private void carregarTabelaCampus() {
		try {
			dao = new CampusDAO();
			List<Campus> lista = dao.listarTodos();

			modeloTabela.setRowCount(0); // limpa a tabela

			for (Campus c : lista) {
				modeloTabela.addRow(new Object[] {
					c.getId(),
					c.getNome(),
					c.getCep(),
					c.getEndereco(),
					c.getNumero(),
					c.getCidade(),
					c.getEstado(),
					c.getUf()
				});
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar tabela: " + e.getMessage());
		}
		
	}
	
	// Método para limpar os campos após salvar
			private void limparCamposCampus() {
			    txtIdCampus.setText("");
			    txtNomeCampus.setText("");
			    txtCepCampus.setText("");
			    txtEnderecoCampus.setText("");
			    txtNumeroCampus.setText("");
			    cbCidadeCampus.setSelectedIndex(-1);
			    cbEstadoCampus.setSelectedIndex(-1);
			    txtUFCampus.setText("");
			}	
			
}
