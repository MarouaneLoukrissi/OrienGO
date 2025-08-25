package com.example.oriengo.service;

import com.example.oriengo.exception.custom.Media.MediaCreationException;
import com.example.oriengo.exception.custom.Media.MediaDeleteException;
import com.example.oriengo.exception.custom.Media.MediaGetException;
import com.example.oriengo.exception.custom.Media.MediaUpdateException;
import com.example.oriengo.exception.custom.PathVarException;
import com.example.oriengo.mapper.MediaMapper;
import com.example.oriengo.model.entity.Media;
import com.example.oriengo.model.entity.User;
import com.example.oriengo.model.enumeration.MediaType;
import com.example.oriengo.repository.MediaRepository;
import com.example.oriengo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaService {

    @org.springframework.beans.factory.annotation.Value("${media.upload-cover-image-dir}")
    private String uploadCoverImageDir;

    @org.springframework.beans.factory.annotation.Value("${media.upload-profile-image-dir}")
    private String uploadProfileImageDir;

    @org.springframework.beans.factory.annotation.Value("${media.upload-pdf-dir}")
    private String uploadPdfDir;

    private static final String validPdfFileType = "pdf";
    private static final List<String> validImageFileTypes = List.of("jpeg", "jpg", "png");

    private final MediaRepository mediaRepository;
    private final UserRepository userRepository;
    private final MediaMapper mediaMapper;
    private final MessageSource messageSource;

    private String getMessage(String key, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, args, locale);
    }

    public List<Media> getAllMedias() {
        try {
            log.info("Fetching all media files");
            List<Media> medias = mediaRepository.findAll();
            log.info("Found {} media files", medias.size());
            return medias;
        } catch (Exception e) {
            log.error("Error fetching media files: {}", e.getMessage(), e);
            throw new MediaGetException(HttpStatus.NOT_FOUND, getMessage("media.not.found"));
        }
    }

    public Media getMediaById(Long id) {
        if (id == null) {
            log.warn("Media ID is null");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("media.id.empty"));
        }
        return mediaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Media not found with ID: {}", id);
                    return new MediaGetException(HttpStatus.NOT_FOUND, getMessage("media.not.found"));
                });
    }

    public List<Media> getMediaByUserId(Long userId) {
        if (userId == null) {
            log.warn("User ID is null");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("media.user.id.empty"));
        }
        try {
            return mediaRepository.findByUserId(userId);
        } catch (Exception e) {
            log.error("Error getting media by user ID {}: {}", userId, e.getMessage());
            throw new MediaGetException(HttpStatus.BAD_REQUEST, getMessage("media.by.user.failed", userId));
        }
    }

    public List<Media> getLatestMediaByUserId(Long userId) {
        if (userId == null) {
            log.warn("User ID is null");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("media.user.id.empty"));
        }
        try {
            Optional<Media> latestCover = mediaRepository.findFirstByUserIdAndTypeOrderByCreatedAtDesc(userId, MediaType.COVER_PHOTO);
            Optional<Media> latestProfile = mediaRepository.findFirstByUserIdAndTypeOrderByCreatedAtDesc(userId, MediaType.PROFILE_PHOTO);

            return Stream.of(latestCover, latestProfile)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
        } catch (Exception e) {
            log.error("Error getting media by user ID {}: {}", userId, e.getMessage());
            throw new MediaGetException(HttpStatus.BAD_REQUEST, getMessage("media.by.user.failed", userId));
        }
    }

    @Transactional
    public Media createMedia(MultipartFile file, Long userId, MediaType mediaType) {
        if (file.isEmpty()) {
            throw new MediaCreationException(HttpStatus.BAD_REQUEST, getMessage("media.file.empty"));
        }

        // Validate media type and get path
        Path uploadPath = resolveUploadPath(mediaType);

        // Extract file metadata
        String originalFileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        if (contentType == null) {
            throw new MediaCreationException(HttpStatus.BAD_REQUEST, getMessage("media.contentType.null"));
        }
        long size = file.getSize();

        // Validate content type format
        String[] parts = contentType.split("/");
        if (parts.length < 2) {
            throw new MediaCreationException(HttpStatus.BAD_REQUEST, getMessage("media.contentType.invalid"));
        }
        String extension = parts[1].toLowerCase();

        // Validate content type according to mediaType
        validateContentType(contentType, mediaType);

        // Generate unique file name
        String uniqueFileName = UUID.randomUUID() + "." + extension;
        Path fullPath = uploadPath.resolve(uniqueFileName);

        // Build URL path
        String url = buildFileUrl(mediaType, uniqueFileName);

        // Fetch user
        User user = userRepository.findByIdAndIsDeleted(userId, false).orElseThrow(() -> {
            log.error("User not found with ID: {}", userId);
            return new MediaCreationException(HttpStatus.NOT_FOUND, getMessage("media.user.not.found"));
        });

        // Create media entity
        Media media = new Media();
        media.setName(uniqueFileName);
        media.setContentType(contentType);
        media.setSize(size);
        media.setType(mediaType);
        media.setUrl(url);
        media.setUser(user);

        // Persist media
        Media savedMedia = mediaRepository.save(media);

        // Save file physically
        try {
            Files.copy(file.getInputStream(), fullPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Failed to store file: {}", e.getMessage(), e);
            throw new MediaCreationException(HttpStatus.CONFLICT, getMessage("media.upload.failed"));
        }

        return savedMedia;
    }

    private Path resolveUploadPath(MediaType mediaType) {
        return switch (mediaType) {
            case COVER_PHOTO -> Paths.get(uploadCoverImageDir);
            case PROFILE_PHOTO -> Paths.get(uploadProfileImageDir);
            case PROFILE_PDF, RESULT_PDF -> Paths.get(uploadPdfDir);
            default -> throw new MediaCreationException(
                    HttpStatus.BAD_REQUEST, getMessage("media.mediaType.not.found"));
        };
    }

    private void validateContentType(String contentType, MediaType mediaType) {
        String type = contentType.split("/")[1].toLowerCase();

        if (isImageType(mediaType)) {
            if (!validImageFileTypes.contains(type)) {
                throw new MediaCreationException(HttpStatus.BAD_REQUEST,
                        getMessage("media.invalid.image.type"));
            }
        } else if (isPdfType(mediaType)) {
            if (!validPdfFileType.equals(type)) {
                throw new MediaCreationException(HttpStatus.BAD_REQUEST,
                        getMessage("media.invalid.pdf.type"));
            }
        }
    }

    private boolean isImageType(MediaType mediaType) {
        return mediaType == MediaType.COVER_PHOTO || mediaType == MediaType.PROFILE_PHOTO;
    }

    private boolean isPdfType(MediaType mediaType) {
        return mediaType == MediaType.PROFILE_PDF || mediaType == MediaType.RESULT_PDF;
    }

    private String buildFileUrl(MediaType mediaType, String fileName) {
        return switch (mediaType) {
            case COVER_PHOTO -> "/uploads/images/cover/" + fileName;
            case PROFILE_PHOTO -> "/uploads/images/profile/" + fileName;
            case PROFILE_PDF, RESULT_PDF -> "/uploads/pdfs/" + fileName;
            default -> throw new MediaCreationException(
                    HttpStatus.BAD_REQUEST, getMessage("media.mediaType.not.found"));
        };
    }

    @Transactional
    public void deleteMedia(Long id) {
        if (id == null) {
            log.warn("Media ID is null for deletion");
            throw new PathVarException(HttpStatus.BAD_REQUEST, getMessage("media.id.empty"));
        }
        try {
            Media media = getMediaById(id);
            mediaRepository.delete(media);
            log.info("Media deleted with ID: {}", media.getId());
        } catch (Exception e) {
            log.error("Error deleting media: {}", e.getMessage());
            throw new MediaDeleteException(HttpStatus.CONFLICT, getMessage("media.delete.failed"));
        }
    }

    @Transactional
    public Media updateMedia(Long mediaId, MultipartFile newFile, MediaType newMediaType) {
        if (newFile.isEmpty()) {
            throw new MediaUpdateException(HttpStatus.BAD_REQUEST, getMessage("media.file.empty"));
        }

        Media media = mediaRepository.findById(mediaId)
            .orElseThrow(() -> {
                log.error("Media not found with ID: {}", mediaId);
                return new MediaUpdateException(HttpStatus.NOT_FOUND, getMessage("media.not.found"));
            });

        Path uploadPath = resolveUploadPath(newMediaType);

        String contentType = newFile.getContentType();
        if (contentType == null) {
            throw new MediaUpdateException(HttpStatus.BAD_REQUEST, getMessage("media.contentType.null"));
        }

        String[] parts = contentType.split("/");
        if (parts.length < 2) {
            throw new MediaUpdateException(HttpStatus.BAD_REQUEST, getMessage("media.contentType.invalid"));
        }
        String extension = parts[1].toLowerCase();

        validateContentType(contentType, newMediaType);

        String newFileName = UUID.randomUUID() + "." + extension;
        Path newFilePath = uploadPath.resolve(newFileName);

        // Delete old file
        try {
            Path oldFilePath = resolveUploadPath(media.getType()).resolve(media.getName());
            Files.deleteIfExists(oldFilePath);
        } catch (IOException e) {
            log.warn("Failed to delete old file: {}", e.getMessage());
        }

        // Save new file
        try {
            Files.copy(newFile.getInputStream(), newFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new MediaUpdateException(HttpStatus.CONFLICT, getMessage("media.upload.failed"));
        }

        media.setName(newFileName);
        media.setContentType(contentType);
        media.setSize(newFile.getSize());
        media.setType(newMediaType);
        media.setUrl(buildFileUrl(newMediaType, newFileName));

        return mediaRepository.save(media);
    }

    public ResponseEntity<Resource> getMediaFileById(Long mediaId) {
        Media media = mediaRepository.findById(mediaId)
            .orElseThrow(() -> {
                log.error("Media not found with ID: {}", mediaId);
                throw new MediaGetException(HttpStatus.NOT_FOUND, getMessage("media.not.found"));
            });

        Path filePath = resolveUploadPath(media.getType()).resolve(media.getName());

        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new MediaGetException(HttpStatus.NOT_FOUND, getMessage("media.unreadable.file"));
            }

            String contentType = media.getContentType();
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + media.getName() + "\"")
                .body(resource);

        } catch (MalformedURLException e) {
            log.error("File loading error: {}", e.getMessage());
            throw new MediaGetException(HttpStatus.INTERNAL_SERVER_ERROR, getMessage("media.load.failed"));
        }
    }
}
