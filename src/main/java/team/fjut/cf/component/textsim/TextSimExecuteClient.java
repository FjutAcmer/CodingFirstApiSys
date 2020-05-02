package team.fjut.cf.component.textsim;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 文本匹配程序执行客户端
 *
 * @author axiang [2020/5/2]
 */
@Component
@Slf4j
public class TextSimExecuteClient {
    @Value("${cf.config.local.simFilePath}")
    String simFilePath;

    /**
     * 相似度匹配程序名
     */
    private static final String simFileName = "CNTextSim.py";

    /**
     * TODO: python执行程序路径需要在部署时进行切换
     */
    private static final String pythonExecPath = "D://Anaconda3/envs/web/python.exe";

    public JSONObject executeTextSim(String testFilePath, String sampleFilePath, String resultFilePath) {
        StringBuilder result = new StringBuilder();
        try {

            String[] args = new String[]{pythonExecPath, simFilePath + simFileName, testFilePath, sampleFilePath, resultFilePath};
            Process proc = Runtime.getRuntime().exec(args); // 执行py文件

            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(), Charsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return JSONObject.parseObject(result.toString());
    }
}
