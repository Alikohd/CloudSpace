package ru.oleevych.cloudspace.repository;

import ru.oleevych.cloudspace.dto.FileMetaDto;

import java.io.InputStream;
import java.util.List;

public interface FileRepository {
    void saveFile(String pathToSave, InputStream fileInputStream);
    void deleteFile(String filePath);
    void moveFile(String currFilePath, String newFilePath);
    void moveFolder(String currFolderPath, String newFolderPath);
    List<FileMetaDto> getFilesFromFolder(String folder, Boolean recursive);

    boolean isFolderExists(String path);

    InputStream getFile(String filePath);

    boolean isFileExists(String path);
}

