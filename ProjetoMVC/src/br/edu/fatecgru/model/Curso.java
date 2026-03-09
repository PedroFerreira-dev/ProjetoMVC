package br.edu.fatecgru.model;

public class Curso {
	
	private int id_curso, id_campus, duracao_semestre;
	private String nome_curso, periodo;

	public Curso() {
		
	}

	public Curso(int id_curso, int id_campus, int duracao_semestre, String nome_curso, String periodo) {
		super();
		this.id_curso = id_curso;
		this.id_campus = id_campus;
		this.duracao_semestre = duracao_semestre;
		this.nome_curso = nome_curso;
		this.periodo = periodo;
	}
	
	// Getters e Setters

	public int getId_curso() {
		return id_curso;
	}

	public void setId_curso(int id_curso) {
		this.id_curso = id_curso;
	}

	public int getId_campus() {
		return id_campus;
	}

	public void setId_campus(int id_campus) {
		this.id_campus = id_campus;
	}

	public int getDuracao_semestre() {
		return duracao_semestre;
	}

	public void setDuracao_semestre(int duracao_semestre) {
		this.duracao_semestre = duracao_semestre;
	}

	public String getNome_curso() {
		return nome_curso;
	}

	public void setNome_curso(String nome_curso) {
		this.nome_curso = nome_curso;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	
	@Override
	public String toString() {
		 return nome_curso + " - " + periodo; // ComboBox exibirá o nome do curso
	}	
}

