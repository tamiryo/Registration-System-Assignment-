package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessagingProtocolClass;

public abstract class Message {

    public abstract Message act(MessagingProtocolClass protocol);

    public abstract boolean isAck();

    public abstract Short getMessageOpcode();

    public abstract String getOptional();

}
