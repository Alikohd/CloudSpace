package ru.oleevych.cloudspace.repository;

import ru.oleevych.cloudspace.dto.FileResponseDto;

import java.io.InputStream;
import java.util.List;

public interface FileRepository {
    void saveFile(String pathToSave, InputStream fileInputStream);
    void deleteFile(String filePath);
    void moveFile(String currFilePath, String newFilePath);
    void moveFolder(String currFolderPath, String newFolderPath);
    List<FileResponseDto> getFilesFromFolder(String folder, Boolean recursive);

}

