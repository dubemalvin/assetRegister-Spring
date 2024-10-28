package com.malvin.assetregister.service.document;

import com.malvin.assetregister.dto.DocumentDto;
import com.malvin.assetregister.entity.Document;
import com.malvin.assetregister.request.AddDocReq;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IDocumentService {
    List<DocumentDto> save(List<MultipartFile> files, List<AddDocReq> request, Long assetId);
    Document getDocument(Long docId);
    void updateDocument(Long docId, DocumentDto documentDto, MultipartFile file);
    void deleteDocument(Long docId);

    List<Document> getAllDocuments();
    List<Document> getDocumentsByAssetId(Long assetId);

    DocumentDto convertToDto(Document doc);

    List<DocumentDto> convertToDtoList(List<Document> docs);
}
