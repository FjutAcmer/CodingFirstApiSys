package team.fjut.cf.component.textsim;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import team.fjut.cf.component.textsim.pojo.ProblemInfoToSim;
import team.fjut.cf.utils.FileUtils;

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
public class TextSimExecClient {
    @Value("${cf.config.local.simFilePath}")
    private String simFilePath;

    /**
     * 相似度匹配程序名
     */
    private static final String simFileName = "CNTextSim.py";

    /**
     * python执行文件路径
     */
    @Value("${cf.config.local.pythonPath}")
    private String pythonExecPath;

    /**
     * 执行两个题目文本相似度检测
     *
     * @param simReportId
     * @param problemInfo1
     * @param problemInfo2
     * @return
     */
    public JSONObject SimTwoProblem(String simReportId, ProblemInfoToSim problemInfo1, ProblemInfoToSim problemInfo2) throws IOException {
        String testFilePath = String.format("%s%s-%s-test.txt", simFilePath, simReportId, problemInfo1.getTitle());
        String sampleFilePath = String.format("%s%s-%s-sample.txt", simFilePath, simReportId, problemInfo2.getTitle());
        String resultFilePath = String.format("%s%s-result.json", simFilePath, simReportId);
        FileUtils.writeToFile(testFilePath, problemInfo1.toPureString());
        FileUtils.writeToFile(sampleFilePath, problemInfo2.toPureString());
        return executeTextSim(testFilePath, sampleFilePath, resultFilePath);
    }

    /**
     * 实际操作
     *
     * @param testFilePath
     * @param sampleFilePath
     * @param resultFilePath
     * @return
     */
    private JSONObject executeTextSim(String testFilePath, String sampleFilePath, String resultFilePath) {
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
