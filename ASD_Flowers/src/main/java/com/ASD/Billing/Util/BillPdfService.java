package com.ASD.Billing.Util;

import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Component;

import com.ASD.Billing.Entity.Bill;
import com.ASD.Billing.Entity.BillItem;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Component
public class BillPdfService {

    public byte[] generateBillPdf(Bill bill) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);

            document.open();

            Font titleFont =
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 26, BaseColor.RED);

            Font headerFont =
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

            Font normalFont =
                    FontFactory.getFont(FontFactory.HELVETICA, 11);

            // Title
            Paragraph title = new Paragraph("ASD FLOWERS", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);

            document.add(title);

            document.add(new Paragraph("\n"));

            // Shop Details
            document.add(new Paragraph(
                    "PHONE NO : 9047121773 , 9047121790", headerFont));

            document.add(new Paragraph(
                    "ADDRESS : JOTHI FLOWER MARKET THIRUVANNAMALAI 606601", headerFont));

            document.add(new Paragraph(
                    "CUSTOMER : " + bill.getCustomer().getUserName(), normalFont));

            document.add(new Paragraph(
                    "VILLAGE : " + bill.getCustomer().getVillageName(), normalFont));

            document.add(new Paragraph(
                    "FLOWER : " + bill.getFlowerName(), normalFont));

            document.add(new Paragraph("\n"));

            // Table
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);

            table.addCell(headerCell("DATE"));
            table.addCell(headerCell("WEIGHT"));
            table.addCell(headerCell("RATE"));
            table.addCell(headerCell("TOTAL"));

            double grandTotal = 0;

            for (BillItem item : bill.getItems()) {

                table.addCell(bodyCell(item.getDate()));
                table.addCell(bodyCell(String.valueOf(item.getQuantity())));
                table.addCell(bodyCell(String.valueOf(item.getRate())));
                table.addCell(bodyCell(String.valueOf(item.getTotal())));

                grandTotal += item.getTotal();
            }

            document.add(table);

            document.add(new Paragraph("\n"));

            // Summary
            PdfPTable summary = new PdfPTable(2);
            summary.setWidthPercentage(50);
            summary.setHorizontalAlignment(Element.ALIGN_RIGHT);

            summary.addCell(headerCell("TOTAL"));
            summary.addCell(bodyCell(String.valueOf(grandTotal)));

            summary.addCell(headerCell("COMMISSION (10%)"));
            summary.addCell(bodyCell(String.valueOf(bill.getCommission())));

            summary.addCell(headerCell("ADVANCE"));
            summary.addCell(bodyCell(String.valueOf(bill.getAdvance())));

            summary.addCell(headerCell("FINAL AMOUNT"));
            summary.addCell(bodyCell(String.valueOf(bill.getFinalAmount())));

            document.add(summary);

            document.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return out.toByteArray();
    }

    private PdfPCell headerCell(String text) {

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

        PdfPCell cell = new PdfPCell(new Paragraph(text, font));

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);

        return cell;
    }

    private PdfPCell bodyCell(String text) {

        Font font = FontFactory.getFont(FontFactory.HELVETICA, 11);

        PdfPCell cell = new PdfPCell(new Paragraph(text, font));

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        return cell;
    }
}