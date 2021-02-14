package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessagingProtocolClass;

public class ErrorMessage extends Message {

    private Short messageOpcode;

    public ErrorMessage(Short _messageOpcode){
        messageOpcode = _messageOpcode;
    }

    @Override
    public Message act(MessagingProtocolClass protocol) {
        return null;
    }

    @Override
    public boolean isAck() {
        return false;
    }

    public Short getMessageOpcode() {
        return messageOpcode;
    }

    @Override
    public String getOptional() {
        return null;
    }
}
