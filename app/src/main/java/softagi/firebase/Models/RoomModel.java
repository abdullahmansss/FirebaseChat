package softagi.firebase.Models;

public class RoomModel
{
    private String room_name,room_id;

    public RoomModel() {
    }

    public RoomModel(String room_name, String room_id) {
        this.room_name = room_name;
        this.room_id = room_id;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }
}
