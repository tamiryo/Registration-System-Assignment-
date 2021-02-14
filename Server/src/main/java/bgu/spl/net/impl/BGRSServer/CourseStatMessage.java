package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessagingProtocolClass;

import java.util.Arrays;

public class CourseStatMessage extends Message{

    private Short courseNumber;

    public CourseStatMessage(Short _courseNumber){
        courseNumber = _courseNumber;
    }

    @Override
    public Message act(MessagingProtocolClass protocol) {
        Short messageOpcode = 7;
        if (protocol.getUser() == null || !protocol.getUser().isAdmin()) {
            return new ErrorMessage(messageOpcode);
        }
        if (Database.getInstance().getCourseNumMap().get(courseNumber)==null) {
            return new ErrorMessage(messageOpcode);
        }
        synchronized (Database.getInstance().getCourseNumMap().get(courseNumber)) {
            Course course = Database.getInstance().getCourseNumMap().get(courseNumber);
            String courseStat = "Course: (" + courseNumber + ") " + course.getCourseName();
            int available = course.getNumOfMaxStudents() - course.getRegisteredStudents().size();
            String seatsStat = "Seats Available: " + available + "/" + course.getNumOfMaxStudents();
            Object[] regStudents = course.getRegisteredStudents().toArray();
            String temp;
            for (int i = 0; i < regStudents.length; i = i + 1) {
                for (int j = i + 1; j < regStudents.length; j = j + 1) {
                    String ob1 = (String) regStudents[i];
                    String ob2 = (String) regStudents[j];
                    if (ob1.compareTo(ob2) > 0) {
                        temp = (String) regStudents[i];
                        regStudents[i] = regStudents[j];
                        regStudents[j] = temp;
                    }
                }
            }
            String RegStat = "Students Registered: " + Arrays.toString(regStudents).replace(" ", "");
            String status = courseStat + "\n" + seatsStat + "\n" + RegStat;
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
