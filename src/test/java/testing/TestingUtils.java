package testing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Generic utility class for all unit tests.
 */
public final class TestingUtils {

    public static String getWorkingDirectory() {
        return System.getProperty("user.dir").replace(".idea\\modules", ""); // this will make it work in IntelliJ and outside of it...
    }

    /**
     * Reads the content of the provided file.
     */
    public static List<String> readFile(File file) throws IOException {

        InputStream is = new FileInputStream(file);
        if (file.getName().toLowerCase().endsWith(".gz"))
            is = new GZIPInputStream(is);

        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
        }

        return lines;
    }

    /**
     * Creates a file in a "test-tmp" folder in the build folder.
     */
    public static File createFile(String filename) throws IOException {

        // create the tmp folder
        File tmpDir = new File(getWorkingDirectory() + "/build/test-tmp");
        if (!tmpDir.exists() && !tmpDir.mkdirs())
            throw new IOException("Unable to create tmp dir...");

        // there is no need to physically create the file, it will be created when something is written to it...
        File file = new File(tmpDir, filename);
        file.deleteOnExit();
        return file;
    }
}
