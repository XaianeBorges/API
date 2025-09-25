package com.flordacidade.api.flor_da_cidade_api.service;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;

import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import com.flordacidade.api.flor_da_cidade_api.model.Horta;
import com.flordacidade.api.flor_da_cidade_api.model.UsuarioModel;

@Service
public class PdfService {

     private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

    public ByteArrayInputStream gerarPdfHorta(Horta horta) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UsuarioModel usuario = horta.getUsuario(); 

        try (PdfWriter writer = new PdfWriter(out);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf, PageSize.A4)) {

            document.setMargins(50, 50, 50, 50); 

            // --- TÍTULO DO DOCUMENTO ---
            Paragraph titulo = new Paragraph("Relatório Detalhado da Horta")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20)
                    .setBold()
                    .setMarginBottom(30);
            document.add(titulo);

            // --- SEÇÃO 1: DADOS DA HORTA ---
            Paragraph subtituloHorta = new Paragraph("Dados da Horta")
                    .setFontSize(16)
                    .setBold()
                    .setMarginBottom(10);
            document.add(subtituloHorta);

            // Adiciona os detalhes da horta 
            addDetailParagraph(document, "Nome da Horta:", horta.getNomeHorta());
            addDetailParagraph(document, "Status Atual:", horta.getStatusHorta().toString());
            addDetailParagraph(document, "Endereço Principal:", horta.getEndereco());
            addDetailParagraph(document, "Endereço Alternativo:", horta.getEnderecoAlternativo());
            addDetailParagraph(document, "Unidade de Ensino:", horta.getUnidadeEnsino().getNome()); 
            addDetailParagraph(document, "Tipo de Horta:", horta.getTipoDeHorta().getNome()); 
            addDetailParagraph(document, "Quantidade de Pessoas Envolvidas:", String.valueOf(horta.getQntPessoas()));
            addDetailParagraph(document, "Função na Unidade de Ensino:", horta.getFuncaoUniEnsino());
            addDetailParagraph(document, "Ocupação Principal do Grupo:", horta.getOcupacaoPrincipal());
            addDetailParagraph(document, "Características do Grupo:", horta.getCaracteristicaGrupo());
            addDetailParagraph(document, "Descrição das Atividades:", horta.getAtividadeDescricao());
            addDetailParagraph(document, "Parcerias:", horta.getParceria());
            addDetailParagraph(document, "Data de Criação do Registro:", horta.getDataCriacao().format(dateTimeFormatter));
            addDetailParagraph(document, "Última Atualização:", horta.getDataAtualizacao().format(dateTimeFormatter));


            // --- SEÇÃO 2: DADOS DO RESPONSÁVEL ---
            if (usuario != null) {
                Paragraph subtituloUsuario = new Paragraph("Dados do Responsável")
                        .setFontSize(16)
                        .setBold()
                        .setMarginTop(25) 
                        .setMarginBottom(10);
                document.add(subtituloUsuario);

                addDetailParagraph(document, "Nome:", usuario.getNome());
                addDetailParagraph(document, "CPF:", usuario.getCpf());
                addDetailParagraph(document, "Email:", usuario.getEmail());
                addDetailParagraph(document, "Telefone:", usuario.getTelefone());
                addDetailParagraph(document, "Endereço:", usuario.getEndereco());
                addDetailParagraph(document, "Data de Nascimento:", usuario.getDataNascimento().format(dateFormatter));
                addDetailParagraph(document, "Escolaridade:", usuario.getEscolaridade().toString().replace("_", " "));
            }

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

    private void addDetailParagraph(Document document, String label, String value) {
        String displayValue = (value != null && !value.trim().isEmpty()) ? value : "Não informado";

        Text labelText = new Text(label + " ").setBold();
        Text valueText = new Text(displayValue);

        document.add(new Paragraph().add(labelText).add(valueText).setMarginBottom(2));
    }
}
