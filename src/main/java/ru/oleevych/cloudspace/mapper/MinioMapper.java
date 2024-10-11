package ru.oleevych.cloudspace.mapper;

import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.oleevych.cloudspace.dto.FileMetaDto;
import ru.oleevych.cloudspace.dto.FileInputStreamDto;
import ru.oleevych.cloudspace.exceptions.MinioMappingException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MinioMapper {
    public List<FileMetaDto> mapToFileMetaListDto(Iterable<Result<Item>> items) {
        List<FileMetaDto> resultList = new ArrayList<>();
        items.forEach(item -> {
            try {
                Item currItem = item.get();
                var currDto = mapToMetaDto(currItem);
                String nameWithHiddenUserDir = currDto.getPath().replaceFirst("user-\\d-files/", "");
                currDto.setPath(nameWithHiddenUserDir);
                resultList.add(currDto);
            } catch (Exception e) {
                throw new MinioMappingException("An error occurred while mapping from FileRepository toFileMetaListDto", e);
            }
        });
        return resultList;
    }

    public List<String> mapToPathList(Iterable<Result<Item>> items) {
        List<String> resultList = new ArrayList<>();
        items.forEach(item -> {
            try {
                Item currItem = item.get();
                resultList.add(currItem.objectName());
            } catch (Exception e) {
                throw new MinioMappingException("An error occurred while mapping from FileRepository toPathList", e);
            }
        });
        return resultList;
    }

    private FileMetaDto mapToMetaDto(Item item) {
        Path filePath = Paths.get(item.objectName());
        return FileMetaDto.builder()
                .name(filePath.getFileName().toString())
                .path(item.objectName())
                .isDir(item.isDir())
                .size(item.size())
                .build();
    }
}
