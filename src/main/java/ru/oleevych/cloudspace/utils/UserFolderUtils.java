package ru.oleevych.cloudspace.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserFolderUtils {
    private final String USER_FILES_PATTERN_HOME = "user-%d-files";
    private final String USER_FILES_PATTERN_NESTED = "user-%d-files/%s";
    public String addUserFolder(String path, Long id) {
        return String.format(USER_FILES_PATTERN_NESTED, id, path);
    }

    public String removeUserFolder(String path) {
        return path.replaceFirst("user-\\d-files/", "");
    }

    public String getUserFolder(Long id) {
        return String.format(USER_FILES_PATTERN_HOME, id);
    }
}
