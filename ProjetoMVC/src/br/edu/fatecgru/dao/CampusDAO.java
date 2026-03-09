package br.edu.fatecgru.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.fatecgru.model.Campus;
import br.edu.fatecgru.util.ConnectionFactory;

public class CampusDAO {
	
	private Connection conn;
	private PreparedStatement ps;
	private ResultSet rs;
	
	public CampusDAO() throws Exception {
		// chama a classe ConnectionFactory e estabele uma conexão
		try {
			this.conn = ConnectionFactory.getConnection();
		} catch (Exception e) {
			throw new Exception("erro: \n" + e.getMessage());
		}
	}
	
	//Método de salvar
	public void salvar(Campus campus) throws Exception {
		if (campus == null) throw new Exception("O valor passado nao pode ser nulo");
			try {
				String SQL = "INSERT INTO campus (id_campus, nome_campus, cep, endereco, numero, cidade, estado, uf) "
						+ "values (?, ?, ?, ?, ?, ?, ?, ?)";
				ps = conn.prepareStatement(SQL);
				ps.setInt(1, campus.getId());
				ps.setString(2, campus.getNome());
				ps.setString(3, campus.getCep());
				ps.setString(4, campus.getEndereco());
				ps.setInt(5, campus.getNumero());
				ps.setString(6, campus.getCidade());
				ps.setString(7, campus.getEstado());
				ps.setString(8, campus.getUf());
				ps.executeUpdate();
			} catch (SQLException sqle) {
				throw new Exception("Erro ao inserir dados " + sqle);
			} finally {
				ConnectionFactory.closeConnection(conn, ps);;
			}
		}
		
	//Método de Listar/Atualizar todos os Campus para tabela 
	public List<Campus> listarTodos() throws SQLException {
			 
			  List<Campus> lista = new ArrayList<>();
			  
			  try {
				  String sql = "SELECT * FROM campus ORDER BY nome_campus";
				  ps = conn.prepareStatement(sql);
			      rs = ps.executeQuery();
			      
			      while (rs.next()) 
			      {
			        Campus c = new Campus();
			        c.setId(rs.getInt("id_campus"));
			        c.setNome(rs.getString("nome_campus"));
	                c.setCep(rs.getString("cep"));
	                c.setEndereco(rs.getString("endereco"));
    	            c.setNumero(rs.getInt("numero"));
 		            c.setCidade(rs.getString("cidade"));
 		            c.setEstado(rs.getString("estado"));
			        c.setUf(rs.getString("uf"));
			        lista.add(c);
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
		 
		// método de excluir
			
		public void excluir(Campus campus) throws Exception {
					    conn = ConnectionFactory.getConnection();
					    
					    try {
					    	
					    	//Primeiro vai excluir a falta
					    	String sqlExcluirNotaFalta =
					    		    "DELETE FROM aluno_nota_falta WHERE id_aluno_disciplina IN (" +
					    		    " SELECT ad.id FROM aluno_disciplina ad " +
					    		    " JOIN curso_disciplina cd ON ad.id_curso_disciplina = cd.id " +
					    		    " JOIN curso c ON cd.id_curso = c.id_curso WHERE c.id_campus = ?)";
					    	PreparedStatement psNotaFalta = conn.prepareStatement(sqlExcluirNotaFalta);
					    	psNotaFalta.setInt(1, campus.getId());
					    	psNotaFalta.executeUpdate();
					    		String sqlExcluirAlunoDisciplina =
					    		    "DELETE FROM aluno_disciplina WHERE id_curso_disciplina IN (" +
					    		    " SELECT cd.id FROM curso_disciplina cd JOIN curso c ON cd.id_curso = c.id_curso WHERE c.id_campus = ?)";
					    		
					    		PreparedStatement psAlunoDisciplina = conn.prepareStatement(sqlExcluirAlunoDisciplina);
					    		psAlunoDisciplina.setInt(1, campus.getId());
					    		psAlunoDisciplina.executeUpdate();
					    		
					    		String sqlExcluirCursoDisciplina = "DELETE FROM curso_disciplina WHERE id_curso IN (SELECT id_curso FROM curso WHERE id_campus = ?)";
					    		PreparedStatement psCursoDisciplina = conn.prepareStatement(sqlExcluirCursoDisciplina);
					    		psAlunoDisciplina.setInt(1, campus.getId());
					    		psAlunoDisciplina.executeUpdate();
					    		
					    		
					    	
		
					    	
					        // Primeiro, excluir os cursos associados a esse campus
					        String sqlExcluirCursos = "DELETE FROM curso WHERE id_campus = ?";
					        PreparedStatement psCursos = conn.prepareStatement(sqlExcluirCursos);
					        psCursos.setInt(1, campus.getId());
					        psCursos.executeUpdate();
					        
					        // Depois, excluir o campus
					        String sqlExcluirCampus = "DELETE FROM campus WHERE id_campus = ?";
					        PreparedStatement psCampus = conn.prepareStatement(sqlExcluirCampus);
					        psCampus.setInt(1, campus.getId());
					        psCampus.executeUpdate();

					        psCampus.close();
					        psCursos.close();
					    } catch (SQLException e) {
					        throw new Exception("Erro ao excluir campus: " + e.getMessage());
					    } finally {
					        conn.close();
					    }
		}
			
		// método de alterar

		public void alterar(Campus campus) throws Exception {
				if (campus == null)
					throw new Exception("O valor passado nao pode ser nulo");
				try {
					String SQL = "UPDATE campus SET nome_campus = ?, cep = ?, endereco = ?, numero = ?, cidade = ?, estado = ?, uf = ? WHERE id_campus = ?";
					ps = conn.prepareStatement(SQL);
					ps.setString(1, campus.getNome());
					ps.setString(2, campus.getCep());
					ps.setString(3, campus.getEndereco());
					ps.setInt(4, campus.getNumero());
					ps.setString(5, campus.getCidade());
					ps.setString(6, campus.getEstado());
					ps.setString(7, campus.getUf());
					ps.setInt(8, campus.getId());
					ps.executeUpdate();
				} catch (SQLException sqle) {
					throw new Exception("Erro ao inserir dados " + sqle);
				} finally {
					ConnectionFactory.closeConnection(conn, ps);;
				}
				
		}
			
		//Consultar por ID para mostrar na lista dos cursos --> Literalmente um método consultar
		public Campus consultarPorId(int id) throws SQLException {
		    	
		    	//Váriavel para armazenar o campus consultado
		        Campus campus = null;

		        try {
		            String sql = "SELECT * FROM campus WHERE id_campus = ?";
		            ps = conn.prepareStatement(sql);
		            ps.setInt(1, id);
		            rs = ps.executeQuery();

		            if (rs.next()) {
		                campus = new Campus();
		                campus.setId(rs.getInt("id_campus"));
		                campus.setNome(rs.getString("nome_campus"));
		                campus.setCep(rs.getString("cep"));
		                campus.setEndereco(rs.getString("endereco"));
		                campus.setNumero(rs.getInt("numero"));
		                campus.setCidade(rs.getString("cidade"));
		                campus.setEstado(rs.getString("estado"));
		                campus.setUf(rs.getString("uf"));
		            }

		        } catch (SQLException e) {
		            throw new SQLException("Erro ao consultar campus por ID: " + e.getMessage());
		        } finally {
		            //ConnectionFactory.closeConnection(null, ps, rs);
		        }

		        return campus;
		}			
}


