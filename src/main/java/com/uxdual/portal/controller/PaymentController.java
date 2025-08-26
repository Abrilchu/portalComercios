package com.uxdual.portal.controller;

import com.uxdual.portal.dto.PaymentDto;
import com.uxdual.portal.dto.PaymentFilter;
import com.uxdual.portal.service.ExportService;
import com.uxdual.portal.service.PaymentService;
import io.micronaut.data.model.Page;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;

import java.time.LocalDateTime;
import java.util.List;

@Controller("/api/payments")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class PaymentController {
    
    private final PaymentService paymentService;
    private final ExportService exportService;
    
    public PaymentController(PaymentService paymentService, ExportService exportService) {
        this.paymentService = paymentService;
        this.exportService = exportService;
    }
    
    @Get
    public Page<PaymentDto> getPayments(@QueryValue PaymentFilter filter, Authentication authentication) {
        List<Long> branchIds = (List<Long>) authentication.getAttributes().get("branchIds");
        List<Long> cashierIds = (List<Long>) authentication.getAttributes().get("cashierIds");
        
        return paymentService.getPayments(filter, branchIds, cashierIds);
    }
    
    @Get("/updates")
    public List<PaymentDto> getUpdatedPayments(@QueryValue LocalDateTime sinceCursor, Authentication authentication) {
        List<Long> branchIds = (List<Long>) authentication.getAttributes().get("branchIds");
        List<Long> cashierIds = (List<Long>) authentication.getAttributes().get("cashierIds");
        
        return paymentService.getUpdatedPayments(sinceCursor, branchIds, cashierIds);
    }
    
    @Get("/{id}")
    public PaymentDto getPaymentDetail(@PathVariable Long id) {
        return paymentService.getPaymentDetail(id);
    }
    
    @Get("/export/csv")
    public HttpResponse<byte[]> exportPaymentsCsv(@QueryValue PaymentFilter filter, Authentication authentication) {
        List<Long> branchIds = (List<Long>) authentication.getAttributes().get("branchIds");
        List<Long> cashierIds = (List<Long>) authentication.getAttributes().get("cashierIds");
        
        Page<PaymentDto> paymentsPage = paymentService.getPayments(filter, branchIds, cashierIds);
        List<PaymentDto> payments = paymentsPage.getContent();
        
        byte[] csvData = exportService.exportPaymentsToCsv(payments);
        
        return HttpResponse.ok(csvData)
            .contentType(MediaType.TEXT_CSV)
            .header("Content-Disposition", "attachment; filename=payments.csv");
    }
}
