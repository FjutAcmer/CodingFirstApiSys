package team.fjut.cf.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.fjut.cf.pojo.enums.ResultCode;
import team.fjut.cf.pojo.vo.ResultJson;
import team.fjut.cf.utils.UUIDUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author axiang [2019/11/15]
 */
@RestController
@CrossOrigin
@RequestMapping("/upload")
public class UploadController {
    @Value("${cf.config.file.globalPath}")
    private String globalPath;

    @Value("${cf.config.file.picPath}")
    private String picPath;

    @Value("${cf.config.file.avatarPath}")
    private String avatarPath;

    @RequestMapping("/avatar")
    public ResultJson uploadAvatar(@RequestBody final MultipartFile file)
            throws IOException {
        if (file.getSize() == 0) {
            return new ResultJson(ResultCode.BAD_REQUEST, "文件为空！");
        }
        ResultJson resultJson = new ResultJson();
        final byte[] bytes = file.getBytes();
        String randFileName = UUIDUtils.getUUID32();
        int len = Objects.requireNonNull(file.getOriginalFilename()).split("\\.").length;
        String suffix = "";
        if (len > 0) {
            suffix = file.getOriginalFilename().split("\\.")[len - 1];
        }
        final Path path = Paths.get(avatarPath + randFileName + "." + suffix);
        Files.write(path, bytes);
        resultJson.addInfo(randFileName + "." + suffix);
        return resultJson;
    }


}
