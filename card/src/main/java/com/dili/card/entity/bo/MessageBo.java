package com.dili.card.entity.bo;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/1/19 15:58
 * @Description:
 */
public class MessageBo<T> {
    /**返回码*/
    private String code;
    /**数据*/
    private T data;
    /**消息数据*/
    private String message;

    public static <T> MessageBo<T> success(T data) {
        MessageBo<T> messageBo = new MessageBo<>();
        messageBo.setData(data);
        messageBo.setMessage("ok");
        messageBo.setCode("200");
        return messageBo;
    }

    public static <T> MessageBo<T> fail(String code, String message, T data) {
        MessageBo<T> messageBo = new MessageBo<>();
        messageBo.setData(data);
        messageBo.setMessage(message);
        messageBo.setCode(code);
        return messageBo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
