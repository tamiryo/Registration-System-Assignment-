package bgu.spl.net.api;

import bgu.spl.net.impl.BGRSServer.Message;
import bgu.spl.net.impl.BGRSServer.User;

public class MessagingProtocolClass implements MessagingProtocol<Message>{

    private boolean shouldTerminate = false;
    private User user = null;

    public MessagingProtocolClass() {}

    @Override
    public Message process(Message msg) {
        return msg.act(this);
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void terminate(){
        shouldTerminate = true;
    }
}
