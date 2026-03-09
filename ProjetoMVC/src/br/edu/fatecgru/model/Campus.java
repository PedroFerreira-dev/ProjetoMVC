package br.edu.fatecgru.model;

public class Campus {
	
	//Atributos 
	
	private int id, numero;
	private String nome;
	private String endereco, cep, cidade, estado,  uf;
	
	//Construtores
	public Campus() {
		
	}
	
	
	public Campus(int id, String nome, String endereco, String cep, String cidade, String estado, String uf, int numero) {
		this.id = id;
		this.nome = nome;
		this.endereco = endereco;
		this.cep = cep;
		this.cidade = cidade;
		this.estado = estado;
		this.uf = uf;
		this.numero = numero;
				
	}
	
	//Getters e Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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
	
	public int getNumero() {
		return numero;
	}


	public void setNumero(int numero) {
		this.numero = numero;
	}
	
	//To String para mostrar o nome no combobox
	@Override
	public String toString() {
	    return nome;
	}		
}
