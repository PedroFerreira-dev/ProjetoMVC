package br.edu.fatecgru.model;


//Classe para representar o Endereço na  busca por API
public class Endereco {
	
	//Atributos
	 private String uf;
	 private String cidade;
     private String logradouro;
     private String bairro;
     private String estado;
     
     
	 //Getters e Setters
	 public String getUf() {
		 return uf;
	 }
	 
	 public void setUf(String uf) {
		 this.uf = uf;
	 }
	 
	 public String getCidade() {
		 return cidade;
	 }
	 
	 public void setCidade(String cidade) {
		 this.cidade = cidade;
	 }
	 
	 public String getLogradouro() {
		 return logradouro;
	 }
	 
	 public void setLogradouro(String logradouro) {
		 this.logradouro = logradouro;
	 }
	 
	 public String getBairro() {
		 return bairro;
	 }
	 
	 public void setBairro(String bairro) {
		 this.bairro = bairro;
	 }
	 
	 public String getEstado() {
		return estado;
     }
	 
	 public void setEstado(String estado) {
		 this.estado = estado;
	 }
     
     
   
}
