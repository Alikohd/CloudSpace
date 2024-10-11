package ru.oleevych.cloudspace.repository;

import ru.oleevych.cloudspace.dto.FileMetaDto;
import ru.oleevych.cloudspace.dto.FileInputStreamDto;

import java.io.FilterInputStream;
import java.io.InputStream;
import java.util.List;

public interface FileRepository {
    void saveFile(String pathToSave, InputStream fileInputStream);
    void deleteFile(String filePath);
    void moveFile(String currFilePath, String newFilePath);
    void moveFolder(String currFolderPath, String newFolderPath);
    List<FileMetaDto> getFilesMeta(String folder, Boolean recursive);
    boolean isFolderExists(String path);
    FilterInputStream getFile(String filePath);
    boolean isFileExists(String path);
    List<FileInputStreamDto> getFiles(String folder, boolean recursive);
}

