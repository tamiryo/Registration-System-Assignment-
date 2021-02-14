package bgu.spl.net.impl.BGRSServer;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class User {

    private String userName;
    private String password;
    private boolean isAdmin; //true for admin, false for student
    private ConcurrentLinkedQueue<Integer> courses = new ConcurrentLinkedQueue<>();
    private boolean isLog = false;

    public User(String userName, String password, boolean isAdmin){
        this.userName = userName;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public ConcurrentLinkedQueue<Integer> getCourses() {
        return courses;
    }

    public boolean isLog() {
        return isLog;
    }

    public void setLog(boolean status) {
        isLog = status;
    }

    public boolean kdamCheck(Short courseNum) {
        Course course = Database.getInstance().getCourseNumMap().get(courseNum);
        for (int kdamNum : course.getKdamCourseList()) {
            if (!courses.contains(kdamNum))
                return false;
        }
        return true;
    }
}
