package com.uxdual.portal.service;

import com.uxdual.portal.dto.PaymentDto;
import com.uxdual.portal.dto.PaymentFilter;
import jakarta.inject.Singleton;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Singleton
public class ExportService {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public byte[] exportPaymentsToCsv(List<PaymentDto> payments) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PrintWriter writer = new PrintWriter(baos)) {
            
            // CSV Header
            writer.println("Payment ID,Order ID,Customer,Branch,Cashier,Amount,Method,Status,Settlement ETA,Created At");
            
            // CSV Data
            for (PaymentDto payment : payments) {
                writer.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%.2f,\"%s\",\"%s\",\"%s\",\"%s\"%n",
                    payment.getPaymentId() != null ? payment.getPaymentId() : "",
                    payment.getOrderId() != null ? payment.getOrderId() : "",
                    payment.getCustomerName() != null ? payment.getCustomerName() : "",
                    payment.getBranchName() != null ? payment.getBranchName() : "",
                    payment.getCashierName() != null ? payment.getCashierName() : "",
                    payment.getAmount() != null ? payment.getAmount().doubleValue() : 0.0,
                    payment.getMethod() != null ? payment.getMethod().getValue() : "",
                    payment.getStatus() != null ? payment.getStatus().getValue() : "",
                    payment.getSettlementEta() != null ? payment.getSettlementEta().format(DATE_FORMATTER) : "",
                    payment.getCreatedAt() != null ? payment.getCreatedAt().format(DATE_FORMATTER) : ""
                );
            }
            
            writer.flush();
            return baos.toByteArray();
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to export payments to CSV", e);
        }
    }
}
