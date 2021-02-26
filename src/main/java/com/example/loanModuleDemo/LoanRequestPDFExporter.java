package com.example.loanModuleDemo;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.example.loanModuleDemo.entities.concretes.LoanRequest;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

public class LoanRequestPDFExporter {
	private List<LoanRequest> listLoanRequest;

	public LoanRequestPDFExporter(List<LoanRequest> listLoanRequest) {
		this.listLoanRequest = listLoanRequest;
	}

	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(4);

		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);

		cell.setPhrase(new Phrase("Kredi Girisi Numarası", font));

		table.addCell(cell);

		cell.setPhrase(new Phrase("Musteri Numarası", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Miktar", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Geridonus", font));
		table.addCell(cell);

	}

	private void writeTableData(PdfPTable table) {
		for (LoanRequest loanRequest : listLoanRequest) {
			table.addCell(String.valueOf(loanRequest.getId()));
			table.addCell(String.valueOf(loanRequest.getCustomerId()));
			table.addCell(String.valueOf(loanRequest.getAmount()) + " TL");
			table.addCell(loanRequest.getFeedback());

		}
	}

	public void export(HttpServletResponse response) throws DocumentException, IOException {
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());

		document.open();
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.BLUE);

		Paragraph p = new Paragraph("AYLIK KREDI GIRIS LISTESI", font);
		p.setAlignment(Paragraph.ALIGN_CENTER);

		document.add(p);

		PdfPTable table = new PdfPTable(4);
		table.setWidthPercentage(100f);
		table.setWidths(new float[] { 1.5f, 3.5f, 3.0f, 3.0f });
		table.setSpacingBefore(10);

		writeTableHeader(table);
		writeTableData(table);

		document.add(table);

		document.close();

	}
}
