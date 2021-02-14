package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessagingProtocolClass;

public class LoginMessage extends Message{

    private String userName;
    private String password;

    public LoginMessage (String _userName, String _password) {
        userName = _userName;
        password = _password;
    }

    @Override
    public Message act(MessagingProtocolClass protocol) {
        Short messageOpcode = 3;
        Database data = Database.getInstance();
        synchronized (data.getKey()) {
            if (protocol.getUser() == null) {  //no client is logged in
                if (!data.getUserMap().containsKey(userName)) {
                    return new ErrorMessage(messageOpcode);
                }
                User user = data.getUserMap().get(userName);
                if (!user.getPassword().equals(password)) {
                    return new ErrorMessage(messageOpcode);
                }
                if (user.isLog()) {
                    return new ErrorMessage(messageOpcode);
                }
                protocol.setUser(user);
                user.setLog(true);
                return new AckMessage(messageOpcode, "");
            } else //there is a user logged in
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
