package ru.oleevych.cloudspace.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.oleevych.cloudspace.dto.FileResponseDto;
import ru.oleevych.cloudspace.security.UserDetailsImpl;
import ru.oleevych.cloudspace.service.FileService;

import java.util.List;

@Controller
@RequestMapping("/drive")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;
    @GetMapping()
    public String getFiles(@RequestParam String folder, @RequestParam boolean recursive, Model model,
                           @AuthenticationPrincipal UserDetailsImpl user) {
        List<FileResponseDto> files = fileService.getFiles(
                String.format("user-%d-files/%s", user.getUserId(), folder),
                recursive);
        model.addAttribute("files", files);
        return "drive";
    }
}
