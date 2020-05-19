package team.fjut.cf.component.spider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * 爬虫参数工具类
 *
 * @author axiang [2020/4/29]
 */
@Slf4j
public class SpiderParamsTool {
    /**
     * 解析题目范围字符串
     * 将形如 “1000，[1001-1003]”的字符串解析为 “1000,1001,1002,1003”
     *
     * @param range
     * @return
     */
    public static String parseRange2Problems(String range) {
        if (StringUtils.isEmpty(range)) {
            return null;
        }
        StringBuilder problems = new StringBuilder();
        String[] split = range.split(",");
        for (String item : split) {
            if (item.charAt(0) == '[' && item.charAt(item.length() - 1) == ']') {
                String[] split1 = item.substring(1, item.length() - 1).split("-");
                for (int i = Integer.parseInt(split1[0]); i <= Integer.parseInt(split1[1]); i++) {
                    problems.append(i).append(",");
                }
            } else {
                problems.append(item).append(",");
            }
        }
        return problems.toString().substring(0, problems.length() - 1);
    }
}
