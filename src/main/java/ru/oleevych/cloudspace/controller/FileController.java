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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.oleevych.cloudspace.dto.FileDto;
import ru.oleevych.cloudspace.dto.FileMetaDto;
import ru.oleevych.cloudspace.security.UserDetailsImpl;
import ru.oleevych.cloudspace.service.FileService;
import ru.oleevych.cloudspace.utils.BreadcrumbUtils;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/drive")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;
    @GetMapping()
    public String getFiles(@RequestParam String path,
                           @RequestParam(defaultValue = "false") boolean recursive, Model model,
                           @AuthenticationPrincipal UserDetailsImpl user) {
        List<FileMetaDto> files = fileService.getFiles(
                String.format("user-%d-files/%s", user.getUserId(), path),
                recursive);
        model.addAttribute("files", files);
        model.addAttribute("breadcrumbs", BreadcrumbUtils.createBreadcrumbs(path));
        return "drive";
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String path, @AuthenticationPrincipal UserDetailsImpl user) throws IOException {
        String userPath = String.format("user-%d-files/%s", user.getUserId(), path);
        FileDto fileDto = fileService.downloadFile(userPath);
        Resource resource = new ByteArrayResource(fileDto.getContent().readAllBytes());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getName() + "\"")
                .body(resource);
    }

    // TODO Rename & Upload/Download endpoints
    // TODO Валидация путей по которым приходят файлы

}
