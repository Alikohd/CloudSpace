package ru.oleevych.cloudspace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStream;

@Data
@AllArgsConstructor
public class FileDto {
    private String name;
    private InputStream content;
}
