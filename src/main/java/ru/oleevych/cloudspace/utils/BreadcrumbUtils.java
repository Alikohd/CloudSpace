package ru.oleevych.cloudspace.utils;

import lombok.experimental.UtilityClass;
import ru.oleevych.cloudspace.dto.BreadcrumbsDto;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class BreadcrumbUtils {
    public static List<BreadcrumbsDto> createBreadcrumbs(String path) {
        List<BreadcrumbsDto> breadcrumbs = new ArrayList<>();
        String[] folderSequence = path.split("/");
        StringBuilder initPath = new StringBuilder();

        breadcrumbs.add(new BreadcrumbsDto("Home", ""));

        for (String folder : folderSequence) {
            if (folder.isBlank()) {
                continue;
            }
            initPath.append(folder).append("/");
            breadcrumbs.add(new BreadcrumbsDto(folder, initPath.toString()));
        }
        return breadcrumbs;

    }
}
