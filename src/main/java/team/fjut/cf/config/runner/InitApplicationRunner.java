package team.fjut.cf.config.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 项目初始化执行内容
 * 在Bean生成后
 *
 * @author axiang [2020/3/18]
 */
@Slf4j
@Component
@Order(1)
public class InitApplicationRunner implements CommandLineRunner {
    @Value("${cf.config.file.globalPath}")
    String globalPath;

    @Value("${cf.config.file.tempPath}")
    String tempPath;

    @Value("${cf.config.file.picPath}")
    String picPath;

    @Value("${cf.config.file.avatarPath}")
    String avatarPath;

    @Value("${cf.config.local.simFilePath}")
    String simFilePath;

    @Override
    public void run(String... args) {
        initFilePath();
    }

    /**
     * 检查文件夹，并生成
     */
    public void initFilePath() {
        boolean isAllCreated = true;
        File[] files = new File[5];
        files[0] = new File(globalPath);
        files[1] = new File(tempPath);
        files[2] = new File(picPath);
        files[3] = new File(avatarPath);
        files[4] = new File(simFilePath);
        for (File file : files) {
            // 文件夹不存在，则创建
            if (!file.exists() && !file.isDirectory()) {
                boolean mkdir = file.mkdirs();
                isAllCreated = mkdir & isAllCreated;
            }
        }
        if (isAllCreated) {
            log.info("项目初始化：所需文件夹已全部创建");
        } else {
            log.error("项目初始化：所需文件夹创建失败！");
        }
    }


}
