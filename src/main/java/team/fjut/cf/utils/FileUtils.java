package team.fjut.cf.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 文件工具类
 *
 * @author axiang [2020/5/2]
 */
public class FileUtils {
    /**
     * 将content写入文件路径下的文件
     *
     * @param filePath
     * @param content
     * @throws IOException
     */
    public static void writeToFile(String filePath, String content) throws IOException {
        File file = new File(filePath);
        FileWriter fileWriter = null;
        fileWriter = new FileWriter(file);
        fileWriter.write(content);
        fileWriter.close();

    }

}
