package ru.oleevych.cloudspace.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserFolderUtils {
    private final String USER_FILES_PATTERN = "user-%d-files/%s";
    public String addUserFolder(String path, long id) {
        return String.format(USER_FILES_PATTERN, id, path);
    }

    public String removeUserFolder(String path) {
        return path.replaceFirst("user-\\d-files/", "");
    }
}
