package br.edu.fatecgru.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.fatecgru.model.Aluno;
import br.edu.fatecgru.util.ConnectionFactory;

public class AlunoDAO {
	
	private Connection conn;
	private PreparedStatement ps;
	private ResultSet rs;
	private Aluno aluno;
	
	
	//Throws transfere a responsabilidade para quem chamou o método, no caso a tela (não tem como mostrar o erro aqui pois não tem interface gráfica


	public AlunoDAO() throws Exception {
		// chama a classe ConnectionFactory e estabele uma conexão
		try {
			this.conn = ConnectionFactory.getConnection();
		} catch (Exception e) {
			throw new Exception("erro: \n" + e.getMessage());
		}
	}

	// método de salvar

	public void salvar(Aluno aluno) throws Exception {
		if (aluno == null)
			throw new Exception("O valor passado nao pode ser nulo");
		try {
			String SQL = "INSERT INTO tbaluno (ra, nome, celular, data_nascimento, cpf, email, endereco, numero, cep, cidade, estado, uf, id_curso) "
					+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			ps = conn.prepareStatement(SQL);
			ps.setInt(1, aluno.getRa());
			ps.setString(2, aluno.getNome());
			ps.setString(3, aluno.getCelular());
			ps.setString(4, aluno.getDataNascimento());
			ps.setString(5, aluno.getCpf());
			ps.setString(6, aluno.getEmail());
			ps.setString(7, aluno.getEndereco());
			ps.setInt(8, aluno.getNumero());
			ps.setString(9, aluno.getCep());
			ps.setString(10, aluno.getCidade());
			ps.setString(11, aluno.getEstado());
			ps.setString(12, aluno.getUf());
			ps.setInt(13, aluno.getId_curso());
			ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new Exception("Erro ao inserir dados " + sqle);
		} finally {
			//ConnectionFactory.closeConnection(conn, ps);;
		}
	}
	
	// método de alterar

	public void alterar(Aluno aluno) throws Exception {
		if (aluno == null)
			throw new Exception("O valor passado nao pode ser nulo");
		try {
			String SQL = "UPDATE tbaluno SET nome = ?, celular = ?, data_nascimento = ?, cpf = ?, email = ?, endereco = ?, numero = ?, cep = ?, cidade = ?, estado = ?, uf = ?, id_curso = ? WHERE ra = ?";
			ps = conn.prepareStatement(SQL);
			ps.setString(1, aluno.getNome());
			ps.setString(2, aluno.getCelular());
			ps.setString(3, aluno.getDataNascimento());
			ps.setString(4, aluno.getCpf());
			ps.setString(5, aluno.getEmail());
			ps.setString(6, aluno.getEndereco());
			ps.setInt(7, aluno.getNumero());
			ps.setString(8, aluno.getCep());
			ps.setString(9, aluno.getCidade());
			ps.setString(10, aluno.getEstado());
			ps.setString(11, aluno.getUf());
			ps.setInt(12, aluno.getId_curso());
			ps.setInt(13, aluno.getRa());
			ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new Exception("Erro ao inserir dados " + sqle);
		} finally {
			ConnectionFactory.closeConnection(conn, ps);;
		}

	}
	
	// método de consultar

	public Aluno consultar(Aluno aluno) throws Exception {
			if (aluno == null) 
				throw new Exception("O valor passado nao pode ser nulo");
				try {
					String SQL = ("SELECT * FROM tbaluno WHERE ra=?");
					ps = conn.prepareStatement(SQL);
					ps.setInt(1, aluno.getRa());
					rs = ps.executeQuery();
					Aluno a = new Aluno();
					while (rs.next()) {
						int ra = rs.getInt("ra");
						String nome = rs.getString("nome");
						String celular = rs.getString("celular");
						String nascimento = rs.getString("data_nascimento");
						String cpf = rs.getString("cpf");
						String email = rs.getString("email");
						String endereco = rs.getString("endereco");
						int numero = rs.getInt("numero");
						String cep = rs.getString("cep");
						String cidade = rs.getString("cidade");
						String estado = rs.getString("estado");
						String uf = rs.getString("uf");
						int id_curso = rs.getInt("id_curso");
						
						a = new Aluno(ra, nome, celular, nascimento, cpf, email, endereco, numero, cep, cidade, estado, uf, id_curso);
						return a;
					}		
				} catch (SQLException sqle) {
					throw new Exception(sqle);
				} finally {
					ConnectionFactory.closeConnection(conn, ps, rs);
				}
				return null;		
	}
			
	// método de excluir
			
