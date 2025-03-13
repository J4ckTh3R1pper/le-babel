package dayp308.chatroom.deprecated.model;

import java.util.Date;

import org.springframework.stereotype.Component;

//@Component
public class Message {
    private String text;
    private int owner;
    private int inChannel;
    private String file;
    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    // @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private int ind;
    private String type;
    public Message() {
    }
    public Message(String text, int owner, int inChannel, String file, Date date, int index, String type ) {
        this.text = text;
        this.inChannel = inChannel;
        this.owner = owner;
        this.file = file;
        this.date = date;
        this.ind = index;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getInChannel() {
        return inChannel;
    }

    public void setInChannel(int inChannel) {
        this.inChannel = inChannel;
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getInd() {
        return ind;
    }

    public void setInd(int ind) {
        this.ind = ind;
    }

    @Override
    public String toString() {
        return "Message{" +
                "text='" + text + '\'' +
                ", owner=" + owner +
                ", in_channel=" + inChannel +
                ", file='" + file + '\'' +
                ", date=" + date +
                ", ind=" + ind +
                ", type='" + type + '\'' +
                '}';
    }
}
