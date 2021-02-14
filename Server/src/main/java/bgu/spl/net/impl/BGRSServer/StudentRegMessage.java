package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessagingProtocolClass;

public class StudentRegMessage extends Message{

    private String userName;
    private String password;

    public StudentRegMessage (String _userName, String _password) {
        userName = _userName;
        password = _password;
    }

    @Override
    public Message act(MessagingProtocolClass protocol) {
        Short messageOpcode = 2;
        Database data = Database.getInstance();
        synchronized (data.getKey()) {
            if (protocol.getUser() == null) { //no client is logged in
                if (data.getUserMap().containsKey(userName)) {
                    return new ErrorMessage(messageOpcode);
                } else {
                        if (!data.getUserMap().containsKey(userName)) {
                            data.getUserMap().putIfAbsent(userName, new User(userName, password, false));
                            return new AckMessage(messageOpcode, "");
                        }
                        return new ErrorMessage(messageOpcode);
                }
            }
            else //there is a user logged in
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
