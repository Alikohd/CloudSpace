package ru.oleevych.cloudspace.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.oleevych.cloudspace.dto.FileMetaDto;
import ru.oleevych.cloudspace.security.UserDetailsImpl;
import ru.oleevych.cloudspace.service.FolderService;
import ru.oleevych.cloudspace.utils.BreadcrumbUtils;
import ru.oleevych.cloudspace.utils.UserFolderUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("drive/folder")
public class FolderController {
    private final FolderService folderService;
    private final String HOME_URL_REDIRECT = "redirect:/drive/folder?path=";


    @GetMapping()
    public String getFiles(@RequestParam String path,
                           @RequestParam(defaultValue = "false") boolean recursive, Model model,
                           @AuthenticationPrincipal UserDetailsImpl user) {
        String userPath = UserFolderUtils.addUserFolder(path, user.getUserId());
        List<FileMetaDto> files = folderService.getFilesInfo(userPath, recursive);
        model.addAttribute("files", files);
        model.addAttribute("breadcrumbs", BreadcrumbUtils.createBreadcrumbs(path));
        model.addAttribute("currentPath", path);
        return "drive";
    }

    @PatchMapping("rename")
    public String renameFolder(@RequestParam String path, @RequestParam String newName, @RequestParam String location,
                               @AuthenticationPrincipal UserDetailsImpl user) {
        String userPath = UserFolderUtils.addUserFolder(path, user.getUserId());
        folderService.renameFolder(userPath, newName);
        return HOME_URL_REDIRECT + URLEncoder.encode(location, StandardCharsets.UTF_8);
    }

    @GetMapping("download")
    @SneakyThrows
    public void downloadFolder(@RequestParam String path, HttpServletResponse response,
                               @AuthenticationPrincipal UserDetailsImpl user) {
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=\"archive.zip\"");
        String userPath = UserFolderUtils.addUserFolder(path, user.getUserId());
        folderService.downloadFolder(userPath, response.getOutputStream());
    }

    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SneakyThrows
    public String uploadFolder(@RequestParam String location, HttpServletRequest request,
                             @AuthenticationPrincipal UserDetailsImpl user) {
        folderService.uploadFolder(location, request.getParts(), user.getUserId());
        return HOME_URL_REDIRECT + URLEncoder.encode(location, StandardCharsets.UTF_8);
    }

}
