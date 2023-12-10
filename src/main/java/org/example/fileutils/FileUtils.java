package org.example.fileutils;

import com.google.common.base.Preconditions;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {
    public static List<String> readResourceAsStream(String absoluteResourcePath) throws IOException {
        if (!absoluteResourcePath.startsWith("/")) {
            absoluteResourcePath = "/" + absoluteResourcePath;
        }

        try (InputStream is = FileUtils.class.getResourceAsStream(absoluteResourcePath)){
            Preconditions.checkNotNull(is, "Resource not found: " + absoluteResourcePath);

            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            return br.lines().collect(Collectors.toList());
        }
    }
}
