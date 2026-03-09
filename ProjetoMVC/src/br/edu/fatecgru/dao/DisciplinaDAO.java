package br.edu.fatecgru.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.fatecgru.model.Disciplina;
import br.edu.fatecgru.util.ConnectionFactory;

public class DisciplinaDAO {
	
	private Connection conn;
	private PreparedStatement ps;
	private ResultSet rs;
	
	public DisciplinaDAO() throws Exception {
		// chama a classe ConnectionFactory e estabele uma conexão
		try {
			this.conn = ConnectionFactory.getConnection();
		} catch (Exception e) {
			throw new Exception("erro: \n" + e.getMessage());
		}
	}

	// método de salvar
	public void salvar(Disciplina disciplina) throws Exception {
		if (disciplina == null)
			throw new Exception("O valor passado nao pode ser nulo");
		try {
			String SQL = "INSERT INTO disciplina (id_disciplina, nome_disciplina) "
					+ "values (?, ?)";
			ps = conn.prepareStatement(SQL);
			ps.setInt(1, disciplina.getId());
			ps.setString(2, disciplina.getNome());
			ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new Exception("Erro ao inserir dados " + sqle);
		} finally {
			ConnectionFactory.closeConnection(conn, ps);;
		}
	}
	
	//Método de Listar/Atualizar todos as disciplinas para tabela 
	 public List<Disciplina> listarTodos() throws SQLException {
		 
		  List<Disciplina> lista = new ArrayList<>();
		  
		  try {
			  String sql = "SELECT * FROM disciplina";
			  ps = conn.prepareStatement(sql);
		      rs = ps.executeQuery();
		      
		      while (rs.next()) 
		      {
		        Disciplina d = new Disciplina();
		        d.setId(rs.getInt("id_disciplina"));
		        d.setNome(rs.getString("nome_disciplina"));
		        lista.add(d);
		      }
		      
		      return lista;
		  } 
		  catch (SQLException e) {
			  e.printStackTrace();
			  return null;
		  } 	finally {
					//ConnectionFactory.closeConnection(conn, ps, rs);
			}		 
	 }
	 
	// Método de alterar
	public void alterar(Disciplina disciplina) throws Exception {

		if (disciplina == null) throw new Exception("O valor passado nao pode ser nulo");
			try {
				String SQL = "UPDATE disciplina SET nome_disciplina = ? WHERE id_disciplina = ?";
				ps = conn.prepareStatement(SQL);
				ps.setString(1, disciplina.getNome());
				ps.setInt(2, disciplina.getId());
				ps.executeUpdate();
				} catch (SQLException sqle) {
					throw new Exception("Erro ao alterar dados " + sqle);
					} finally {
						ConnectionFactory.closeConnection(conn, ps);;
					}
						
					}
	
	// Método de excluir	
	public void excluir(Disciplina disciplina) throws Exception {
	    Connection conn = ConnectionFactory.getConnection();

	    try {
	        // 1️⃣ Apagar registros da tabela aluno_nota_falta
	        String sqlExcluirNotaFalta = 
	            "DELETE FROM aluno_nota_falta " +
	            "WHERE id_aluno_disciplina IN (" +
	            "SELECT ad.id FROM aluno_disciplina ad " +
	            "INNER JOIN curso_disciplina cd ON ad.id_curso_disciplina = cd.id " +
	            "WHERE cd.id_disciplina = ?)";
	        PreparedStatement psNotaFalta = conn.prepareStatement(sqlExcluirNotaFalta);
	        psNotaFalta.setInt(1, disciplina.getId());
	        psNotaFalta.executeUpdate();
	        psNotaFalta.close();

	        // 2️⃣ Apagar registros da tabela aluno_disciplina
	        String sqlExcluirAlunoDisciplina = 
	            "DELETE FROM aluno_disciplina " +
	            "WHERE id_curso_disciplina IN (" +
	            "SELECT id FROM curso_disciplina WHERE id_disciplina = ?)";
	        PreparedStatement psAlunoDisciplina = conn.prepareStatement(sqlExcluirAlunoDisciplina);
	        psAlunoDisciplina.setInt(1, disciplina.getId());
	        psAlunoDisciplina.executeUpdate();
	        psAlunoDisciplina.close();

	        // 3️⃣ Apagar vínculos na tabela curso_disciplina
	        String sqlExcluirCursoDisciplina = 
	            "DELETE FROM curso_disciplina WHERE id_disciplina = ?";
	        PreparedStatement psCursoDisciplina = conn.prepareStatement(sqlExcluirCursoDisciplina);
	        psCursoDisciplina.setInt(1, disciplina.getId());
	        psCursoDisciplina.executeUpdate();
	        psCursoDisciplina.close();

	        // 4️⃣ Finalmente, apagar a disciplina
	        String sqlExcluirDisciplina = 
	            "DELETE FROM disciplina WHERE id_disciplina = ?";
	        PreparedStatement psDisciplina = conn.prepareStatement(sqlExcluirDisciplina);
	        psDisciplina.setInt(1, disciplina.getId());
	        psDisciplina.executeUpdate();
	        psDisciplina.close();

	    } catch (SQLException e) {
	        throw new Exception("Erro ao excluir disciplina e dependências: " + e.getMessage());
	    } finally {
	        conn.close();
	    }
	}

			
			//Método de Listar/Atualizar todas as disciplinas, exceto as que já estão cadastradas
			 public List<Disciplina> listarTodasNaoCadastradas(int idCurso) throws SQLException {
				 
				  List<Disciplina> lista = new ArrayList<>();
				  
				  try {
					  String sql = "SELECT * FROM disciplina d " +
						        "WHERE d.id_disciplina NOT IN (" +
						        "    SELECT cd.id_disciplina FROM curso_disciplina cd WHERE cd.id_curso = ?" +
						        ")";

					  ps = conn.prepareStatement(sql);
					  ps.setInt(1, idCurso); 
				      rs = ps.executeQuery();
				      
				      while (rs.next()) 
				      {
				        Disciplina d = new Disciplina();
				        d.setId(rs.getInt("id_disciplina"));
				        d.setNome(rs.getString("nome_disciplina"));
				        lista.add(d);
				      }
				      
				      return lista;
				  } 
				  catch (SQLException e) {
					  e.printStackTrace();
					  return null;
				  } 	finally {
							//ConnectionFactory.closeConnection(conn, ps, rs);
					}		 
			 }
	
			 public String buscarNomeDisciplinaPorAlunoDisciplina(int idAlunoDisciplina) throws Exception {
				    String nome = null;

				    String sql = "SELECT d.nome_disciplina "
				               + "FROM aluno_disciplina ad "
				               + "JOIN curso_disciplina cd ON ad.id_curso_disciplina = cd.id "
				               + "JOIN disciplina d ON cd.id_disciplina = d.id_disciplina "
				               + "WHERE ad.id = ?";

				    try (Connection con = ConnectionFactory.getConnection();
				         PreparedStatement ps = con.prepareStatement(sql)) {

				        ps.setInt(1, idAlunoDisciplina);
				        ResultSet rs = ps.executeQuery();

				        if (rs.next()) {
				            nome = rs.getString("nome_disciplina");
				        }
				    }
				    return nome;
				}

}