package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessagingProtocolClass;

public class AckMessage extends Message {

    private Short messageOpcode;
    private String optional;

    public AckMessage(Short _messageOpcode, String _optional){
        messageOpcode = _messageOpcode;
        optional = _optional;
    }

    @Override
    public Message act(MessagingProtocolClass protocol) {
        return null;
    }

    @Override
    public boolean isAck() {
        return true;
    }

    public Short getMessageOpcode() {
        return messageOpcode;
    }

    public String getOptional() {
        return optional;
    }
}
