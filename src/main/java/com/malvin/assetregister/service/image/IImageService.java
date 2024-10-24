package com.malvin.assetregister.service.image;

import com.malvin.assetregister.dto.ImageDto;
import com.malvin.assetregister.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    List<ImageDto> saveImage(List<MultipartFile> images, Long assetId);
    Image getImageById(Long imageId);
    void updateImage(Long imageId, MultipartFile image);
    void deleteImage(Long imageId);

    List<Image> getImagesByAsset(Long assetId);

    ImageDto convertToDto(Image image);

    List<ImageDto> convertToDtoList(List<Image> images);
}
