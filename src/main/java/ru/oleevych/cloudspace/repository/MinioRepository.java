package ru.oleevych.cloudspace.repository;

import io.minio.*;
import io.minio.messages.Item;
import org.springframework.stereotype.Repository;
import ru.oleevych.cloudspace.exceptions.MinioOperationException;
import java.io.InputStream;

@Repository
public class MinioRepository implements FileRepository{
    private final MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://localhost:9000")
                    .credentials("devuser", "devpassword")
                    .build();
    private static final String BUCKET_NAME = "user-files";

    @Override
    public void createFile(String pathToSave, InputStream fileInputStream) {
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

}
