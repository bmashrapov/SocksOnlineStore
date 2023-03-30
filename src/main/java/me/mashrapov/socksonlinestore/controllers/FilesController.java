package me.mashrapov.socksonlinestore.controllers;

import io.swagger.v3.oas.annotations.Operation;
import me.mashrapov.socksonlinestore.services.OperationsFileService;
import me.mashrapov.socksonlinestore.services.SocksFilesService;
import me.mashrapov.socksonlinestore.services.SocksService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.*;

@RestController
@RequestMapping("/files")
public class FilesController {
    private final SocksService socksService;
    private final OperationsFileService operationsFileService;
    private final SocksFilesService socksFilesService;

    public FilesController(SocksService socksService, OperationsFileService operationsFileService, SocksFilesService socksFilesService) {
        this.socksService = socksService;
        this.operationsFileService = operationsFileService;
        this.socksFilesService = socksFilesService;
    }
    @Operation(summary = "Export data file", description = "Download data file with socks")
    @GetMapping("/exportFile")
    public ResponseEntity<InputStreamResource> downloadFile() throws FileNotFoundException {
        File file = socksFilesService.getDataFile();
        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource((new FileInputStream(file)));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Socks.json\"")
                    .contentLength(file.length())
                    .body(resource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
    @Operation(summary = "Import data file", description = "Upload data file with socks")
    @PostMapping(value = "/importSocks", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadDataFileRecipes(@RequestParam @NotNull MultipartFile file) {
        socksFilesService.cleanDataFile();
        File dataFile = socksFilesService.getDataFile();
        try (FileOutputStream fos = new FileOutputStream(dataFile)){
            IOUtils.copy(file.getInputStream(),fos);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    @Operation(summary = "Export data file with operations", description = "Download data file with operations")
    @GetMapping("/exportOperations")
    public ResponseEntity<InputStreamResource> downloadFileWithOperations() throws FileNotFoundException {
        File file = operationsFileService.getDataFile();
        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource((new FileInputStream(file)));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Operations.json\"")
                    .contentLength(file.length())
                    .body(resource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
