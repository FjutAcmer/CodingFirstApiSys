package team.fjut.cf.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResultJson uploadAvatar(@RequestBody final MultipartFile avatar)
            throws IOException {
        if (avatar.getSize() == 0) {
            return new ResultJson(ResultCode.BAD_REQUEST, "文件为空！");
        }
        final byte[] bytes = avatar.getBytes();
        String randFileName = "avatar-" + UUIDUtils.getUUID32();
        int len = Objects.requireNonNull(avatar.getOriginalFilename()).split("\\.").length;
        String suffix = "";
        if (len > 0) {
            suffix = avatar.getOriginalFilename().split("\\.")[len - 1];
        }
        final Path path = Paths.get(avatarPath + randFileName + "." + suffix);
        Files.write(path, bytes);

        return new ResultJson(ResultCode.REQUIRED_SUCCESS, randFileName + "." + suffix, "上传成功！");

    }


    @RequestMapping("/picture")
    public ResultJson uploadPicture(@RequestBody final MultipartFile file)
            throws IOException {
        if (file.getSize() == 0) {
            return new ResultJson(ResultCode.BUSINESS_FAIL, "文件为空！");
        }
        ResultJson resultJson = new ResultJson();
        final byte[] bytes = file.getBytes();
        String randFileName = "picture-" + UUIDUtils.getUUID32();
        int len = Objects.requireNonNull(file.getOriginalFilename()).split("\\.").length;
        String suffix = "";
        if (len > 0) {
            suffix = file.getOriginalFilename().split("\\.")[len - 1];
        }
        final Path path = Paths.get(picPath + randFileName + "." + suffix);
        Files.write(path, bytes);
        resultJson.addInfo(randFileName + "." + suffix);
        return resultJson;
    }


}
