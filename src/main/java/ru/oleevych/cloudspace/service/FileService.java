package ru.oleevych.cloudspace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ru.oleevych.cloudspace.dto.FileDto;
import ru.oleevych.cloudspace.dto.FileMetaDto;
import ru.oleevych.cloudspace.exceptions.DownloadFileException;
import ru.oleevych.cloudspace.exceptions.ObjectAlreadyExists;
import ru.oleevych.cloudspace.repository.FileRepository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    public void saveFile(String path, InputStream file) {
        fileRepository.saveFile(path, file);
    }

    public FileDto downloadFile(String filePath) {
        String name = Paths.get(filePath).getFileName().toString();
        InputStream file = fileRepository.getFile(filePath);
        Resource resource;
        try {
            resource = new ByteArrayResource(file.readAllBytes());
        } catch (IOException e) {
            throw new DownloadFileException("Error while reading" + name + "file", e);
        }
        return new FileDto(name, resource);
    }

    public void renameFile(String path, String newName) {
        String newPath = path.replaceFirst("[^/]+$", newName);
        if (fileRepository.exists(newPath)) {
            throw new ObjectAlreadyExists("File on the path " + newPath + " already exists");
        }
        fileRepository.moveFile(path, newPath);
    }
}
