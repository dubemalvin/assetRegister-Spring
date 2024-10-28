package com.malvin.assetregister.service.document;

import com.malvin.assetregister.dto.DocumentDto;
import com.malvin.assetregister.dto.ImageDto;
import com.malvin.assetregister.entity.Document;
import com.malvin.assetregister.entity.Image;
import com.malvin.assetregister.exception.ResourceNotFoundException;
import com.malvin.assetregister.repository.DocumentRepository;
import com.malvin.assetregister.request.AddDocReq;
import com.malvin.assetregister.service.asset.IAssetService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DocumentService implements IDocumentService {
    private final DocumentRepository documentRepository;
    private final IAssetService assetService;
    private final ModelMapper modelMapper;

    @Override
    public List<DocumentDto> save(List<MultipartFile> files, List<AddDocReq> requests, Long assetId) {
        List<DocumentDto> documentDtos = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            try {
                MultipartFile file = files.get(i);
                AddDocReq request = requests.get(i);

                Document document = new Document();
                document.setDocumentName(request.getDocumentName());
                document.setDescription(request.getDescription());
                document.setDocumentType(request.getDocumentType());
                document.setIssueDate(request.getIssueDate());
                document.setExpiryDate(request.getExpiryDate());
                document.setFileType(file.getContentType());
                document.setLifetime(request.isLifetime());
                document.setFileData(file.getBytes());
                document.setAsset(assetService.getAssetById(assetId)); // Assuming Asset entity has an ID field

                String downloadUrl = "api/v1/documents/download"+document.getId() ;
                document.setDownloadUrl(downloadUrl);
                Document  savedDoc = documentRepository.save(document);
                savedDoc.setDownloadUrl("/api/v1/documents/download/" +savedDoc.getId());

                documentRepository.save(document);

                DocumentDto dto =new DocumentDto();
                dto.setDocId(savedDoc.getId());
                dto.setDownloadUrl(savedDoc.getDownloadUrl());
                dto.setDocumentName(file.getOriginalFilename());
                documentDtos.add(dto);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return documentDtos;
    }

    @Override
    public Document getDocument(Long docId) {
        return documentRepository.findById(docId)
                .orElseThrow(()-> new ResourceNotFoundException("Document not found"));
    }

    @Override
    public void updateDocument(Long docId, DocumentDto documentDto, MultipartFile file) {

    }

    @Override
    public void deleteDocument(Long docId) {
        documentRepository.findById(docId)
                .ifPresentOrElse(documentRepository::delete,
                        ()-> {throw new ResourceNotFoundException("Oops! Document not found");
                });
    }

    @Override
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    @Override
    public List<Document> getDocumentsByAssetId(Long assetId) {
        return assetService.getAssetById(assetId).getDocuments().stream().toList();
    }

    @Override
    public DocumentDto convertToDto(Document doc){
        return modelMapper.map(doc,DocumentDto.class);
    }

    @Override
    public List<DocumentDto> convertToDtoList(List<Document> docs){
        return docs.stream().map(this::convertToDto).toList();
    }
}
