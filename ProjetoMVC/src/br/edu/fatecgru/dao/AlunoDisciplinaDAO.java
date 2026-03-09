package br.edu.fatecgru.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.fatecgru.model.CursoDisciplina;
import br.edu.fatecgru.model.Disciplina;
import br.edu.fatecgru.util.ConnectionFactory;

public class AlunoDisciplinaDAO {

    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public AlunoDisciplinaDAO() throws Exception {
        try {
            this.conn = ConnectionFactory.getConnection();
        } catch (Exception e) {
            throw new Exception("erro: \n" + e.getMessage());
        }
    }

    // Método de salvar
    public void salvar(int raAluno, int idCursoDisciplina) throws Exception {
        String SQL = "INSERT INTO aluno_disciplina (aluno_ra, id_curso_disciplina) VALUES (?, ?)";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, raAluno);
            ps.setInt(2, idCursoDisciplina);
            ps.executeUpdate();
            
        } catch (SQLException sqle) {
            throw new Exception("Erro ao inserir dados " + sqle.getMessage(), sqle);
        }
    }

    // Listar disciplinas pelo RA do aluno
    public List<CursoDisciplina> listarDisciplinasPorAluno(int ra) throws Exception {
        List<CursoDisciplina> lista = new ArrayList<>();

        String SQL = "SELECT ad.id AS id_aluno_disciplina, cd.id AS id_curso_disciplina, "
                + "d.id_disciplina, d.nome_disciplina, cd.semestre "
                + "FROM aluno_disciplina ad "
                + "JOIN curso_disciplina cd ON ad.id_curso_disciplina = cd.id "
                + "JOIN disciplina d ON cd.id_disciplina = d.id_disciplina "
                + "WHERE ad.aluno_ra = ? "
                + "ORDER BY cd.semestre";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL)) {

            ps.setInt(1, ra);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CursoDisciplina cd = new CursoDisciplina();
                    Disciplina d = new Disciplina();

                    d.setId(rs.getInt("id_disciplina"));
                    d.setNome(rs.getString("nome_disciplina"));

                    cd.setId(rs.getInt("id_curso_disciplina"));
                    cd.setSemestre(rs.getInt("semestre"));
                    cd.setDisciplina(d);

                    lista.add(cd);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar disciplinas do aluno: " + e.getMessage(), e);
        }

        return lista;
    }

    // Retornar o id da relação usando aluno_ra + id_disciplina (útil para buscar notas)
    public Integer buscarIdAlunoDisciplina(int raAluno, int idDisciplina) throws Exception {
        String sql = "SELECT ad.id "
                + "FROM aluno_disciplina ad "
                + "JOIN curso_disciplina cd ON ad.id_curso_disciplina = cd.id "
                + "WHERE ad.aluno_ra = ? AND cd.id_disciplina = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, raAluno);
            ps.setInt(2, idDisciplina);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar id aluno_disciplina: " + e.getMessage(), e);
        }

        return null;
    }

    // Retornar o id da relação usando aluno_ra + id_curso_disciplina (mais direto após inserir)
    public Integer buscarIdPorAlunoECursoDisciplina(int raAluno, int idCursoDisciplina) throws Exception {
        String sql = "SELECT id FROM aluno_disciplina WHERE aluno_ra = ? AND id_curso_disciplina = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, raAluno);
            ps.setInt(2, idCursoDisciplina);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar aluno_disciplina por curso_disciplina: " + e.getMessage(), e);
        }

        return null;
    }

    // Excluir todas as relações aluno_disciplina de um aluno
    public void excluirPorAluno(int raAluno) throws Exception {
        String sql = "DELETE FROM aluno_disciplina WHERE aluno_ra = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, raAluno);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            throw new Exception("Erro ao excluir disciplinas do aluno: " + sqle.getMessage(), sqle);
        }
    }

    // Usado no alterar: excluir relações antigas do aluno (apenas aluno_disciplina)
    public void excluirPorRaAluno(int raAluno) throws Exception {
        excluirPorAluno(raAluno);
    }

    // Inserir e retornar o id da relação recém-criada (inserção + lookup)
    public Integer alterarAlunoDisciplina(int raAluno, int idCursoDisciplina) throws Exception {
        // Insere a relação
        salvar(raAluno, idCursoDisciplina);
        // Retorna o id da relação recém-criada
        return buscarIdPorAlunoECursoDisciplina(raAluno, idCursoDisciplina);
    }
}
