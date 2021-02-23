package nio;

import java.util.concurrent.atomic.AtomicInteger;

public class User {
    private final String username;
    private static final String prefix = "User";
    private static final AtomicInteger identity = new AtomicInteger(0);
    private User sendTo;

    public User() {
        this.username = prefix + identity.incrementAndGet();
    }

    public String getUsername() {
        return username;
    }

    public User getSendTo() {
        return sendTo;
    }

    public void setSendTo(User sendTo) {
        this.sendTo = sendTo;
    }
}
