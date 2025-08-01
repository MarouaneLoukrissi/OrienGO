//package com.example.oriengo.controller;
//
//import com.example.oriengo.model.entity.Admin;
//import com.example.oriengo.model.entity.Media;
//import com.example.oriengo.service.MediaService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.net.URI;
//
//@RestController
//@RequestMapping("api/media")
//@RequiredArgsConstructor
//public class MediaController {
//    private final MediaService mediaService;
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Media> getMediaById(@PathVariable Long id){
//        Media media = mediaService.getMediaById(id);
//        return ResponseEntity.ok(media);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Media> getMediaByUserId(@PathVariable Long userId){
//        Media media = mediaService.getMediaById(userId);
//        return ResponseEntity.ok(media);
//    }
//
//    @PostMapping
//    public ResponseEntity<Media> createMedia(@RequestParam("file") MultipartFile file) {
//        Media media = mediaService.createMedia(file);
//        URI location = URI.create("/api/admin/" + media.getId());
//        return ResponseEntity.created(location).body(media);
//    }
//
//    @DeleteMapping
//    public ResponseEntity<String> deleteMedia(@RequestParam Long id) {
//        mediaService.deleteMedia(id);
//        return ResponseEntity.noContent().build();
//
//    }
//
//    @PutMapping
//    public ResponseEntity<String> updateMedia(@RequestParam String userId, @RequestParam("media") MultipartFile file) {
//        mediaService.updateMedia(userId, file);
//        return ResponseEntity.ok("Document updated successfully: ");
//    }
//}
