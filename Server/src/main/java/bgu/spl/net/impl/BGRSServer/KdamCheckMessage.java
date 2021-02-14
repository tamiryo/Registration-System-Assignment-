package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessagingProtocolClass;

import java.util.Arrays;

public class KdamCheckMessage extends Message {

    private Short courseNumber;

    public KdamCheckMessage(Short _courseNumber) {
        courseNumber = _courseNumber;
    }

    @Override
    public Message act(MessagingProtocolClass protocol) {
        Short messageOpcode = 6;
        if (protocol.getUser() == null || protocol.getUser().isAdmin()) {
            return new ErrorMessage(messageOpcode);
        }
        Database data = Database.getInstance();
        Course course = data.getCourseNumMap().get(courseNumber);
        if (course == null)
            return new ErrorMessage(messageOpcode);
        int[] kdam = course.getKdamCourseList();
        for (int i = 1; i < kdam.length; i = i + 1) {
            int v = kdam[i];
            int x = data.getCourseNumMap().get(Short.valueOf((short) kdam[i])).getIndex();
            while (i > 0 && data.getCourseNumMap().get(Short.valueOf((short) kdam[i-1])).getIndex() > x) {
                kdam[i] = kdam[i - 1];
                i = i - 1;
            }
            kdam[i] = v;
        }
        return new AckMessage(messageOpcode,Arrays.toString(kdam).replace(" ",""));
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
