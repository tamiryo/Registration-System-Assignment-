package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessagingProtocolClass;

public class LogoutMessage extends Message{

    public LogoutMessage() {

    }

    @Override
    public Message act(MessagingProtocolClass protocol) {
        Short messageOpcode = 4;
        if (protocol.getUser()!=null){ //there is a user logged in that can make a logout
            protocol.getUser().setLog(false);
            protocol.terminate();
            return new AckMessage(messageOpcode,"");
        }
        else //no user is logged in
            return new ErrorMessage(messageOpcode);
    }

    @Override
    public boolean isAck() {
        return false;
    }

    @Override
    public Short getMessageOpcode() {
        return null;
    }

    @Override
    public String getOptional() {
        return null;
    }
}
