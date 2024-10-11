package ru.oleevych.cloudspace.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.oleevych.cloudspace.dto.FileMetaDto;
import ru.oleevych.cloudspace.security.UserDetailsImpl;
import ru.oleevych.cloudspace.service.FolderService;
import ru.oleevych.cloudspace.utils.BreadcrumbUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("drive/folder")
public class FolderController {
    private final FolderService folderService;
    private final String USER_FILES_PATTERN = "user-%d-files/%s";
    private final String HOME_URL_REDIRECT = "redirect:/drive/folder?path=";

    @GetMapping()
    public String getFiles(@RequestParam String path,
                           @RequestParam(defaultValue = "false") boolean recursive, Model model,
                           @AuthenticationPrincipal UserDetailsImpl user) {
        String userPath = String.format(USER_FILES_PATTERN, user.getUserId(), path);
        List<FileMetaDto> files = folderService.getFiles(userPath, recursive);
        model.addAttribute("files", files);
        model.addAttribute("breadcrumbs", BreadcrumbUtils.createBreadcrumbs(path));
        model.addAttribute("currentPath", path);
        return "drive";
    }

    @PatchMapping("rename")
    public String renameFolder(@RequestParam String path, @RequestParam String newName, @RequestParam String location,
                               @AuthenticationPrincipal UserDetailsImpl user) {
        String userPath = String.format(USER_FILES_PATTERN, user.getUserId(), path);
        folderService.renameFolder(userPath, newName);
        return HOME_URL_REDIRECT + URLEncoder.encode(location, StandardCharsets.UTF_8);
    }

}
