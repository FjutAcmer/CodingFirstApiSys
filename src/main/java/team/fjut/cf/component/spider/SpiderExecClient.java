package team.fjut.cf.component.spider;

import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * 直接操作爬虫系统的文件
 *
 * @author axiang [2020/5/14]
 */
@Component
@Slf4j
public class SpiderExecClient {
    @Value("${cf.config.local.spiderProjectPath}")
    private String spiderProjectPath;

    private static final String reDeployFileName = "redeploy.py";

    /**
     * python执行文件路径
     */
    @Value("${cf.config.local.pythonPath}")
    private String pythonExecPath;

    /**
     * FIXME: 执行失败
     * 执行重启服务器任务
     *
     * @return
     */
    public String executeRedeploy() {
        StringBuilder result = new StringBuilder();
        try {
            String[] args = new String[]{pythonExecPath, spiderProjectPath + reDeployFileName};
            log.info(Arrays.toString(args));
            // 执行py文件
            Process proc = Runtime.getRuntime().exec(args);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getErrorStream(), Charsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    /**
     * 更新配置文件
     *
     * @return
     */
    public String updateSetting() {
        return null;
    }
}
