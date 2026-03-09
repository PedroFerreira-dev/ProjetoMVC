package br.edu.fatecgru.model;

public class AlunoNotaFalta {
	
	//Atributos
	 private int id;
	 private int alunoDisciplinaId;
	 private double nota1;
     private double nota2;
     private int faltas;
     
     //Construtores
     public AlunoNotaFalta() {
    	 
     }
     
	 public AlunoNotaFalta(int id, int alunoDisciplinaId, double nota1, double nota2, int faltas) {
		this.id = id;
		this.alunoDisciplinaId = alunoDisciplinaId;
		this.nota1 = nota1;
		this.nota2 = nota2;
		this.faltas = faltas;
	 }

	 //Getters e Setters
	 public int getId() {
		 return id;
	 }


	 public void setId(int id) {
		 this.id = id;
	 }


	 public int getAlunoDisciplinaId() {
		 return alunoDisciplinaId;
	 }


	 public void setAlunoDisciplinaId(int alunoDisciplinaId) {
		 this.alunoDisciplinaId = alunoDisciplinaId;
	 }


	 public double getNota1() {
		 return nota1;
	 }


	 public void setNota1(double nota1) {
		 this.nota1 = nota1;
	 }


	 public double getNota2() {
		 return nota2;
	 }


	 public void setNota2(double nota2) {
		 this.nota2 = nota2;
	 }


	 public int getFaltas() {
		 return faltas;
	 }


	 public void setFaltas(int faltas) {
		 this.faltas = faltas;
	 }
}
