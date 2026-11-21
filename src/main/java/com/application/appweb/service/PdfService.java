package com.application.appweb.service;

import com.application.appweb.dto.financeiro.FinanceiroResponseDTO;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class PdfService {
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final NumberFormat MONEY = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public byte[] gerarRelatorio(String titulo, List<FinanceiroResponseDTO> registros,
                                 BigDecimal entradas, BigDecimal saidas,
                                 LocalDate inicio, LocalDate fim) throws DocumentException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter.getInstance(doc, out);
        doc.open();

        Font title = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Font normal = FontFactory.getFont(FontFactory.HELVETICA, 10);
        Font header = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);

        Paragraph p = new Paragraph(titulo, title);
        p.setAlignment(Element.ALIGN_CENTER);
        doc.add(p);
        doc.add(new Paragraph("Período: " + DATE.format(inicio) + " até " + DATE.format(fim), normal));
        doc.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1f, 3.5f, 2f, 2f, 2f});

        addHeader(table, "ID", header);
        addHeader(table, "Descrição", header);
        addHeader(table, "Valor", header);
        addHeader(table, "Tipo", header);
        addHeader(table, "Membro", header);

        for (FinanceiroResponseDTO r : registros) {
            table.addCell(String.valueOf(r.id()));
            table.addCell(r.descricao());
            table.addCell(MONEY.format(r.valor()));
            table.addCell(r.tipoRegistro().name());
            table.addCell(r.membroNome() == null ? "-" : r.membroNome());
        }

        doc.add(table);
        doc.add(Chunk.NEWLINE);
        doc.add(new Paragraph("Total de entradas: " + MONEY.format(entradas), normal));
        doc.add(new Paragraph("Total de saídas: " + MONEY.format(saidas), normal));
        doc.add(new Paragraph("Saldo: " + MONEY.format(entradas.subtract(saidas)), header));
        doc.close();
        return out.toByteArray();
    }

    private void addHeader(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setPadding(5);
        table.addCell(cell);
    }
}
