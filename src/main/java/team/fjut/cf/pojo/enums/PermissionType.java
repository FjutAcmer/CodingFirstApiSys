package team.fjut.cf.pojo.enums;

import javax.persistence.Table;

/**
 * @author axiang [2020/4/16]
 */
public enum PermissionType {
    /**
     * 管理员基本权限
     */
    BASE_ADMIN(00, "管理员"),
    USER_CHECKIN_QUERY(11, "查看用户签到记录"),
    USER_AWARD_ACB(12, "奖励ACB"),
    USER_RESET_PSW(13, "重置密码"),
    USER_TITLE_MANAGER(14, "用户称号管理"),
    USER_VERIFY(15, "认证审核"),
    PERMISSION_MANAGER(21, "权限管理"),
    SGRAPY_GET_PROBLEM(31, "爬取题目"),
    SGRAPY_SIM_PROBLEM(32, "题目查重"),
    SGRAPY_MANAGER_QUERY(41, "查看爬虫状态"),
    SGRAPY_MANAGER_SET(42, "爬虫任务设置"),
    LOCAL_PROBLEM_MANAGER(51, "本地题库管理"),
    VJ_PROBLEM_MANAGER(52, "VJ题库管理"),
    PROBLEM_TAG_MANAGER(53, "标签管理"),
    GOODS_MANAGER(61, "商品管理"),
    ORDER_MANAGER(62, "订单管理"),
    CONTEST_MANAGER(71, "比赛管理"),
    VERIFY_REGISTER(72, "报名审核"),
    JUDGE_MANAGER(81, "评测管理"),
    CHALLENGE_MANAGER(91, "挑战管理"),
    RANK_MANAGER(101, "排名管理"),
    SYSTEM_ADD_MESSAGE(111, "新增通知"),
    SYSTEM_LOG_QUERY(112, "查看Log");



    private int id;
    private String name;

    PermissionType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getNameByID(int id) {
        for (PermissionType t : PermissionType.values()) {
            if (t.getId() == id) {
                return t.getName();
            }
        }
        return null;
    }

    public static PermissionType getEnumByID(int id) {
        for (PermissionType t : PermissionType.values()) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }
}
