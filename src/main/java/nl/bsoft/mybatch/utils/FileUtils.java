package nl.bsoft.mybatch.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.StringTokenizer;

@Slf4j
public class FileUtils {

    /**
     * Searching the file name
     *
     * @param fileName
     * @return - if found File
     * - if not found null
     */
    public static File search(final String fileName) {
        final String classpathStr = System.getProperty("java.class.path");
        final String separator = System.getProperty("path.separator");
        final StringTokenizer strTokenizer = new StringTokenizer(classpathStr, separator);

        while (strTokenizer.hasMoreTokens()) {
            final String pathElement = strTokenizer.nextToken();
            final File directory = new File(pathElement);
            final File absoluteDirectory = directory.getAbsoluteFile();
            if (absoluteDirectory.isFile()) {
                final File target = new File(absoluteDirectory.getParent(), fileName);
                if (target.exists()) {
                    return target;
                }
            } else {
                final File target = new File(directory, fileName);
                if (target.exists()) {
                    return target;
                }
            }
        }
        return null;
    }
}
