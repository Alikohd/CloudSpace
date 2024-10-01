package ru.oleevych.cloudspace.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileResponseDto {
    private String name;
    private String path;
    private boolean isDir;
    private long size;
}
