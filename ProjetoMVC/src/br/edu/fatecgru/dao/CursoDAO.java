package br.edu.fatecgru.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.fatecgru.model.Curso;
import br.edu.fatecgru.util.ConnectionFactory;

public class CursoDAO {
	private Connection conn;
	private PreparedStatement ps;
	private ResultSet rs;
	private Curso curso;
	
	
	
	//Throws transfere a responsabilidade para quem chamou o método, no caso a tela (não tem como mostrar o erro aqui pois não tem interface gráfica


	public CursoDAO() throws Exception {
			// chama a classe ConnectionFactory e estabele uma conexão
			try {
				this.conn = ConnectionFactory.getConnection();
			} catch (Exception e) {
				throw new Exception("erro: \n" + e.getMessage());
			}
	}

	// método de salvar
	public void salvar(Curso curso) throws Exception {
			if (curso == null)
				throw new Exception("O valor passado nao pode ser nulo");
			try {
				String SQL = "INSERT INTO curso (id_curso, nome_curso, id_campus, periodo, duracao_semestre) "
						+ "values (?, ?, ?, ?, ?)";
				ps = conn.prepareStatement(SQL);
				ps.setInt(1, curso.getId_curso());
				ps.setString(2, curso.getNome_curso());
				ps.setInt(3, curso.getId_campus());
				ps.setString(4, curso.getPeriodo());
				ps.setInt(5, curso.getDuracao_semestre());
				ps.executeUpdate();
			} catch (SQLException sqle) {
				throw new Exception("Erro ao inserir dados " + sqle);
			} finally {
				ConnectionFactory.closeConnection(conn, ps);;
			}
	}
		
	//Consultar
	public Curso consultar(int idCurso) {
		    String sql = "SELECT * FROM curso WHERE id_curso = ?";
		    Curso curso = null;

		    try {
		        conn = ConnectionFactory.getConnection();
		        ps = conn.prepareStatement(sql);
		        ps.setInt(1, idCurso);
		        rs = ps.executeQuery();

		        if (rs.next()) {
		            curso = new Curso();
		            curso.setId_curso(rs.getInt("id_curso"));
		            curso.setNome_curso(rs.getString("nome_curso"));
		            curso.setId_campus(rs.getInt("id_campus"));
		            curso.setPeriodo(rs.getString("periodo"));
		            curso.setDuracao_semestre(rs.getInt("duracao_semestre"));
		        }
		    } catch (Exception e) {
		        System.out.println("Erro ao buscar curso por ID: " + e.getMessage());
		    } finally {
		        //ConnectionFactory.closeConnection(conn, ps, rs);
		    }

		    return curso;
	}
		
	//Método de Listar/Atualizar todos os Campus para tabela 
	public List<Curso> listarTodos() throws SQLException {
			 
			  List<Curso> lista = new ArrayList<>();
			  
			  try {
				  String sql = "SELECT * FROM curso";
				  ps = conn.prepareStatement(sql);
			      rs = ps.executeQuery();
			      
			      while (rs.next()) 
			      {
			        Curso c = new Curso();
			        c.setId_curso(rs.getInt("id_curso"));
			        c.setNome_curso(rs.getString("nome_curso"));
	                c.setId_campus(rs.getInt("id_campus"));
	                c.setPeriodo(rs.getString("periodo"));
	 	            c.setDuracao_semestre(rs.getInt("duracao_semestre"));
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
		public void excluir(Curso curso) throws Exception {
			if (curso == null) throw new Exception("O valor passado nao pode ser nulo");
				try {
					
					//Deletar Curso_Disciplina antes de deletar curso
					
					String sqlDeleteGrade = "DELETE FROM curso_disciplina WHERE id_curso = ?";
					ps = conn.prepareStatement(sqlDeleteGrade);
					ps.setInt(1, curso.getId_curso());
					ps.executeUpdate();
					
					//Deletar da tabela curso
					String SQL = "DELETE FROM curso WHERE id_curso = ?";
					conn = this.conn;
					ps = conn.prepareStatement(SQL);
					ps.setInt(1, curso.getId_curso());
					ps.executeUpdate();
				} catch (SQLException sqle) {
				   throw new Exception("Erro ao excluir dados " + sqle);
					} finally {
							ConnectionFactory.closeConnection(conn, ps);						
							}
		}
		
		// método de alterar
			public void alterar(Curso curso) throws Exception {

				if (curso == null) throw new Exception("O valor passado nao pode ser nulo");
					try {
						String SQL = "UPDATE curso SET nome_curso = ?, id_campus = ?, periodo = ?, duracao_semestre = ? WHERE id_curso = ?";
						ps = conn.prepareStatement(SQL);
						ps.setInt(5, curso.getId_curso());
						ps.setString(1, curso.getNome_curso());
						ps.setInt(2, curso.getId_campus());
						ps.setString(3, curso.getPeriodo());
						ps.setInt(4, curso.getDuracao_semestre());
						ps.executeUpdate();
					} catch (SQLException sqle) {
						throw new Exception("Erro ao alterar dados " + sqle);
					} finally {
							ConnectionFactory.closeConnection(conn, ps);;
					}
					
				}
			
			//Listar por campus
			public List<Curso> listarPorCampus(int idCampus) throws SQLException {
			    List<Curso> lista = new ArrayList<>();

			    String sql = "SELECT id_curso, nome_curso, id_campus, periodo, duracao_semestre "
			               + "FROM curso WHERE id_campus = ? ORDER BY nome_curso";

			    PreparedStatement ps = conn.prepareStatement(sql);
			    ps.setInt(1, idCampus);
			    ResultSet rs = ps.executeQuery();

			    while (rs.next()) {
			        Curso c = new Curso();
			        c.setId_curso(rs.getInt("id_curso"));
			        c.setNome_curso(rs.getString("nome_curso"));
			        c.setId_campus(rs.getInt("id_campus"));
			        c.setPeriodo(rs.getString("periodo"));
			        c.setDuracao_semestre(rs.getInt("duracao_semestre"));
			        lista.add(c);
			    }

			    return lista;
			}
			
			// Buscar quantidade de semestres de um curso pelo nome
			public int buscarQuantidadeSemestres(String nomeCurso) throws Exception {
			    int qtdSemestres = 0;

			    try {
			        String sql = "SELECT duracao_semestre FROM curso WHERE nome_curso = ?";
			        ps = conn.prepareStatement(sql);
			        ps.setString(1, nomeCurso);
			        rs = ps.executeQuery();

			        if (rs.next()) {
			            qtdSemestres = rs.getInt("duracao_semestre");
			        }
			    } catch (SQLException e) {
			        throw new Exception("Erro ao buscar quantidade de semestres: " + e.getMessage());
			    } finally {
			        ConnectionFactory.closeConnection(conn, ps, rs);
			    }

			    return qtdSemestres;
			}		
}
