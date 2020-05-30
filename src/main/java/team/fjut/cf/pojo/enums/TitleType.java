package team.fjut.cf.pojo.enums;

/**
 * @author zhongml [2020/05/15]
 */
public enum TitleType {
    /**
     *
     */
    UNDEFINE(0, "未定义"),
    ADJECTIVE(1, "形容词"),
    TITLE(2, "名词头衔");


    private int id;
    private String name;

    TitleType(int id, String name) {
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
        for (TitleType t : TitleType.values()) {
            if (t.getId() == id) {
                return t.getName();
            }
        }
        return null;
    }


}