	public void excluirCompleto(int ra) throws Exception {

			    // excluir notas e faltas
			    AlunoNotaFaltaDAO nfDAO = new AlunoNotaFaltaDAO();
			    nfDAO.excluirPorAluno(ra);

			    // excluir disciplinas
			    AlunoDisciplinaDAO adDAO = new AlunoDisciplinaDAO();
			    adDAO.excluirPorAluno(ra);

			    // excluir aluno
			    String SQL = "DELETE FROM tbaluno WHERE ra = ?";

			    try (Connection con = ConnectionFactory.getConnection();
			         PreparedStatement ps = con.prepareStatement(SQL)) {

			        ps.setInt(1, ra);
			        ps.executeUpdate();
			    }
	}

			
	//Método de Listar/Atualizar todos os Alunos para tabela 
	public ResultSet listarTodos() throws SQLException {
				 
			List<Aluno> lista = new ArrayList<>();
				  
				  try {
					  String sql = "SELECT \r\n"
					  		+ "    a.ra,\r\n"
					  		+ "    a.nome,\r\n"
					  		+ "    a.email,\r\n"
					  		+ "    a.celular,\r\n"
					  		+ "    a.cpf,\r\n"
					  		+ "    c.nome_campus AS campus,\r\n"
					  		+ "    CONCAT(cu.nome_curso, ' - ', cu.periodo) AS curso\r\n"
					  		+ "FROM tbaluno a\r\n"
					  		+ "JOIN curso cu ON a.id_curso = cu.id_curso\r\n"
					  		+ "JOIN campus c ON cu.id_campus = c.id_campus;";
					  
					  PreparedStatement ps = conn.prepareStatement(sql);
					   return ps.executeQuery();
				      
				
				  } 
				  catch (SQLException e) {
					  e.printStackTrace();
					  return null;
				  } 	finally {
							//ConnectionFactory.closeConnection(conn, ps, rs);
					}		 
	 }
			
	//Usei No boletim para pegar o aluno disciplina
	public List<Integer> buscarIdsAlunoDisciplinaPorRa(int raAluno) throws Exception {
			    List<Integer> lista = new ArrayList<>();
			    String sql = "SELECT id FROM aluno_disciplina WHERE aluno_ra  = ?";

			    try (Connection con = ConnectionFactory.getConnection();
			         PreparedStatement ps = con.prepareStatement(sql)) {

			        ps.setInt(1, raAluno);
			        ResultSet rs = ps.executeQuery();

			        while (rs.next()) {
			            lista.add(rs.getInt("id"));
			        }
			    }
			    return lista;
	}
			
	//Também usei no boletim para conseguir os dados através do RA
	public Aluno consultar(int ra) throws Exception {
			    try {
			        String SQL = "SELECT * FROM tbaluno WHERE ra = ?";
			        ps = conn.prepareStatement(SQL);
			        ps.setInt(1, ra);
			        rs = ps.executeQuery();

			        if (rs.next()) {
			            Aluno a = new Aluno();
			            a.setRa(rs.getInt("ra"));
			            a.setNome(rs.getString("nome"));
			            a.setCelular(rs.getString("celular"));
			            a.setDataNascimento(rs.getString("data_nascimento"));
			            a.setCpf(rs.getString("cpf"));
			            a.setEmail(rs.getString("email"));
			            a.setEndereco(rs.getString("endereco"));
			            a.setNumero(rs.getInt("numero"));
			            a.setCep(rs.getString("cep"));
			            a.setCidade(rs.getString("cidade"));
			            a.setEstado(rs.getString("estado"));
			            a.setUf(rs.getString("uf"));
			            a.setId_curso(rs.getInt("id_curso"));

			            return a;
			        }

			    } catch (SQLException sqle) {
			        throw new Exception("Erro ao consultar aluno: " + sqle);
			    } finally {
			        ConnectionFactory.closeConnection(conn, ps, rs);
			    }

			    return null;
	}
			
	// Método para pegar os dados do aluno pelo RA, usado no alterar
	public Aluno consultarPorRA(int ra) throws Exception {
			    Aluno aluno = null;

			    String sql = "SELECT * FROM tbaluno WHERE ra = ?";
			    PreparedStatement ps = conn.prepareStatement(sql);
			    ps.setInt(1, ra);
			    ResultSet rs = ps.executeQuery();

			    if (rs.next()) {
			        aluno = new Aluno();
			        aluno.setRa(rs.getInt("ra"));
			        aluno.setNome(rs.getString("nome"));
			        aluno.setDataNascimento(rs.getString("data_nascimento"));
			        aluno.setCpf(rs.getString("cpf"));
			        aluno.setCelular(rs.getString("celular"));
			        aluno.setEmail(rs.getString("email"));
			        aluno.setEndereco(rs.getString("endereco"));
			        aluno.setNumero(rs.getInt("numero"));
			        aluno.setCep(rs.getString("cep"));
			        aluno.setCidade(rs.getString("cidade"));
			        aluno.setEstado(rs.getString("estado"));
			        aluno.setUf(rs.getString("uf"));
			        aluno.setId_curso(rs.getInt("id_curso"));
			    }

			    rs.close();
			    ps.close();
			    return aluno;
	}			
}