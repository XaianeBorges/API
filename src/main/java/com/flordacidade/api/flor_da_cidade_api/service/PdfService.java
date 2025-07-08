package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.model.Horta;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

    public ByteArrayInputStream gerarPdfHorta(Horta horta)
            throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (PdfWriter writer = new PdfWriter(out);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf, PageSize.A4)) {

            // --- TÍTULO DO DOCUMENTO ---
            Paragraph titulo = new Paragraph("Relatório Detalhado da Horta")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20)
                    .setBold() // <-- CORREÇÃO: Este método existe. Se ainda der erro, há algo errado com a
                               // dependência.
                    .setMarginBottom(20);
            document.add(titulo);

            // --- TABELA DE DADOS ---
            Table table = new Table(UnitValue.createPercentArray(new float[] { 1, 2 })); // 2 colunas
            table.setWidth(UnitValue.createPercentValue(100)); // Ocupa 100% da largura

            // Adicionando linhas à tabela

            addCell(table, "Nome da Horta:", true);
            addCell(table, horta.getNomeHorta(), false);

            addCell(table, "Status Atual:", true);
            addCell(table, horta.getStatusHorta().toString(), false);

            addCell(table, "Endereço:", true);
            addCell(table, horta.getEndereco(), false);

            addCell(table, "Tamanho da Área (m²):", true);
            addCell(table, String.valueOf(horta.getTamanhoAreaProducao()), false);

            addCell(table, "Quantidade de Pessoas:", true);
            addCell(table, String.valueOf(horta.getQntPessoas()), false);

            // Tratando dados de entidades relacionadas
            if (horta.getUsuario() != null && horta.getUsuario().getPessoa() != null) {
                addCell(table, "Responsável:", true);
                addCell(table, horta.getUsuario().getPessoa().getNome(), false);
            }

            if (horta.getUnidadeDeEnsino() != null) {
                addCell(table, "Unidade de Ensino:", true);
                addCell(table, horta.getUnidadeDeEnsino().getNome(), false);
            }

            if (horta.getTipoDeHorta() != null) {
                addCell(table, "Tipo de Horta:", true);
                addCell(table, horta.getTipoDeHorta().getNome(), false); // Supondo que TipoDeHorta tem
                                                                         // getDescricao()
            }

            addCell(table, "Data de Criação:", true);
            addCell(table, horta.getDataCriacao().format(dateFormatter), false);

            document.add(table);

            // --- RODAPÉ ---
            Paragraph rodape = new Paragraph("Relatório gerado pelo sistema Flor da Cidade")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(10)
                    .setItalic()
                    .setMarginTop(50);
            document.add(rodape);

            document.close();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    // Método auxiliar para criar células e facilitar a formatação
    private void addCell(Table table, String content, boolean isHeader) {
        Cell cell = new Cell().add(new Paragraph(content));
        cell.setPadding(5);
        if (isHeader) {
            cell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
            cell.setBold();
        }
        table.addCell(cell);
    }
}
