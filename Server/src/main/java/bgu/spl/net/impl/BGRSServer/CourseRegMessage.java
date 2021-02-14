package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessagingProtocolClass;


public class CourseRegMessage extends Message{

    private Short courseNumber;

    public CourseRegMessage(Short _courseNumber){
        courseNumber = _courseNumber;
    }

    @Override
    public Message act(MessagingProtocolClass protocol) {
        Short messageOpcode = 5;
        if (protocol.getUser() == null || protocol.getUser().isAdmin()) {
            return new ErrorMessage(messageOpcode);
        }
        synchronized (Database.getInstance().getKey()) {
            Course course = Database.getInstance().getCourseNumMap().get(courseNumber);
            if (course == null || course.isFull() || !protocol.getUser().kdamCheck(courseNumber)) {
                return new ErrorMessage(messageOpcode);
            }
            if (protocol.getUser().getCourses().contains(Integer.valueOf(courseNumber))) {
                return new ErrorMessage(messageOpcode);
            }
            if (!protocol.getUser().getCourses().contains(Integer.valueOf(courseNumber))) {
                protocol.getUser().getCourses().add(Integer.valueOf(courseNumber));
                course.getRegisteredStudents().add(protocol.getUser().getUserName());
                return new AckMessage(messageOpcode, "");
            }
            return new ErrorMessage(messageOpcode);
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
