package br.edu.fatecgru.model;

public class CursoDisciplina {
	
	//Atributos
	private int id;
	private int id_curso;
	private int id_disciplina;
	private int semestre;
	private Disciplina disciplina; //Atributo para preencher a table
	
	

	//Construtores
	public CursoDisciplina() {
		
	}

	public CursoDisciplina(int id_curso, int id_disciplina, int semestre) {
	
		this.id_curso = id_curso;
		this.id_disciplina = id_disciplina;
		this.semestre = semestre;
	}
	
	
	//Getters e Setters 

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	

	public int getId_curso() {
		return id_curso;
	}

	public void setId_curso(int id_curso) {
		this.id_curso = id_curso;
	}

	public int getId_disciplina() {
		return id_disciplina;
	}

	public void setId_disciplina(int id_disciplina) {
		this.id_disciplina = id_disciplina;
	}

	public int getSemestre() {
		return semestre;
	}

	public void setSemestre(int semestre) {
		this.semestre = semestre;
	}
	
	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}
	
	@Override
	public String toString() {
	    return disciplina.getNome();
	}
}
