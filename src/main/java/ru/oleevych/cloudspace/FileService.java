package ru.oleevych.cloudspace;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.oleevych.cloudspace.dto.UploadFileDto;
import ru.oleevych.cloudspace.repository.FileRepository;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;

    public void saveFile(Long id, UploadFileDto uploadFileDto) {
//        TODO: refactor
        fileRepository.createFile("user-" + id.toString() + "-files/" + uploadFileDto.getPath(),
                uploadFileDto.getFile());
    }
}
