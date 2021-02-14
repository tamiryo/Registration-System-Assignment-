package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessagingProtocolClass;

import java.util.Arrays;

public class MyCoursesMessage extends Message{

    public MyCoursesMessage(){

    }

    @Override
    public Message act(MessagingProtocolClass protocol) {
        Short messageOpcode = 11;
        if (protocol.getUser()==null || protocol.getUser().isAdmin()) {
            return new ErrorMessage(messageOpcode);
        }
        Database data = Database.getInstance();
        User student = protocol.getUser();
        Object [] courses = student.getCourses().toArray();
        for (int i = 1; i < courses.length; i = i + 1) {
            int v = (int)courses[i];
            int x = data.getCourseNumMap().get(Short.valueOf((short)(int)courses[i])).getIndex();
            while (i > 0 && data.getCourseNumMap().get(Short.valueOf((short)(int)courses[i-1])).getIndex() > x) {
                courses[i] = courses[i - 1];
                i = i - 1;
            }
            courses[i] = v;
        }
        String coursesList = Arrays.toString(courses).replace(" ", "");
        return new AckMessage(messageOpcode,coursesList);
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
