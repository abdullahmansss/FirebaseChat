package softagi.firebase.Models;

public class MessageModel
{
    private String msg,name,id;

    public MessageModel() {
    }

    public MessageModel(String msg, String name, String id) {
        this.msg = msg;
        this.name = name;
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
