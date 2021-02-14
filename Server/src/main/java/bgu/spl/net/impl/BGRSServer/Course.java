package bgu.spl.net.impl.BGRSServer;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Course implements Comparable<Course> {

    private Short courseNum;
    private String courseName;
    private int [] kdamCourseList;
    private int numOfMaxStudents;
    private ConcurrentLinkedQueue<String> registeredStudents;
    private int index;

    public Course(Short courseNum,String courseName,int [] kdamCourseList,int numOfMaxStudents,int index){
        this.courseNum = courseNum;
        this.courseName = courseName;
        this.kdamCourseList = kdamCourseList;
        this.numOfMaxStudents = numOfMaxStudents;
        this.registeredStudents = new ConcurrentLinkedQueue<>();
        this.index = index;
    }

    public int getCourseNum() {
        return courseNum;
    }

    public String getCourseName() {
        return courseName;
    }

    public int [] getKdamCourseList() {
        return kdamCourseList;
    }

    public int getNumOfMaxStudents() {
        return numOfMaxStudents;
    }

    public ConcurrentLinkedQueue<String> getRegisteredStudents() {
        return registeredStudents;
    }

    public boolean isFull() {
        return numOfMaxStudents==registeredStudents.size();
    }

    public int getIndex() {
        return index;
    }

    @Override
    public int compareTo(Course o) {
        return (this.index-o.getIndex());
    }
}
