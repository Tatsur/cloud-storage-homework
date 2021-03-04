package netty;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    private String username;
    private String text;
    private LocalDateTime sendAt;

    public Message(String username, String text) {
        this.username = username;
        this.text = text;
        this.sendAt = LocalDateTime.now();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getSendAt() {
        return sendAt;
    }

    public void setSendAt(LocalDateTime sendAt) {
        this.sendAt = sendAt;
    }

    @Override
    public String toString() {
        return "Message{" +
                "username='" + username + '\'' +
                ", text=" + text +
                ", sendAt=" + sendAt +
                '}';
    }
}
