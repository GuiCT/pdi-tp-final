package utils;

import pdi.Channel;
import pdi.Format;

import java.io.File;

public record Status(
        Integer id,
        Format format,
        File file,
        Object image
) {
    public static Integer nextId = 0;

    public Status(Format format, File file, Object image) {
        this(++nextId, format, file, image);
    }

    @Override
    public String toString() {
        return String.format("%d - %s", id, file.getPath());
    }
}
