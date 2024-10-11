package ru.oleevych.cloudspace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.oleevych.cloudspace.dto.FileMetaDto;
import ru.oleevych.cloudspace.repository.FileRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {
    private final FileRepository fileRepository;

    public void renameFolder(String currPath, String newPath) {
        fileRepository.moveFolder(currPath, newPath);
    }

    public List<FileMetaDto> getFiles(String folder, boolean recursive) {
        return fileRepository.getFilesFromFolder(folder, recursive);
    }
}
