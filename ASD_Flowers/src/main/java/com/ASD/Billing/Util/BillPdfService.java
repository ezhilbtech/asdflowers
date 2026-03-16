package com.ASD.Billing.Util;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ASD.Billing.Entity.Bill;
import com.ASD.Billing.Entity.BillItem;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Component
public class BillPdfService {

    DecimalFormat df = new DecimalFormat("₹0");

    public byte[] generateBillPdf(Bill bill) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            Rectangle pageSize = new Rectangle(400,800);
            Document document = new Document(pageSize,25,25,25,25);

            PdfWriter writer = PdfWriter.getInstance(document,out);
            document.open();

            /* -------- BORDER -------- */

            PdfContentByte canvas = writer.getDirectContent();

            canvas.setColorStroke(BaseColor.RED);
            canvas.setLineWidth(3);
            canvas.rectangle(10,10,400,575);
            canvas.stroke();

            canvas.setLineWidth(1.5f);
            canvas.rectangle(15,15,390,565);
            canvas.stroke();

            /* -------- FONTS -------- */

            Font redFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD,12,BaseColor.RED);
            Font blueFont = FontFactory.getFont(FontFactory.HELVETICA,12,BaseColor.BLUE);
            Font bold = FontFactory.getFont(FontFactory.HELVETICA_BOLD,12,BaseColor.BLUE);
            Font title = FontFactory.getFont(FontFactory.HELVETICA_BOLD,60,BaseColor.RED);
            Font ownerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD,12,BaseColor.RED);

            /* -------- MOBILE NUMBERS -------- */

            PdfPTable phoneTable = new PdfPTable(2);
            phoneTable.setWidthPercentage(100);

            PdfPCell leftPhone = new PdfPCell(new Phrase("Cell : D.9047121790",bold));
            PdfPCell rightPhone = new PdfPCell(new Phrase("Cell : S.7339198360\n          9047121773",bold));

            leftPhone.setBorder(Rectangle.NO_BORDER);
            rightPhone.setBorder(Rectangle.NO_BORDER);

            leftPhone.setHorizontalAlignment(Element.ALIGN_LEFT);
            rightPhone.setHorizontalAlignment(Element.ALIGN_RIGHT);

            phoneTable.addCell(leftPhone);
            phoneTable.addCell(rightPhone);

            document.add(phoneTable);

            /* -------- PILLAIYAR -------- */

            Image pillaiyar =
                    Image.getInstance(getClass().getClassLoader()
                            .getResource("/Pillayar.png"));

            pillaiyar.scaleAbsolute(25,25);
            pillaiyar.setAlignment(Image.ALIGN_CENTER);

            document.add(pillaiyar);

            /* -------- TITLE -------- */

            Paragraph murugan = new Paragraph("SRI MURUGAN THUNAI",bold);
            murugan.setAlignment(Element.ALIGN_CENTER);
            document.add(murugan);

            /* -------- HEADER -------- */

            PdfPTable header = new PdfPTable(3);
            header.setWidthPercentage(100);
            header.setWidths(new int[]{2,3,2});

            Image annamalaiyar =
                    Image.getInstance(getClass().getClassLoader()
                            .getResource("/annamalaiyar.png"));

            annamalaiyar.scaleAbsolute(70,70);

            PdfPCell leftImg = new PdfPCell(annamalaiyar);
            leftImg.setBorder(Rectangle.NO_BORDER);

            PdfPCell asd = new PdfPCell(new Phrase("ASD",title));
            asd.setBorder(Rectangle.NO_BORDER);
            asd.setHorizontalAlignment(Element.ALIGN_CENTER);

            Image muruganImg =
                    Image.getInstance(getClass().getClassLoader()
                            .getResource("/murugan.png"));

            muruganImg.scaleAbsolute(85,85);

            PdfPCell rightImg = new PdfPCell(muruganImg);
            rightImg.setBorder(Rectangle.NO_BORDER);
            rightImg.setHorizontalAlignment(Element.ALIGN_RIGHT);

            header.addCell(leftImg);
            header.addCell(asd);
            header.addCell(rightImg);

            document.add(header);

            /* -------- ADDRESS -------- */

            Paragraph address = new Paragraph(
                    "183, Annamalaiyar Flower Market\nThiruvannamalai - 606601",
                    bold);

            address.setAlignment(Element.ALIGN_CENTER);
            document.add(address);

            document.add(new Paragraph(" "));

            /* -------- OWNERS BOX (MOVED HERE) -------- */

            PdfPTable ownerTable = new PdfPTable(1);
            ownerTable.setWidthPercentage(65);
            ownerTable.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell owner =
                    new PdfPCell(new Phrase("Owners : M.Selvam , M.Dinakaran",ownerFont));

            owner.setHorizontalAlignment(Element.ALIGN_CENTER);
            owner.setBorderColor(BaseColor.RED);
            owner.setBorderWidth(2);
            owner.setPaddingTop(8);
            owner.setPaddingBottom(8);

            ownerTable.addCell(owner);

            document.add(ownerTable);

            document.add(new Paragraph(" "));

            /* -------- CUSTOMER DETAILS -------- */

            document.add(new Paragraph("Customer : "+bill.getCustomer().getUserName(),redFont));
            document.add(new Paragraph("Village : "+bill.getCustomer().getVillageName(),redFont));
            document.add(new Paragraph("Flower : "+bill.getFlowerName(),redFont));

            document.add(new Paragraph(" "));

            /* -------- ITEMS TABLE -------- */

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{1,2,2,2,2,2});

            table.addCell(headerCell("No"));
            table.addCell(headerCell("Date"));
            table.addCell(headerCell("Weight"));
            table.addCell(headerCell("Rate"));
            table.addCell(headerCell("Borrow"));
            table.addCell(headerCell("Total"));

            int i = 1;
            double grandTotal = 0;
            double borrowTotal = 0;

            List<BillItem> items = bill.getItems();

            for(BillItem item : items){

                table.addCell(bodyCell(String.valueOf(i++)));
                table.addCell(bodyCell(item.getDate()));
                table.addCell(bodyCell(String.valueOf(item.getQuantity())));
                table.addCell(bodyCell(df.format(item.getRate())));
                table.addCell(bodyCell(df.format(item.getBorrow())));
                table.addCell(bodyCell(df.format(item.getTotal())));

                grandTotal += item.getTotal();
                borrowTotal += item.getBorrow();
            }

            document.add(table);

            document.add(new Paragraph(" "));

            /* -------- TOTAL TABLE -------- */

            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(45);
            totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

            totalTable.addCell(totalCell("TOTAL"));
            totalTable.addCell(totalCell(df.format(grandTotal)));

            totalTable.addCell(totalCell("COMMISSION"));
            totalTable.addCell(totalCell(df.format(bill.getCommission())));

            totalTable.addCell(totalCell("BORROW"));
            totalTable.addCell(totalCell(df.format(borrowTotal)));

            totalTable.addCell(totalCell("FINAL AMOUNT"));
            totalTable.addCell(totalCell(df.format(bill.getFinalAmount())));

            document.add(totalTable);

            document.close();

        } catch(Exception e){
            e.printStackTrace();
        }

        return out.toByteArray();
    }

    private PdfPCell headerCell(String text){

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD,12,BaseColor.RED);

        PdfPCell cell = new PdfPCell(new Phrase(text,font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderColor(BaseColor.RED);

        return cell;
    }

    private PdfPCell bodyCell(String text){

        Font font = FontFactory.getFont(FontFactory.HELVETICA,12,BaseColor.BLUE);

        PdfPCell cell = new PdfPCell(new Phrase(text,font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderColor(BaseColor.RED);

        return cell;
    }

    private PdfPCell totalCell(String text){

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD,12,BaseColor.RED);

        PdfPCell cell = new PdfPCell(new Phrase(text,font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderColor(BaseColor.RED);

        return cell;
    }
}
