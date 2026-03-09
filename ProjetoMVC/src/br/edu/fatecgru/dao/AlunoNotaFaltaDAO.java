package br.edu.fatecgru.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import br.edu.fatecgru.model.AlunoNotaFalta;
import br.edu.fatecgru.util.ConnectionFactory;

public class AlunoNotaFaltaDAO {
	
	 private Connection conn;
	 private PreparedStatement ps;
	 private ResultSet rs;

	 public AlunoNotaFaltaDAO() throws Exception {
			// chama a classe ConnectionFactory e estabele uma conexão
			try {
				this.conn = ConnectionFactory.getConnection();
			} catch (Exception e) {
				throw new Exception("erro: \n" + e.getMessage());
			}
		}
	 
	 //As notas iniciais são criadas ao cadastrar o aluno, pois depois so será feito updates
	 public void salvarNotaInicial(int idAlunoDisciplina) throws Exception {
		    String sql = "INSERT INTO aluno_nota_falta (id_aluno_disciplina, nota1, nota2, faltas) " + "VALUES (?, 0, 0, 0)";

		    try (Connection con = ConnectionFactory.getConnection();
		         PreparedStatement ps = con.prepareStatement(sql)) {

		    	ps.setInt(1, idAlunoDisciplina);
	            ps.execute();
		    }
		}
	 
	 public AlunoNotaFalta buscarNotasFaltas(int idAlunoDisciplina) throws Exception {
	        AlunoNotaFalta anf = null;
	        String sql = "SELECT nota1, nota2, faltas FROM aluno_nota_falta WHERE id_aluno_disciplina = ?";

	        try (Connection con = ConnectionFactory.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {
	            
	            ps.setInt(1, idAlunoDisciplina);
	            ResultSet rs = ps.executeQuery();

	            if (rs.next()) {
	            	anf = new AlunoNotaFalta();
	            	anf.setNota1(rs.getObject("nota1") != null ? rs.getDouble("nota1") : null);
	            	anf.setNota2(rs.getObject("nota2") != null ? rs.getDouble("nota2") : null);
	            	anf.setFaltas(rs.getInt("faltas"));
	            }
	        }
	        return anf;
	    }
	 
	 
	 public void atualizarNotasFaltas(int idAlunoDisciplina, Double nota1, Double nota2, Integer faltas) throws Exception {
		    String sql = "UPDATE aluno_nota_falta SET nota1 = ?, nota2 = ?, faltas = ? WHERE id_aluno_disciplina = ?";

		    try (Connection con = ConnectionFactory.getConnection();
		         PreparedStatement ps = con.prepareStatement(sql)) {

		        if (nota1 != null) ps.setDouble(1, nota1);
		        else ps.setNull(1, java.sql.Types.DOUBLE);

		        if (nota2 != null) ps.setDouble(2, nota2);
		        else ps.setNull(2, java.sql.Types.DOUBLE);

		        ps.setInt(3, faltas);
		        ps.setInt(4, idAlunoDisciplina);

		        ps.executeUpdate();
		    }
		}
	 
	 // método de excluir
		
	 public void excluirPorAluno(int raAluno) throws Exception {
		    String sql = "DELETE FROM aluno_nota_falta "
		               + "WHERE id_aluno_disciplina IN (SELECT id FROM aluno_disciplina WHERE aluno_ra = ?)";

		    try (Connection con = ConnectionFactory.getConnection();
		         PreparedStatement ps = con.prepareStatement(sql)) {

		        ps.setInt(1, raAluno);
		        ps.executeUpdate();
		    }
		}
	 
	 public AlunoNotaFalta buscarPorAlunoDisciplina(int idAlunoDisciplina) throws Exception {
		    AlunoNotaFalta nf = null;
		    String sql = "SELECT * FROM aluno_nota_falta WHERE id_aluno_disciplina = ?";

		    try (Connection con = ConnectionFactory.getConnection();
		         PreparedStatement ps = con.prepareStatement(sql)) {

		        ps.setInt(1, idAlunoDisciplina);
		        ResultSet rs = ps.executeQuery();

		        if (rs.next()) {
		            nf = new AlunoNotaFalta();
		            nf.setAlunoDisciplinaId(idAlunoDisciplina);
		            nf.setNota1(rs.getObject("nota1") != null ? rs.getDouble("nota1") : null);
		            nf.setNota2(rs.getObject("nota2") != null ? rs.getDouble("nota2") : null);
		            nf.setFaltas(rs.getInt("faltas"));
		        }
		    }
		    return nf;
		}			
}