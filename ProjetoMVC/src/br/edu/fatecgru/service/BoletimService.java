package br.edu.fatecgru.service;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import br.edu.fatecgru.dao.AlunoDAO;
import br.edu.fatecgru.dao.AlunoNotaFaltaDAO;
import br.edu.fatecgru.dao.CursoDAO;
import br.edu.fatecgru.dao.DisciplinaDAO;
import br.edu.fatecgru.model.Aluno;
import br.edu.fatecgru.model.AlunoNotaFalta;
import br.edu.fatecgru.model.Curso;


//Separei como serviço devido ao uso de bibliotecas mais específicas e pensando em "futuras" modificações
public class BoletimService {

    private AlunoDAO alunoDAO;
    private AlunoNotaFaltaDAO nfDAO;
    private CursoDAO cursoDAO;
    private DisciplinaDAO disciplinaDAO;

    public BoletimService() throws Exception {
        this.alunoDAO = new AlunoDAO();
        this.nfDAO = new AlunoNotaFaltaDAO();
        this.cursoDAO = new CursoDAO();
        this.disciplinaDAO = new DisciplinaDAO();
    }

    public String gerarBoletimHtml(int raAluno) throws Exception {

        Aluno aluno = alunoDAO.consultar(raAluno);
        Curso curso = cursoDAO.consultar(aluno.getId_curso());
        List<Integer> idsAlunoDisc = alunoDAO.buscarIdsAlunoDisciplinaPorRa(raAluno);

        StringBuilder html = new StringBuilder();

        html.append("""
        <html>
        <head>
        <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            font-size: 12px;
            color: #333;
        }
        .header {
            text-align: center;
            border-bottom: 2px solid #C8A951;
            padding-bottom: 10px;
            margin-bottom: 20px;
        }
        .title {
            font-size: 18px;
            font-weight: bold;
            color: #C8A951;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;
        }
        th {
            background-color: #444;
            color: #fff;
            padding: 6px;
            border: 1px solid #000;
        }
        td {
            padding: 5px;
            border: 1px solid #000;
            text-align: center;
        }
        .aprovado {
            color: green;
            font-weight: bold;
        }
        .reprovado {
            color: red;
            font-weight: bold;
        }
        .footer {
            margin-top: 15px;
            font-size: 10px;
            text-align: center;
        }
        </style>
        </head>
        <body>

        <div class='header'>
            <div class='title'>BOLETIM ACADÊMICO</div>
            <div>Ano Letivo 2025</div>
        </div>
        """);

        html.append("<p><b>Nome:</b> " + aluno.getNome() + "</p>");
        html.append("<p><b>RA:</b> " + aluno.getRa() + "</p>");
        html.append("<p><b>Curso:</b> " + curso.getNome_curso() + "</p>");

        html.append("""
        <table>
        <tr>
        <th>Disciplina</th>
        <th>Nota 1</th>
        <th>Nota 2</th>
        <th>Média</th>
        <th>Faltas</th>
        <th>Situação</th>
        </tr>
        """);

        for (int idAD : idsAlunoDisc) {
            AlunoNotaFalta nf = nfDAO.buscarPorAlunoDisciplina(idAD);
            String disciplinaNome = disciplinaDAO.buscarNomeDisciplinaPorAlunoDisciplina(idAD);

            double n1 = nf.getNota1();
            double n2 = nf.getNota2();
            double media = (n1 + n2) / 2.0;
            int faltas = nf.getFaltas(); // ✅ pegando faltas

            String situacao = media >= 6
                ? "<span class='aprovado'>APROVADO</span>"
                : "<span class='reprovado'>REPROVADO</span>";

            html.append("<tr>");
            html.append("<td>" + disciplinaNome + "</td>");
            html.append("<td>" + String.format("%.2f", n1) + "</td>");
            html.append("<td>" + String.format("%.2f", n2) + "</td>");
            html.append("<td>" + String.format("%.2f", media) + "</td>");
            html.append("<td>" + faltas + "</td>"); // ✅ exibindo faltas
            html.append("<td>" + situacao + "</td>");
            html.append("</tr>");
        }

        html.append("""
        </table>

        <div class='footer'>
        * Documento sem valor oficial
        </div>

        </body>
        </html>
        """);

        return html.toString();
    }

    public void gerarBoletimPDF(int raAluno, String caminhoArquivo) throws Exception {
        String html = gerarBoletimHtml(raAluno);

        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(caminhoArquivo));
        document.open();

        XMLWorkerHelper.getInstance().parseXHtml(
            writer,
            document,
            new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)),
            StandardCharsets.UTF_8
        );

        document.close();
    }
}