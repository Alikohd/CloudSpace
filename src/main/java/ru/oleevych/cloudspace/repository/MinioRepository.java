package ru.oleevych.cloudspace.repository;

import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.oleevych.cloudspace.dto.FileMetaDto;
import ru.oleevych.cloudspace.exceptions.MinioOperationException;
import ru.oleevych.cloudspace.mapper.MinioMapper;

import java.io.InputStream;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MinioRepository implements FileRepository {
    private final MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://localhost:9000")
                    .credentials("devuser", "devpassword")
                    .build();
    private static final String BUCKET_NAME = "user-files";
    private final MinioMapper minioMapper;

    @Override
    public void saveFile(String pathToSave, InputStream fileInputStream) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(pathToSave)
                            .stream(fileInputStream, -1, 10485760)
                            .build());
        } catch (Exception e) {
            throw new MinioOperationException(e);
        }
    }

    @Override
    public void deleteFile(String filePath) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(filePath)
                    .build());
        } catch (Exception e) {
            throw new MinioOperationException(e);
        }
    }

    @Override
    public void moveFile(String currFilePath, String newFilePath) {
        try {
            minioClient.copyObject(CopyObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(newFilePath)
                    .source(CopySource.builder()
                            .bucket(BUCKET_NAME)
                            .object(currFilePath)
                            .build())
                    .build());

            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(currFilePath)
                    .build());

        } catch (Exception e) {
            throw new MinioOperationException(e);
        }
    }

    @Override
    public void moveFolder(String currFolderPath, String newFolderPath) {
        Iterable<Result<Item>> folderContent = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(BUCKET_NAME)
                .recursive(true)
                .prefix(currFolderPath)
                .build());

        folderContent.forEach(object -> {
            try {
                String filePathFromRoot = object.get().objectName();
                String filePathInCurrFolder = filePathFromRoot.split(currFolderPath)[1];
                moveFile(filePathFromRoot, newFolderPath + filePathInCurrFolder);
            } catch (Exception e) {
                throw new MinioOperationException(e);
            }
        });
    }

    @Override
    public List<FileMetaDto> getFilesFromFolder(String folder, Boolean recursive) {
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                .recursive(recursive)
                .prefix(folder)
                .bucket(BUCKET_NAME)
                .build());
        return minioMapper.mapToListDto(results);
    }

    @Override
    public boolean isFolderExists(String path) {
        return !getFilesFromFolder(path, false).isEmpty();
    }

    @Override
    public InputStream getFile(String filePath) {
        GetObjectResponse file;
        try {
            file = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(filePath)
                    .build());
        } catch (Exception e) {
            throw new MinioOperationException(e);
        }
        return file;
    }

    @Override
    public boolean isFileExists(String path) {
        try {
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(path)
                    .build());
            return true;

        } catch (Exception e) {
            return false;
        }
    }

}
