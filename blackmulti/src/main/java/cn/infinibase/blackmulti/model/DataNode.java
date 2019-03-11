package cn.infinibase.blackmulti.model;

/**
 * @author: fanchao
 * @description:
 * @date: Create in 11:05 2018/7/3
 * @modified By:
 */
public class DataNode {
    private String key;
    private String value;

    public DataNode(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
