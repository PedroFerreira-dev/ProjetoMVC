package br.edu.fatecgru.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

//Conexão com o Banco de dados
public class ConnectionFactory {
	
	public static Connection getConnection() throws Exception {
		
		try {
			//Selecionar o DB
			Class.forName("com.mysql.jdbc.Driver");
			
			//Caminho do DB
			String url = "jdbc:mysql://localhost:3306/bdprojetomvc?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
			String login = "root";
			String senha = "";
			
			return DriverManager.getConnection(url, login, senha);
			
		}
		catch (Exception erro) {
			
			throw new Exception(erro.getMessage());
		}
		
	}
	
	public static void closeConnection(Connection conn, Statement stmt,
			ResultSet rs) throws Exception {
		close(conn, stmt, rs);
	}

	public static void closeConnection(Connection conn, Statement stmt)
			throws Exception {
		close(conn, stmt, null);
	}

	public static void closeConnection(Connection conn) throws Exception {
		close(conn, null, null);
	}

	private static void close(Connection conn, Statement stmt, ResultSet rs)
			throws Exception {
		try {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	/*
	 * public static void main(String[] args) {
	 * 
	 * try { Connection conexao = ConnectionFactory.getConnection();
	 * 
	 * System.out.println("Conexão efetuada com sucesso"); } catch (Exception erro)
	 * { System.out.println(erro.getMessage()); } }
	 */

}