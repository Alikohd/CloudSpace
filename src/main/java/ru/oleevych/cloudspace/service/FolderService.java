package ru.oleevych.cloudspace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.oleevych.cloudspace.dto.FileMetaDto;
import ru.oleevych.cloudspace.dto.FileInputStreamDto;
import ru.oleevych.cloudspace.exceptions.ObjectAlreadyExists;
import ru.oleevych.cloudspace.repository.FileRepository;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    public List<FileMetaDto> getFilesInfo(String folder, boolean recursive) {
        return fileRepository.getFilesMeta(folder, recursive);
    }

    public void downloadFolder(String folder, OutputStream outputStream) throws IOException {
        List<FileInputStreamDto> files = fileRepository.getFiles(folder, true);

        System.out.println("heafasf");
        System.out.println("heafasf");
        System.out.println("heafasf");
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);) {
            for (var file : files) {
                String path = file.getPath();
                InputStream content = file.getContent();

                ZipEntry zipEntry = new ZipEntry(path);
                zipOutputStream.putNextEntry(zipEntry);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = content.read(buffer)) >= 0) {
                    zipOutputStream.write(buffer, 0, length);
                }
                zipOutputStream.closeEntry();;
            }
        }

    }
}
