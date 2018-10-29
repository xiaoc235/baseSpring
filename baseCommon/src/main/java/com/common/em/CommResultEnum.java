package com.common.em;

/**
*  基本结果
 */
public enum CommResultEnum {
    //是
    YES("1"),
    //否
    NO("0");

    private String value ;

    private CommResultEnum(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Integer getIntValue(){
        return Integer.parseInt(value);
    }

    public static boolean getBoolean(int value){
        return value == YES.getIntValue();
    }

    public static int getInt(boolean flag){
        return flag ? YES.getIntValue() : NO.getIntValue();
    }


}
