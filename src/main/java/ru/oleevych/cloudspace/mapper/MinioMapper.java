package ru.oleevych.cloudspace.mapper;

import io.minio.Result;
import io.minio.messages.Item;
import org.springframework.stereotype.Component;
import ru.oleevych.cloudspace.dto.FileResponseDto;
import ru.oleevych.cloudspace.exceptions.MinioMappingException;

import java.util.ArrayList;
import java.util.List;

@Component
public class MinioMapper {
    public List<FileResponseDto> mapToListDto(Iterable<Result<Item>> items) {
        List<FileResponseDto> resultList = new ArrayList<>();
        items.forEach(item -> {
            try {
                Item currItem = item.get();
                resultList.add(mapToDto(currItem));
            } catch (Exception e) {
                throw new MinioMappingException("An error occurred while mapping from FileRepository");
            }
        });
        return resultList;
    }

    private FileResponseDto mapToDto(Item item) {
        return FileResponseDto.builder()
                .name(item.objectName())
                .isDir(item.isDir())
                .size(item.size())
                .build();
    }
}
