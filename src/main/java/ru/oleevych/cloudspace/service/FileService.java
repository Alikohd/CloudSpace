package ru.oleevych.cloudspace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.oleevych.cloudspace.dto.FileDto;
import ru.oleevych.cloudspace.dto.FileMetaDto;
import ru.oleevych.cloudspace.repository.FileRepository;

import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    public void saveFile(Long id, String path, InputStream file) {
        fileRepository.saveFile("user-" + id.toString() + "-files/" + path, file);
    }

    public List<FileMetaDto> getFiles(String folder, boolean recursive) {
        return fileRepository.getFilesFromFolder(folder, recursive);
    }

    public FileDto downloadFile(String filePath) {
        String name = Paths.get(filePath).getFileName().toString();
        InputStream file = fileRepository.getFile(filePath);
        return new FileDto(name, file);
    }
}
