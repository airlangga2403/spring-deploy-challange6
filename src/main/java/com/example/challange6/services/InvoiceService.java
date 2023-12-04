package com.example.challange6.services;


import com.example.challange6.dto.merchant.response.OrderDTO;
import com.example.challange6.dto.merchant.response.ReportDTO;
import com.example.challange6.dto.merchant.response.ReportItemDTO;
import com.example.challange6.models.OrderDetail;
import com.example.challange6.models.Orders;
import com.example.challange6.models.Users;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

@Service
public class InvoiceService {

    @Autowired
    private OrderService orderService;

    public byte[] generateInvoice(UUID userId) {
        List<Orders> orders = orderService.getOrdersByUserId(userId);

        if (orders == null || orders.isEmpty()) {
            return new byte[0];
        }

        Orders order = orders.get(0);
        Users user = order.getUser();

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, byteArrayOutputStream);

            document.open();

            // Add content to the PDF
            document.add(new Paragraph("Invoice for Order #" + order.getId()));
            document.add(new Paragraph("User: " + user.getUsername()));
            document.add(new Paragraph("Order Details:"));

            for (OrderDetail orderDetail : order.getOrderDetails()) {
                document.add(new Paragraph(orderDetail.getProduct().getProductName() +
                        " - Quantity: " + orderDetail.getQuantity() +
                        " - Total Price: " + orderDetail.getTotalPrice()));
            }

            document.close();

            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }


    public byte[] generateReportingMerchant(ReportDTO reportDTO) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();
            addMerchantReportContentToPdf(document, reportDTO);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }

        return byteArrayOutputStream.toByteArray();
    }

    private void addMerchantReportContentToPdf(Document document, ReportDTO reportDTO) throws DocumentException {
        // Add title
        document.add(new Paragraph("Merchant Report\n\n"));

        for (ReportItemDTO reportItemDTO : reportDTO.getReported()) {
            String header = String.format("Minggu Ke: %d\nBulan Ke: %d\nTotal Pendapatan: %.2f\n\n",
                    reportItemDTO.getWeekNumber(), reportItemDTO.getMonthNumber(), reportItemDTO.getTotalIncome());
            document.add(new Paragraph(header));
            // Add table
            PdfPTable table = new PdfPTable(6);
            table.addCell(new PdfPCell(new Paragraph("Order ID")));
            table.addCell(new PdfPCell(new Paragraph("Order Time")));
            table.addCell(new PdfPCell(new Paragraph("Destination Address")));
            table.addCell(new PdfPCell(new Paragraph("Completed")));
            table.addCell(new PdfPCell(new Paragraph("Quantity")));
            table.addCell(new PdfPCell(new Paragraph("Product Name")));

            // Iterate
            for (OrderDTO orderDTO : reportItemDTO.getOrders()) {
                table.addCell(new PdfPCell(new Paragraph(String.valueOf(orderDTO.getOrder().getId()))));
                table.addCell(new PdfPCell(new Paragraph(String.valueOf(orderDTO.getOrder().getOrder_time()))));
                table.addCell(new PdfPCell(new Paragraph(orderDTO.getOrder().getDestinationAddress())));
                table.addCell(new PdfPCell(new Paragraph(String.valueOf(orderDTO.getOrder().getCompleted()))));
                table.addCell(new PdfPCell(new Paragraph(String.valueOf(orderDTO.getOrder().getOrderDetails().get(0).getQuantity()))));
                table.addCell(new PdfPCell(new Paragraph(orderDTO.getOrder().getOrderDetails().get(0).getProduct().getProductName())));
            }

            document.add(table);
            document.add(new Paragraph("\n"));
        }
    }
}