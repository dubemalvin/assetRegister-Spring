package com.malvin.assetregister.service.image;

import com.malvin.assetregister.dto.ImageDto;
import com.malvin.assetregister.entity.Asset;
import com.malvin.assetregister.entity.Image;
import com.malvin.assetregister.exception.ResourceNotFoundException;
import com.malvin.assetregister.repository.ImageRepository;
import com.malvin.assetregister.service.asset.IAssetService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ImageService implements  IImageService{
    private final ImageRepository imageRepository;
    private final IAssetService assetService;
    private final ModelMapper modelMapper;

    @Override
    public List<ImageDto> saveImage(List<MultipartFile> files, Long assetId) {
        Asset asset = assetService.getAssetById(assetId);
        List<ImageDto> imageList = new ArrayList<>();
        files.forEach(file->{
            try {
                Image image = new Image();
                image.setAsset(asset);
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));

               String downloadUrl = "api/v1/images/download"+image.getId() ;
                image.setDownloadUrl(downloadUrl);
                Image savedImage = imageRepository.save(image);
                savedImage.setDownloadUrl("/api/v1/images/download/" +savedImage.getId());
                imageRepository.save(savedImage);

                ImageDto dto =new ImageDto();
                dto.setImageId(savedImage.getId());
                dto.setDownloadUrl(savedImage.getDownloadUrl());
                dto.setFileName(file.getOriginalFilename());
                imageList.add(dto);
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        });

        return imageList;
    }

    @Override
    public Image getImageById(Long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Oops! Image not found"));
    }

    @Override
    public void updateImage(Long imageId, MultipartFile file) {
        Image image = getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteImage(Long imageId) {
        imageRepository.findById(imageId).ifPresentOrElse(imageRepository::delete,
                ()->{throw  new ResourceNotFoundException("Oops! Resource Not Found");
        });
    }

    @Override
    public List<Image> getImagesByAsset(Long assetId) {
        Asset asset = assetService.getAssetById(assetId);
        return asset.getImage();
    }

    @Override
    public ImageDto convertToDto(Image image){
        return modelMapper.map(image, ImageDto.class);
    }

    @Override
    public List<ImageDto> convertToDtoList(List<Image> images){
        return images
                .stream()
                .map(this::convertToDto)
                .toList();
    }
}
