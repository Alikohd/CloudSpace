package ru.oleevych.cloudspace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.oleevych.cloudspace.dto.FileResponseDto;
import ru.oleevych.cloudspace.repository.FileRepository;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    public void saveFile(Long id, String path, InputStream file) {
        fileRepository.saveFile("user-" + id.toString() + "-files/" + path, file);
    }

    public List<FileResponseDto> getFiles(String folder, boolean recursive) {
        return fileRepository.getFilesFromFolder(folder, recursive);
    }
}
