package ru.oleevych.cloudspace.repository;

import java.io.InputStream;

public interface FileRepository {
    void createFile(String pathToSave, InputStream fileInputStream);
    void deleteFile(String filePath);
    void moveFile(String currFilePath, String newFilePath);
    void moveFolder(String currFolderPath, String newFolderPath);

}

