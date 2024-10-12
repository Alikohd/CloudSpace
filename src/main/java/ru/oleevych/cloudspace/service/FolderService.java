package ru.oleevych.cloudspace.service;

import jakarta.servlet.http.Part;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.oleevych.cloudspace.dto.FileMetaDto;
import ru.oleevych.cloudspace.dto.FileInputStreamDto;
import ru.oleevych.cloudspace.exceptions.ObjectAlreadyExists;
import ru.oleevych.cloudspace.repository.FileRepository;
import ru.oleevych.cloudspace.utils.UserFolderUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class FolderService {
    private final FileRepository fileRepository;

    public void renameFolder(String path, String newName, Long userId) {
        String userPath = UserFolderUtils.addUserFolder(path, userId);

        String newPath = userPath.replaceFirst("[^/]+/$", newName) + "/";
        if (fileRepository.isFolderExists(newPath)) {
            throw new ObjectAlreadyExists("Folder on the path " + newPath + " already exists");
        }
        fileRepository.moveFolder(userPath, newPath);
    }

    public List<FileMetaDto> getFilesInfo(String path, Long userId, boolean recursive) {
        String userPath = UserFolderUtils.addUserFolder(path, userId);
        List<FileMetaDto> files = fileRepository.getFilesMeta(userPath, recursive);
        Predicate<FileMetaDto> isUserDir = (fileMetaDto) -> fileMetaDto.getName().equals(UserFolderUtils.getUserFolder(userId));
        files.removeIf(isUserDir);
        return files;
    }

    public void downloadFolder(String folder, OutputStream outputStream, Long userId) throws IOException {
        String userPathFolder = UserFolderUtils.addUserFolder(folder, userId);
// TODO: when downloading nested dirs, upper dirs includes into archive, should add location param and split filepaths by it
        List<FileInputStreamDto> files = fileRepository.getFiles(userPathFolder, true);
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);) {
            for (var file : files) {
                String path = file.getPath();
                InputStream content = file.getContent();

                ZipEntry zipEntry = new ZipEntry(UserFolderUtils.removeUserFolder(path));
                zipOutputStream.putNextEntry(zipEntry);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = content.read(buffer)) >= 0) {
                    zipOutputStream.write(buffer, 0, length);
                }
                zipOutputStream.closeEntry();
                ;
            }
        }

    }

    @SneakyThrows
    public void uploadFolder(String location, Collection<Part> parts, Long userId) {
        String userPath = UserFolderUtils.addUserFolder(location, userId);
        for (var part : parts) {
            fileRepository.saveFile(userPath + part.getSubmittedFileName(), part.getInputStream());
        }
    }
}
