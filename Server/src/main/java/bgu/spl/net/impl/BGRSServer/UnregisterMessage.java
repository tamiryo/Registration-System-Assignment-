package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessagingProtocolClass;

public class UnregisterMessage extends Message{

    private Short courseNumber;

    public UnregisterMessage(Short _courseNumber){
        courseNumber = _courseNumber;
    }

    @Override
    public Message act(MessagingProtocolClass protocol) {
        Short messageOpcode = 10;
        if (protocol.getUser()==null || protocol.getUser().isAdmin()) {
            return new ErrorMessage(messageOpcode);
        }
        synchronized (Database.getInstance().getCourseNumMap().get(courseNumber)) {
            Course course = Database.getInstance().getCourseNumMap().get(courseNumber);
            if (course == null) {
                return new ErrorMessage(messageOpcode);
            }
            User student = protocol.getUser();
            if (!student.getCourses().contains(Integer.valueOf(courseNumber))) {
                return new ErrorMessage(messageOpcode);
            }
            student.getCourses().remove(Integer.valueOf(courseNumber));
            course.getRegisteredStudents().remove(student.getUserName());
            return new AckMessage(messageOpcode, "");
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
