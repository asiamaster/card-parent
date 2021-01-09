package com.dili.card.type;

public enum BindBankStatus
{
    NORMAL("正常", 1),

    INVALID("失效", 0);

    private String name;
    private int code;

    BindBankStatus(String name, int code)
    {
        this.name = name;
        this.code = code;
    }

    public static BindBankStatus getBindStatus(int code)
    {
        for (BindBankStatus type : BindBankStatus.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }

    public static String getName(int code)
    {
        for (BindBankStatus type : BindBankStatus.values()) {
            if (type.getCode() == code) {
                return type.name;
            }
        }
        return null;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String toString()
    {
        return name;
    }
}
