package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.dto.CursoResponseDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.HortaResponseDTO;
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

    public ByteArrayInputStream exportarCursosParaExcel(List<CursoResponseDTO> cursos) throws IOException {
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
            for (CursoResponseDTO cursoDTO : cursos) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(cursoDTO.getNome());
                row.createCell(1).setCellValue(cursoDTO.getTipoAtividade().toString());
                row.createCell(2).setCellValue(cursoDTO.getDescricao());
                row.createCell(3).setCellValue(cursoDTO.getLocal());
                row.createCell(4).setCellValue(cursoDTO.getInstituicao());
                row.createCell(5).setCellValue(cursoDTO.getPublicoAlvo().toString());
                row.createCell(6).setCellValue(cursoDTO.getDataInicio().format(dateFormatter));
                row.createCell(7).setCellValue(cursoDTO.getDataFim().format(dateFormatter));
                row.createCell(8).setCellValue(cursoDTO.getDataInscInicio().format(dateFormatter));
                row.createCell(9).setCellValue(cursoDTO.getDataInscFim().format(dateFormatter));
                row.createCell(10).setCellValue(cursoDTO.getTurno().toString());
                row.createCell(11).setCellValue(cursoDTO.getMaxPessoas());
                row.createCell(12).setCellValue(cursoDTO.getCargaHoraria());
                row.createCell(13).setCellValue(cursoDTO.getAtivo() ? "Sim" : "Não");
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public ByteArrayInputStream exportarHortasParaExcel(List<HortaResponseDTO> hortas) throws IOException {
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
            for (HortaResponseDTO hortaDTO : hortas) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(hortaDTO.getNomeHorta());
                row.createCell(1).setCellValue(hortaDTO.getStatusHorta());
                row.createCell(2).setCellValue(hortaDTO.getEndereco());
                row.createCell(3).setCellValue(hortaDTO.getNomeUsuario() != null ? hortaDTO.getNomeUsuario() : "N/A");
                row.createCell(4).setCellValue(
                        hortaDTO.getNomeUnidadeEnsino() != null ? hortaDTO.getNomeUnidadeEnsino() : "N/A");
                row.createCell(5).setCellValue(hortaDTO.getTamanhoAreaProducao());
                row.createCell(6).setCellValue(hortaDTO.getQntPessoas());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
