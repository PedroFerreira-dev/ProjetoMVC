package br.edu.fatecgru.model;

public class AlunoDisciplina {
	
	private int id;
	private int aluno_ra;
	private int id_cursoDisciplina;
	
	
	//Construtores 
	public AlunoDisciplina() {
		
	}

	public AlunoDisciplina(int id, int aluno_ra, int id_cursoDisciplina) {
		this.id = id;
		this.aluno_ra = aluno_ra;
		this.id_cursoDisciplina = id_cursoDisciplina;
		
	}

	//Atributos
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAluno_ra() {
		return aluno_ra;
	}

	public void setAluno_ra(int aluno_ra) {
		this.aluno_ra = aluno_ra;
	}

	public int getId_cursoDisciplina() {
		return id_cursoDisciplina;
	}

	public void setId_cursoDisciplina(int id_cursoDisciplina) {
		this.id_cursoDisciplina = id_cursoDisciplina;
	}
}
