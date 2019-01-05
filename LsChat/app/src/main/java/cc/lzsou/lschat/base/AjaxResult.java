package cc.lzsou.lschat.base;


public class AjaxResult{

    public static final String FAILED="400";
    public static final String SUCCESS="200";

    private String code;
    private String content;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess(){
        return  this.getCode().equals(SUCCESS);
    }

}
