package ru.oleevych.cloudspace.dto;

import lombok.Data;

import java.io.InputStream;

@Data
public class UploadFileDto {
    private final String path;
    private final InputStream file;
}
