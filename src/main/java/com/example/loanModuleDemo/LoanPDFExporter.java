package com.example.loanModuleDemo;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.example.loanModuleDemo.entities.concretes.Loan;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class LoanPDFExporter {

	private List<Loan> listLoan;

	public LoanPDFExporter(List<Loan> listLoan) {
		this.listLoan = listLoan;
	}

	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(2);

		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);

		cell.setPhrase(new Phrase("Toplam Kredi Sayisi", font));

		table.addCell(cell);

		cell.setPhrase(new Phrase("Toplam Cekilen Kredilerin Ucret Miktari", font));
		table.addCell(cell);

	}

	private void writeTableData(PdfPTable table) {
		double sum = 0;
		int count = 0;
		for (Loan loan : listLoan) {
			sum += loan.getAmount();
			count++;

		}
		table.addCell(String.valueOf(count));
		table.addCell(String.valueOf(sum) + " TL");
	}

	public void export(HttpServletResponse response) throws DocumentException, IOException {
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());

		document.open();
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.BLUE);

		Paragraph p = new Paragraph("YILLIK KREDI GIRIS LISTESI", font);
		p.setAlignment(Paragraph.ALIGN_CENTER);

		document.add(p);

		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100f);
		table.setWidths(new float[] { 1.5f, 3.5f });
		table.setSpacingBefore(10);

		writeTableHeader(table);
		writeTableData(table);

		document.add(table);

		document.close();

	}

}
