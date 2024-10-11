package ru.oleevych.cloudspace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.oleevych.cloudspace.dto.FileMetaDto;
import ru.oleevych.cloudspace.exceptions.ObjectAlreadyExists;
import ru.oleevych.cloudspace.repository.FileRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {
    private final FileRepository fileRepository;

    public void renameFolder(String path, String newName) {
        String newPath = path.replaceFirst("[^/]+/$", newName) + "/";
        if (fileRepository.isFolderExists(newPath)) {
            throw new ObjectAlreadyExists("Folder on the path " + newPath + " already exists");
        }
        fileRepository.moveFolder(path, newPath);
    }

    public List<FileMetaDto> getFiles(String folder, boolean recursive) {
        return fileRepository.getFilesFromFolder(folder, recursive);
    }
}
