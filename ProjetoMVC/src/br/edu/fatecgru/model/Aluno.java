package br.edu.fatecgru.model;

public class Aluno {
	
	private int ra, numero, id_curso;
	private String nome, celular, dataNascimento, cpf, email, endereco, cep, cidade, estado,  uf;
	
	public Aluno() {
		
	}

	public Aluno(int ra, String nome, String celular, String dataNascimento, String cpf, String email,
			String endereco, int numero, String cep, String cidade, String estado, String uf, int id_curso) {
		super();
		this.ra = ra;
		this.nome = nome;
		this.celular = celular;
		this.dataNascimento = dataNascimento;
		this.cpf = cpf;
		this.email = email;
		this.endereco = endereco;
		this.numero = numero;
		this.cep = cep;
		this.cidade = cidade;
		this.estado = estado;
		this.uf = uf;
		this.id_curso = id_curso;
	}
	
	//Getters e Setters
	public int getRa() {
		return ra;
	}

	public void setRa(int ra) {
		this.ra = ra;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public int getId_curso() {
		return id_curso;
	}

	public void setId_curso(int id_curso) {
		this.id_curso = id_curso;
	}
	
	
	
}