package com.example.oriengo.controller;

import com.example.oriengo.mapper.MediaMapper;
import com.example.oriengo.model.dto.MediaFilteredResponseDTO;
import com.example.oriengo.model.dto.MediaRequestDTO;
import com.example.oriengo.model.dto.MediaResponseDTO;
import com.example.oriengo.model.entity.Media;
import com.example.oriengo.payload.response.ApiResponse;
import com.example.oriengo.service.MediaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;
    private final MediaMapper mediaMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MediaResponseDTO>>> getAllMedia() {
        List<Media> medias = mediaService.getAllMedias();
        return ResponseEntity.ok(ApiResponse.<List<MediaResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("Media files fetched successfully")
                .data(mediaMapper.toDTO(medias))
                .build());
    }

    // 2. Get media metadata by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MediaResponseDTO>> getMediaById(@PathVariable Long id) {
        Media media = mediaService.getMediaById(id);
        return ResponseEntity.ok(ApiResponse.<MediaResponseDTO>builder()
                .code("SUCCESS")
                .status(200)
                .message("Media fetched successfully")
                .data(mediaMapper.toDTO(media))
                .build());
    }

    @GetMapping("/user/latest/{userId}")
    public ResponseEntity<ApiResponse<List<MediaFilteredResponseDTO>>> getLatestMediaByUserId(@PathVariable Long userId) {
        List<Media> medias = mediaService.getLatestMediaByUserId(userId);
        return ResponseEntity.ok(ApiResponse.<List<MediaFilteredResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("User's media files fetched successfully")
                .data(mediaMapper.toFilteredDTO(medias))
                .build());
    }

    // 3. Get media list by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<MediaResponseDTO>>> getMediaByUserId(@PathVariable Long userId) {
        List<Media> medias = mediaService.getMediaByUserId(userId);
        return ResponseEntity.ok(ApiResponse.<List<MediaResponseDTO>>builder()
                .code("SUCCESS")
                .status(200)
                .message("User's media files fetched successfully")
                .data(mediaMapper.toDTO(medias))
                .build());
    }

    // 4. Create media: upload file + metadata (mediaType, userId)
    @PostMapping
    public ResponseEntity<ApiResponse<MediaResponseDTO>> createMedia(@ModelAttribute @Valid MediaRequestDTO dto) {
        Media media = mediaService.createMedia(dto.getMedia(), dto.getUserId(), dto.getType());
        return ResponseEntity.created(URI.create("/api/media/" + media.getId()))
                .body(ApiResponse.<MediaResponseDTO>builder()
                        .code("SUCCESS")
                        .status(201)
                        .message("Media created successfully")
                        .data(mediaMapper.toDTO(media))
                        .build());
    }

    // 5. Update media (file + mediaType)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MediaResponseDTO>> updateMedia(
            @PathVariable Long id,
            @ModelAttribute @Valid MediaRequestDTO dto
    ) {
        Media updatedMedia = mediaService.updateMedia(id, dto.getMedia(), dto.getType());
        return ResponseEntity.ok(ApiResponse.<MediaResponseDTO>builder()
                .code("MEDIA_UPDATED")
                .status(200)
                .message("Media updated successfully")
                .data(mediaMapper.toDTO(updatedMedia))
                .build());
    }

    // 6. Delete media by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMedia(@PathVariable Long id) {
        mediaService.deleteMedia(id);
        return ResponseEntity.status(204).body(ApiResponse.<Void>builder()
                .code("MEDIA_DELETED")
                .status(204)
                .message("Media deleted successfully")
                .build());
    }

    // 7. Get media file content (binary) for displaying/downloading
    @GetMapping("/file/{id}")
    public ResponseEntity<Resource> getMediaFileById(@PathVariable("id") Long mediaId) {
        return mediaService.getMediaFileById(mediaId);
    }
}
