package br.edu.fatecgru.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.fatecgru.model.Curso;
import br.edu.fatecgru.model.CursoDisciplina;
import br.edu.fatecgru.model.Disciplina;
import br.edu.fatecgru.util.ConnectionFactory;

public class CursoDisciplinaDAO {
	
	private Connection conn;
	private PreparedStatement ps;
	private ResultSet rs;
	private Disciplina disciplina;
	private Curso curso;
	
	
	public CursoDisciplinaDAO() throws Exception {
		// chama a classe ConnectionFactory e estabele uma conexão
		try {
			this.conn = ConnectionFactory.getConnection();
		} catch (Exception e) {
			throw new Exception("erro: \n" + e.getMessage());
		}
	}
	
	// método de salvar
	public void salvar(CursoDisciplina curso_disciplina) throws Exception {
		if (curso_disciplina == null)	throw new Exception("O valor passado nao pode ser nulo");
			try {
				String SQL = "INSERT INTO curso_disciplina (id_curso, id_disciplina, semestre) "
						+ "values (?, ?, ?)";
				ps = conn.prepareStatement(SQL);
				ps.setInt(1, curso_disciplina.getId_curso());
				ps.setInt(2, curso_disciplina.getId_disciplina());
				ps.setInt(3, curso_disciplina.getSemestre());
				ps.executeUpdate();
			} catch (SQLException sqle) {
				throw new Exception("Erro ao inserir dados " + sqle);
			} finally {
				//ConnectionFactory.closeConnection(conn, ps);;
			}
		}
	
	// Listar As disciplinas pelo ID do curso
	public List<CursoDisciplina> listarDisciplinasPorCurso(int idCurso) throws Exception {
	    List<CursoDisciplina> lista = new ArrayList<>();

	    try {
	        String SQL ="SELECT cd.id, d.id_disciplina, d.nome_disciplina, cd.semestre "
	                + "FROM curso_disciplina cd "
	                + "JOIN disciplina d ON cd.id_disciplina = d.id_disciplina "
	                + "WHERE cd.id_curso = ? "
	                + "ORDER BY cd.semestre, d.nome_disciplina";
	        ps = conn.prepareStatement(SQL);
	        ps.setInt(1, idCurso);
	        
	        rs = ps.executeQuery();
	        
	        while (rs.next()) {
	        	 CursoDisciplina cd = new CursoDisciplina();
	             Disciplina d = new Disciplina();
	             
	             cd.setId(rs.getInt("id")); // <-- pega o id da tabela curso_disciplina
	             
	             d.setId(rs.getInt("id_disciplina"));
	             d.setNome(rs.getString("nome_disciplina"));

	             cd.setDisciplina(d);
	             cd.setId_disciplina(d.getId());
	             cd.setSemestre(rs.getInt("semestre"));

	             lista.add(cd);
	        }

	    } catch (SQLException e) {
	        throw new Exception("Erro ao listar disciplinas do curso: " + e.getMessage());
	    }

	    return lista;
	}
	
	public void excluirDisciplinaDoCurso(int idCurso, int idDisciplina) throws Exception {
	    try {
	        String SQL = "DELETE FROM curso_disciplina WHERE id_curso = ? AND id_disciplina = ?";
	        ps = conn.prepareStatement(SQL);
	        ps.setInt(1, idCurso);
	        ps.setInt(2, idDisciplina);
	        ps.executeUpdate();
	    } catch (SQLException e) {
	        throw new Exception("Erro ao remover disciplina da grade: " + e.getMessage());
	    }
	}
	
	// O método de filtro por semestre
	public List<CursoDisciplina> listarDisciplinasPorCursoESemestre(int idCurso, int semestre) throws Exception {
	    List<CursoDisciplina> lista = new ArrayList<>();

	    try {
	        String SQL = "SELECT cd.id, d.id_disciplina, d.nome_disciplina, cd.semestre "
	                   + "FROM curso_disciplina cd "
	                   + "JOIN disciplina d ON cd.id_disciplina = d.id_disciplina "
	                   + "WHERE cd.id_curso = ? AND cd.semestre = ? "
	                   + "ORDER BY d.nome_disciplina";

	        ps = conn.prepareStatement(SQL);
	        ps.setInt(1, idCurso);
	        ps.setInt(2, semestre);
	        rs = ps.executeQuery();

	        while (rs.next()) {
	            CursoDisciplina cd = new CursoDisciplina();
	            Disciplina d = new Disciplina();

	            cd.setId(rs.getInt("id"));
	            d.setId(rs.getInt("id_disciplina"));
	            d.setNome(rs.getString("nome_disciplina"));
	            cd.setDisciplina(d);
	            cd.setId_disciplina(d.getId());
	            cd.setSemestre(rs.getInt("semestre"));
	            lista.add(cd);
	        }
	    } catch (SQLException e) {
	        throw new Exception("Erro ao listar disciplinas: " + e.getMessage());
	    }

	    return lista;
	}
	
}