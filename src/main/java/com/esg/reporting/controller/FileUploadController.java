
package com.esg.reporting.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.esg.reporting.entity.EsgReport;
import com.esg.reporting.entity.Evidence;
import com.esg.reporting.repository.EsgReportRepository;
import com.esg.reporting.repository.EvidenceRepository;

@RestController
@RequestMapping("/api/reports/{reportId}/evidence")
public class FileUploadController {

    private final EvidenceRepository evidenceRepository;
    private final EsgReportRepository reportRepository;

    private final Path uploadDirectory =
            Paths.get("uploads").toAbsolutePath().normalize();

    public FileUploadController(
            EvidenceRepository evidenceRepository,
            EsgReportRepository reportRepository) {
        this.evidenceRepository = evidenceRepository;
        this.reportRepository = reportRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @PathVariable Long reportId,
            @RequestParam(value = "file", required = false)
            MultipartFile file) {

        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Please select a file."));
        }

        if (file.getSize() > 10 * 1024 * 1024) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Maximum file size is 10 MB."));
        }

        String contentType = file.getContentType();

        if (contentType == null ||
                !(contentType.equals("application/pdf")
                || contentType.equals("image/png")
                || contentType.equals("image/jpeg"))) {
            return ResponseEntity.badRequest().body(
                    Map.of("error",
                            "Only PDF, PNG, and JPEG files are allowed."));
        }

        EsgReport report = reportRepository.findById(reportId)
                .orElse(null);

        if (report == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "ESG report not found."));
        }

        String originalName = file.getOriginalFilename();
        String extension = getExtension(originalName);

        String storedName = UUID.randomUUID() + extension;
        Path destination = uploadDirectory.resolve(storedName).normalize();

        if (!destination.startsWith(uploadDirectory)) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Invalid file path."));
        }

        try {
            Files.createDirectories(uploadDirectory);
            file.transferTo(destination);

            Evidence evidence = new Evidence();
            evidence.setFileName(originalName == null
                    ? storedName : originalName);
            evidence.setFileType(contentType);
            evidence.setFileLocation(storedName);
            evidence.setReport(report);

            Evidence savedEvidence = evidenceRepository.save(evidence);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    Map.of(
                            "message", "File uploaded and evidence saved.",
                            "evidenceId", savedEvidence.getId(),
                            "reportId", reportId,
                            "fileName", savedEvidence.getFileName(),
                            "storedFileName", storedName
                    ));

        } catch (IOException e) {
            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "File upload failed."));
        }
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }

        return filename.substring(filename.lastIndexOf("."))
                .toLowerCase();
    }
}
