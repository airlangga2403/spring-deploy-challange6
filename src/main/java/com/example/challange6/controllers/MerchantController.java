package com.example.challange6.controllers;


import com.example.challange6.dto.merchant.request.AddMerchantRequestDTO;
import com.example.challange6.dto.merchant.request.UpdateMerchantRequestDTO;
import com.example.challange6.dto.merchant.response.AddMerchantResponseDTO;
import com.example.challange6.dto.merchant.response.ReportDTO;
import com.example.challange6.dto.merchant.response.UpdateMerchantResponseDTO;
import com.example.challange6.models.Merchants;
import com.example.challange6.services.InvoiceService;
import com.example.challange6.services.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@Slf4j
@RequestMapping("api/merchant")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/add")
    public AddMerchantResponseDTO addMerchant(@RequestBody AddMerchantRequestDTO requestDTO) {
        log.info("Received a request to add a merchant.");
        AddMerchantResponseDTO responseDTO = merchantService.addMerchant(requestDTO);

        log.info("Response from addMerchant: {}", responseDTO);
        return responseDTO;
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> updateMerchant(@PathVariable UUID id, @RequestBody UpdateMerchantRequestDTO updateMerchantDTO) {
        log.info("Received a request to update a merchant with ID: {}", id);
        UpdateMerchantResponseDTO updatedMerchant = merchantService.updateMerchant(id, updateMerchantDTO);
        log.info("Response from updateMerchant: {}", updatedMerchant);
        if (updatedMerchant != null) {
            return new ResponseEntity<>(updatedMerchant, HttpStatus.OK);
        } else {
            log.error("Unable to update merchant with ID: {}", id);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("uid", id);
            errorResponse.put("message", "UID tidak valid");
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping()
    public ResponseEntity<?> getOpenMerchant() {
        log.info("Received a request to get open merchants.");
        List<Merchants> merchants = merchantService.getMerchantOpen();
        if (merchants != null) {
            log.info("Response from getOpenMerchant: {}", merchants);
            return new ResponseEntity<>(merchants, HttpStatus.OK);
        } else {
            log.error("No open merchants found.");
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "tidak ada merchants yang open");
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    // Get
    @GetMapping("/report-merchant")
    public ResponseEntity<?> getReportMerchant() {
        ReportDTO getReport = merchantService.getReport();

        if (getReport.getReported().isEmpty()) {
            log.error("No open merchants found.");
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Tidak ada Merchant");
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(getReport, HttpStatus.OK);
        }
    }

    // Generate PDF
    @GetMapping("/generate")
    public ResponseEntity<byte[]> generatePdf() {
        ReportDTO reportDTO = merchantService.getReport()/* Call your getReport method here */;
        byte[] pdfBytes = invoiceService.generateReportingMerchant(reportDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "invoice.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }


}
