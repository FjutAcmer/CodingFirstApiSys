package team.fjut.cf.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author axiang [2020/5/2]
 */
public class FileUtils {
    public static void writeToFile(String filePath, String content) throws IOException {
        File file = new File(filePath);
        FileWriter fileWriter = null;
        fileWriter = new FileWriter(file);
        fileWriter.write(content);
        fileWriter.close();

    }

}
