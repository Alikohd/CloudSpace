package ru.oleevych.cloudspace.mapper;

import io.minio.Result;
import io.minio.messages.Item;
import org.springframework.stereotype.Component;
import ru.oleevych.cloudspace.dto.FileResponseDto;
import ru.oleevych.cloudspace.exceptions.MinioMappingException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class MinioMapper {
    public List<FileResponseDto> mapToListDto(Iterable<Result<Item>> items) {
        List<FileResponseDto> resultList = new ArrayList<>();
        items.forEach(item -> {
            try {
                Item currItem = item.get();
                var currDto = mapToDto(currItem);
                String nameWithHiddenUserDir = currDto.getPath().replaceFirst("user-\\d-files/", "");
                if (!nameWithHiddenUserDir.contains("/")) {
                    nameWithHiddenUserDir = "";
                }
                currDto.setPath(nameWithHiddenUserDir);
                resultList.add(currDto);
            } catch (Exception e) {
                throw new MinioMappingException("An error occurred while mapping from FileRepository");
            }
        });
        return resultList;
    }

    private FileResponseDto mapToDto(Item item) {
        Path filePath = Paths.get(item.objectName());
        return FileResponseDto.builder()
                .name(filePath.getFileName().toString())
                .path(item.objectName())
                .isDir(item.isDir())
                .size(item.size())
                .build();
    }
}
