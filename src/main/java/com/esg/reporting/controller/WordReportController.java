
package com.esg.reporting.controller;

import com.esg.reporting.service.WordReportService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class WordReportController {

    private final WordReportService wordReportService;

    public WordReportController(WordReportService wordReportService) {
        this.wordReportService = wordReportService;
    }

    @GetMapping("/{reportId}/word")
    public ResponseEntity<byte[]> downloadWord(
            @PathVariable Long reportId) throws Exception {

        byte[] document = wordReportService.generateReport(reportId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=esg-report-" + reportId + ".docx")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                .body(document);
    }
}
