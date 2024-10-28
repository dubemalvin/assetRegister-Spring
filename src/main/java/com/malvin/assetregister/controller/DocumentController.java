package com.malvin.assetregister.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.malvin.assetregister.dto.DocumentDto;
import com.malvin.assetregister.entity.Document;
import com.malvin.assetregister.exception.ResourceNotFoundException;
import com.malvin.assetregister.request.AddDocReq;
import com.malvin.assetregister.response.ApiResponse;
import com.malvin.assetregister.service.document.IDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/documents")
public class DocumentController {
    private final IDocumentService documentService;

    @PostMapping("/{assetId}/upload")
    public ResponseEntity<ApiResponse> save(
            @PathVariable Long assetId,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("metadata") String metadataJson) {
        try {
            // Convert metadata JSON to list of AddDocReq objects
            ObjectMapper objectMapper = new ObjectMapper();
            List<AddDocReq> requests = List.of(objectMapper.readValue(metadataJson, AddDocReq[].class));

            List<DocumentDto> docs = documentService.save(files, requests, assetId);

            return ResponseEntity.ok(new ApiResponse("success", docs));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{assetId}/get")
    public ResponseEntity<ApiResponse> getByAssetId(@PathVariable Long assetId){
        try {
            List<Document> documents = documentService.getDocumentsByAssetId(assetId);
            List<DocumentDto> dtos= documentService.convertToDtoList(documents);
            return ResponseEntity.ok(new ApiResponse("success",dtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("{docId}/delete")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long docId){
        try {
            documentService.deleteDocument(docId);
            return new ResponseEntity<>(new ApiResponse("deleted Successfully",null), HttpStatus.GONE);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), null), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{docId}/update")
    public ResponseEntity<ApiResponse> update(@PathVariable Long docId, @RequestBody AddDocReq addDocReq){
        return new ResponseEntity<>(new ApiResponse("success",null),OK);
    }

    @GetMapping("/{assetId}/by-asset")
    public ResponseEntity<ApiResponse> docsByAssetId(@PathVariable Long assetId){
        try {
            List<Document> docs = documentService.getDocumentsByAssetId(assetId);
            List<DocumentDto> dtos = documentService.convertToDtoList(docs);
            return ResponseEntity.ok(new ApiResponse("success",dtos));
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), null), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllDocs(){
        try {
            List<Document> docs = documentService.getAllDocuments();
            List<DocumentDto> dtos = documentService.convertToDtoList(docs);
            return new ResponseEntity<>(new ApiResponse("success", dtos), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(),null),INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download/{docId}")
    public ResponseEntity<Resource> downloadDoc(@PathVariable Long docId) {
        try {
            Document doc = documentService.getDocument(docId);

            // Wrap byte array directly in ByteArrayResource
            ByteArrayResource resource = new ByteArrayResource(doc.getFileData());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(doc.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getDocumentName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while downloading the document", e);
        }
    }



}
