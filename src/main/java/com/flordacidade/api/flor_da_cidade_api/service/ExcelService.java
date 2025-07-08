package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.model.CursoModel;
import com.flordacidade.api.flor_da_cidade_api.model.Horta;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelService {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ByteArrayInputStream exportarCursosParaExcel(List<CursoModel> cursos) throws IOException {
        String[] columns = { "Nome", "Tipo", "Descrição", "Local", "Instituição", "Público Alvo", "Data Início",
                "Data Fim", "Inscrição Início", "Inscrição Fim", "Turno", "Vagas", "Carga Horária", "Ativo" };

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Cursos");

            // Cabeçalho
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Dados
            int rowIdx = 1;
            for (CursoModel curso : cursos) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(curso.getNome());
                row.createCell(1).setCellValue(curso.getTipoAtividade().toString());
                row.createCell(2).setCellValue(curso.getDescricao());
                row.createCell(3).setCellValue(curso.getLocal());
                row.createCell(4).setCellValue(curso.getInstituicao());
                row.createCell(5).setCellValue(curso.getPublicoAlvo().toString());
                row.createCell(6).setCellValue(curso.getDataInicio().format(dateFormatter));
                row.createCell(7).setCellValue(curso.getDataFim().format(dateFormatter));
                row.createCell(8).setCellValue(curso.getDataInscInicio().format(dateFormatter));
                row.createCell(9).setCellValue(curso.getDataInscFim().format(dateFormatter));
                row.createCell(10).setCellValue(curso.getTurno().toString());
                row.createCell(11).setCellValue(curso.getMaxPessoas());
                row.createCell(12).setCellValue(curso.getCargaHoraria());
                row.createCell(13).setCellValue(curso.getAtivo() ? "Sim" : "Não");
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public ByteArrayInputStream exportarHortasParaExcel(List<Horta> hortas) throws IOException {
        String[] columns = { "Nome da Horta", "Status", "Endereço", "Responsável", "Unidade de Ensino",
                "Tamanho (m²)", "Qtd. Pessoas" };

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Hortas");

            // Cabeçalho
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Dados
            int rowIdx = 1;
            for (Horta horta : hortas) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(horta.getNomeHorta());
                row.createCell(1).setCellValue(horta.getStatusHorta().toString());
                row.createCell(2).setCellValue(horta.getEndereco());
                String nomeResponsavel = "N/A";
                if (horta.getUsuario() != null && horta.getUsuario().getPessoa() != null) {
                    nomeResponsavel = horta.getUsuario().getPessoa().getNome();
                }
                row.createCell(3).setCellValue(nomeResponsavel);
                row.createCell(4).setCellValue(
                        horta.getUnidadeDeEnsino() != null ? horta.getUnidadeDeEnsino().getNome() : "N/A");
                row.createCell(5).setCellValue(horta.getTamanhoAreaProducao());
                row.createCell(6).setCellValue(horta.getQntPessoas());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
