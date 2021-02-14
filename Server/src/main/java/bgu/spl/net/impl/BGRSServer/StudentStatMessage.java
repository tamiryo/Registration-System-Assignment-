package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessagingProtocolClass;

import java.util.Arrays;

public class StudentStatMessage extends Message{


    private String studentUserName;

    public StudentStatMessage(String _studentUserName){
        studentUserName = _studentUserName;
    }

    @Override
    public Message act(MessagingProtocolClass protocol) {
        Short messageOpcode = 8;
        if (protocol.getUser()==null || !protocol.getUser().isAdmin()) {
            return new ErrorMessage(messageOpcode);
        }
        Database data = Database.getInstance();
        if (data.getUserMap().get(studentUserName) == null) {
            return new ErrorMessage(messageOpcode);
        }
        synchronized (data.getUserMap().get(studentUserName)) {
            User student = data.getUserMap().get(studentUserName);
            if (student == null || student.isAdmin()) {
                return new ErrorMessage(messageOpcode);
            }
            String name = "Student: " + student.getUserName();
            Object[] courses = student.getCourses().toArray();
            for (int i = 1; i < courses.length; i = i + 1) {
                int v = (int) courses[i];
                int x = data.getCourseNumMap().get(Short.valueOf((short) (int) courses[i])).getIndex();
                while (i > 0 && data.getCourseNumMap().get(Short.valueOf((short) (int) courses[i - 1])).getIndex() > x) {
                    courses[i] = courses[i - 1];
                    i = i - 1;
                }
                courses[i] = v;
            }
            String coursesList = "Courses: " + Arrays.toString(courses).replace(" ", "");
            String status = name + "\n" + coursesList;
            return new AckMessage(messageOpcode, status);
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
