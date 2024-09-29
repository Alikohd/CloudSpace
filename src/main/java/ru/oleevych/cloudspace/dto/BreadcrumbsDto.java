package ru.oleevych.cloudspace.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BreadcrumbsDto {
    private String folderName;
    private String folderLink;
}
