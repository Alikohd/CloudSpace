package ru.oleevych.cloudspace.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.oleevych.cloudspace.dto.FileResourceDto;
import ru.oleevych.cloudspace.security.UserDetailsImpl;
import ru.oleevych.cloudspace.service.FileService;
import ru.oleevych.cloudspace.utils.UserFolderUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/drive/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;
    private final String HOME_URL_REDIRECT = "redirect:/drive/folder?path=";

    @GetMapping("/download")
    @SneakyThrows
    public ResponseEntity<Resource> downloadFile(@RequestParam String path, @AuthenticationPrincipal UserDetailsImpl user) {
        FileResourceDto fileDto = fileService.downloadFile(path, user.getUserId());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(fileDto.getContent().contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getName() + "\"")
                .body(fileDto.getContent());
    }

    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SneakyThrows
    public String uploadFile(@RequestParam("location") String location,
                             @RequestParam("file") MultipartFile file,
                             @AuthenticationPrincipal UserDetailsImpl user) {
        fileService.saveFile(file.getOriginalFilename(), file.getInputStream(), user.getUserId());
        return HOME_URL_REDIRECT + URLEncoder.encode(location, StandardCharsets.UTF_8);
    }

    @PatchMapping("rename")
    public String renameFile(@RequestParam("path") String path,
                             @RequestParam("location") String location,
                             @RequestParam("newName") String newName,
                             @AuthenticationPrincipal UserDetailsImpl user) {
        fileService.renameFile(path, newName, user.getUserId());
        return HOME_URL_REDIRECT + URLEncoder.encode(location, StandardCharsets.UTF_8);
    }


    // TODO 1) Upload for folder
    // TODO 2) Валидация путей по которым приходят файлы
//    TODO 3) Зарефакторить параметры эндпоинтов?

}
