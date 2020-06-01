package team.fjut.cf.pojo.enums;

/**
 * @author axiang [2020/4/16]
 */
public enum PermissionType {
    /**
     * 管理员基本权限
     */
    BASE_ADMIN(0, "管理员认定"),
    USER_LIST_QUERY(11, "用户列表查看"),
    USER_AWARD_ACB(12, "奖励ACB"),
    USER_RESET_PWD(13, "重置密码"),
    USER_TITLE_MANAGER(14, "用户称号管理"),
    USER_GRANT_TITLE(15, "授予称号"),
    USER_VERIFY(16, "认证审核"),
    USER_CHECKIN_LIST_QUERY(17, "用户签到记录查询"),
    ADMIN_LIST_QUERY(21, "管理员列表查看"),
    MODIFY_PERMISSION(22, "修改权限"),
    REMOVE_ADMIN(23, "移除管理员"),
    LOCAL_PROBLEM_MANAGER(31, "本地题库管理"),
    SPIDER_PROBLEM_MANAGER(32, "爬取暂存题库管理"),
    VJ_PROBLEM_MANAGER(33, "VJ远程题库管理"),
    PROBLEM_TAG_MANAGER(34, "题目标签管理"),
    GOODS_MANAGER(41, "商品管理"),
    ORDER_MANAGER(42, "订单管理"),
    CONTEST_MANAGER(51, "比赛管理"),
    VERIFY_REGISTER(52, "报名审核"),
    RANK_MANAGER(61, "排名管理"),
    JUDGE_MANAGER(71, "评测管理"),
    CHALLENGE_MANAGER(81, "挑战管理"),
    DISCUSS_MANAGER(91, "论坛管理"),
    SPIDER_SERVER_MANAGER(101, "爬虫服务器管理"),
    SPIDER_ITEM_MANAGER(102, "爬虫列表管理"),
    SPIDER_ALL_SCARY(103, "全站爬取"),
    SPIDER_RANGE_SCARY(104, "范围爬取"),
    PROBLEM_SIM(105, "题目查重"),
    PROBLEM_LOCALIZED(106, "题目本地化"),
    SYSTEM_SETTING(111, "系统设置"),
    SYSTEM_ADD_MESSAGE(112, "新增通知"),
    SYSTEM_BUG_QUERY(113, "查看系统BUG反馈");

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
