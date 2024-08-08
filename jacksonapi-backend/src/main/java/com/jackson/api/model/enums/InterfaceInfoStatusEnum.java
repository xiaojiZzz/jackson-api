package com.jackson.api.model.enums;

/**
 * 接口信息状态枚举类
 *
 * @author jackson
 */
public enum InterfaceInfoStatusEnum {

    /**
     * 上线和下线
     */
    ONLINE("上线", 1),
    OFFLINE("下线", 0);

    private final String text;
    private final int value;

    InterfaceInfoStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
