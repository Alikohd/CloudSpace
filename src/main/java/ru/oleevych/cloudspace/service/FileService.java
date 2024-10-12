package ru.oleevych.cloudspace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ru.oleevych.cloudspace.dto.FileResourceDto;
import ru.oleevych.cloudspace.exceptions.DownloadFileException;
import ru.oleevych.cloudspace.exceptions.ObjectAlreadyExists;
import ru.oleevych.cloudspace.repository.FileRepository;
import ru.oleevych.cloudspace.utils.UserFolderUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    public void saveFile(String path, InputStream file, Long userId) {
        String userPath = UserFolderUtils.addUserFolder(path, userId);
        fileRepository.saveFile(userPath, file);
    }

    public FileResourceDto downloadFile(String filePath, Long userId) {
        String userPath = UserFolderUtils.addUserFolder(filePath, userId);
        String name = Paths.get(userPath).getFileName().toString();
        InputStream file = fileRepository.getFile(userPath);
        Resource resource;
        try {
            resource = new ByteArrayResource(file.readAllBytes());
        } catch (IOException e) {
            throw new DownloadFileException("Error while reading" + name + "file", e);
        }
        return new FileResourceDto(name, resource);
    }

    public void renameFile(String path, String newName, Long userId) {
        String userPath = UserFolderUtils.addUserFolder(path, userId);
        String newPath = userPath.replaceFirst("[^/]+$", newName);
        if (fileRepository.isFileExists(newPath)) {
            throw new ObjectAlreadyExists("File on the path " + newPath + " already exists");
        }
        fileRepository.moveFile(userPath, newPath);
    }
}
