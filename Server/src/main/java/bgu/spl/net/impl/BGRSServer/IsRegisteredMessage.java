package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessagingProtocolClass;

public class IsRegisteredMessage extends Message{

    private Short courseNumber;

    public IsRegisteredMessage(Short _courseNumber){
        courseNumber = _courseNumber;
    }

    @Override
    public Message act(MessagingProtocolClass protocol) {
        Short messageOpcode = 9;
        if (protocol.getUser()==null || protocol.getUser().isAdmin()) {
            return new ErrorMessage(messageOpcode);
        }
        synchronized (Database.getInstance().getCourseNumMap().get(courseNumber)) {
            User student = protocol.getUser();
            if (Database.getInstance().getCourseNumMap().get(courseNumber) == null) {
                return new ErrorMessage(messageOpcode);
            }
            if (student.getCourses().contains(Integer.valueOf(courseNumber))) {
                return new AckMessage(messageOpcode, "REGISTERED");
            } else
                return new AckMessage(messageOpcode, "NOT REGISTERED");
        }
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
