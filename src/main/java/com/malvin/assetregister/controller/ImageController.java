package com.malvin.assetregister.controller;

import com.malvin.assetregister.dto.ImageDto;
import com.malvin.assetregister.entity.Image;
import com.malvin.assetregister.exception.ResourceNotFoundException;
import com.malvin.assetregister.response.ApiResponse;
import com.malvin.assetregister.service.image.IImageService;
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

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {
    private final IImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long assetId){
        try {
            List<ImageDto> images = imageService.saveImage(files, assetId);
            return ResponseEntity.ok(new ApiResponse("upload success",images));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("upload Failed", e.getMessage()));
        }
    }
    @DeleteMapping("/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId){
        try {
            imageService.deleteImage(imageId);
            return ResponseEntity.ok(new ApiResponse("Image deleted Successfully",null));
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(),null), NOT_FOUND);
        }
    }
    @GetMapping("/download/{imageId}")
    public ResponseEntity<Resource> getImage(@PathVariable Long imageId){
        try {
            Image image = imageService.getImageById(imageId);
            ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1,(int) image.getImage().length()));
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName()+"\"")
                    .body(resource);
        } catch (Exception e) {
                throw new RuntimeException();
        }
    }
    @PutMapping("/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestParam MultipartFile file){
        try {
            imageService.updateImage(imageId,file);
            return ResponseEntity.ok(new ApiResponse("Image Updated Successfully",null));
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(),null), NOT_FOUND);
        }
    }
    @GetMapping("{assetId}/images")
    public ResponseEntity<ApiResponse> getImages(@PathVariable Long assetId){
        try {
            List<Image> images = imageService.getImagesByAsset(assetId);
            return ResponseEntity.ok(new ApiResponse("Images Retrieved Successfully",images));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
}
