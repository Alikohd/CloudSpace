package ru.oleevych.cloudspace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStream;

@Data
@AllArgsConstructor
public class FileInputStreamDto {
    private String path;
    private InputStream content;
}
