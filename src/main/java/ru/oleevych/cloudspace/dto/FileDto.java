package ru.oleevych.cloudspace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.io.Resource;

@Data
@AllArgsConstructor
public class FileDto {
    private String name;
    private Resource content;
}
