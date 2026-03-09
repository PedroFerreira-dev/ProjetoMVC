package br.edu.fatecgru.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class TelaPrincipal extends JFrame {
	
	
	//Atributos 
	private static final long serialVersionUID = 1L; //Relacionado a compatibilidade em caso de mudança
	private JPanel contentPane;
	
	//Variavel para TelaAlunos
	private TelaAlunos telaAlunos; 

	//Método Principal
	public static void main(String[] args) {
		try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaPrincipal frame = new TelaPrincipal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	//Criação da Tela
	public TelaPrincipal() {
		setTitle("Sistema de Gestão Acadêmica");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 834, 580);
		
		//Menu Aluno
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnAluno = new JMenu("Aluno");
		menuBar.add(mnAluno);
		
		JMenuItem mntmGerenciarAluno = new JMenuItem("Gerenciar");
		mntmGerenciarAluno.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				contentPane.removeAll(); // Remove todos os componentes atuais
				
				 // Adiciona o painel do módulo Campus
		        TelaAlunos painelGerenciarAlunos = new TelaAlunos();
		        painelGerenciarAlunos.setBounds(0, 0, contentPane.getWidth(), contentPane.getHeight());
		        contentPane.add(painelGerenciarAlunos);

		        // Atualiza a tela
		        contentPane.revalidate();
		        contentPane.repaint();		
			}
			
		});
		mnAluno.add(mntmGerenciarAluno);
		
		//Menu Campus
		JMenu mnCampus = new JMenu("Campus");
		menuBar.add(mnCampus);
		
		//Opção Campus --> Gerenciar
		JMenuItem mnGerenciarCampus = new JMenuItem("Gerenciar");
		mnGerenciarCampus.addActionListener(new ActionListener() {
			
			//Quando clicar no Gerenciar
			public void actionPerformed(ActionEvent e) {
				
				contentPane.removeAll(); // Remove todos os componentes atuais
				
				 // Adiciona o painel do módulo Campus
		        TelaCampus painelCampus = new TelaCampus();
		        painelCampus.setBounds(0, 0, contentPane.getWidth(), contentPane.getHeight());
		        contentPane.add(painelCampus);

		        // Atualiza a tela
		        contentPane.revalidate();
		        contentPane.repaint();		
			}
		});
		mnCampus.add(mnGerenciarCampus);
		
		JMenu mnCurso = new JMenu("Curso");
		menuBar.add(mnCurso);
		
		//Gerenciar Curso
		JMenuItem mntmGerenciarCurso = new JMenuItem("Gerenciar");
		mntmGerenciarCurso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				contentPane.removeAll(); // Remove todos os componentes atuais
				
				 // Adiciona o painel do módulo Campus
		        TelaCursos painelCurso = new TelaCursos();
		        painelCurso.setBounds(0, 0, contentPane.getWidth(), contentPane.getHeight());
		        contentPane.add(painelCurso);

		        // Atualiza a tela
		        contentPane.revalidate();
		        contentPane.repaint();		
			}
		});
		mnCurso.add(mntmGerenciarCurso);
		
		JMenu mnDisciplinas = new JMenu("Disciplina");
		menuBar.add(mnDisciplinas);
		
		//Gerenciar disciplinas
		JMenuItem mntmGerenciarDisciplinas = new JMenuItem("Gerenciar");
		mntmGerenciarDisciplinas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				contentPane.removeAll(); // Remove todos os componentes atuais
				
				// Adiciona o painel do módulo Campus
		        TelaDisciplina painelDisciplina = new TelaDisciplina();
		        painelDisciplina.setBounds(0, 0, contentPane.getWidth(), contentPane.getHeight());
		        contentPane.add(painelDisciplina);

		        // Atualiza a tela
		        contentPane.revalidate();
		        contentPane.repaint();		
			}
		});
		mnDisciplinas.add(mntmGerenciarDisciplinas);
		
		JMenu mnSair = new JMenu("Sair");
		menuBar.add(mnSair);
		
		// Para quando clicar no menu sair, ele exiba uma mensagem de confirmação
		mnSair.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent e) {
		        int opcao = JOptionPane.showConfirmDialog(null,
		            "Deseja realmente sair?", "Confirmação",
		            JOptionPane.YES_NO_OPTION);
		        if (opcao == JOptionPane.YES_OPTION) {
		            System.exit(0);
		        }
		    }
		});
		
		//mnGerenciarAluno.addActionListener(e -> abrirTela(new TelaAluno()));
        
		// Definição do painel principal
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());

		// Exibir a TelaAlunos logo ao iniciar
		telaAlunos = new TelaAlunos();
		contentPane.add(telaAlunos, BorderLayout.CENTER);
	}
}