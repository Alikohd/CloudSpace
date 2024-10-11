package ru.oleevych.cloudspace.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.oleevych.cloudspace.dto.FileDto;
import ru.oleevych.cloudspace.dto.FileMetaDto;
import ru.oleevych.cloudspace.security.UserDetailsImpl;
import ru.oleevych.cloudspace.service.FileService;
import ru.oleevych.cloudspace.utils.BreadcrumbUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/drive/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;
    private final String USER_FILES_PATTERN = "user-%d-files/%s";
    private final String HOME_URL_REDIRECT = "redirect:/drive/folder?path=";

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String path, @AuthenticationPrincipal UserDetailsImpl user) throws IOException {
        String userPath = String.format(USER_FILES_PATTERN, user.getUserId(), path);
        FileDto fileDto = fileService.downloadFile(userPath);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(fileDto.getContent().contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getName() + "\"")
                .body(fileDto.getContent());
    }

    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFile(@RequestParam("location") String location,
                             @RequestParam("file") MultipartFile file,
                             @AuthenticationPrincipal UserDetailsImpl user) throws IOException {
        String userPath = String.format(USER_FILES_PATTERN, user.getUserId(), location);
        fileService.saveFile(userPath + file.getOriginalFilename(), file.getInputStream());
        return HOME_URL_REDIRECT + URLEncoder.encode(location, StandardCharsets.UTF_8);
    }

    @PatchMapping("rename")
    public String renameFile(@RequestParam("path") String path,
                             @RequestParam("location") String location,
                             @RequestParam("newName") String newName,
                             @AuthenticationPrincipal UserDetailsImpl user) {
        String userPath = String.format(USER_FILES_PATTERN, user.getUserId(), path);
        fileService.renameFile(userPath, newName);
        return HOME_URL_REDIRECT + URLEncoder.encode(location, StandardCharsets.UTF_8);
    }


    // TODO Rename & Upload/Download endpoints
    // TODO Валидация путей по которым приходят файлы

}
