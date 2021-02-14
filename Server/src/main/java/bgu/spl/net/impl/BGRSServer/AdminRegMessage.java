package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessagingProtocolClass;

public class AdminRegMessage extends Message {

    private String userName;
    private String password;

    public AdminRegMessage (String _userName, String _password) {
        userName = _userName;
        password = _password;
    }

    @Override
    public Message act(MessagingProtocolClass protocol) {
        Short messageOpcode = 1;
        if (protocol.getUser() == null) { //no client is logged in
            Database data = Database.getInstance();
            if (data.getUserMap().containsKey(userName)){
                return new ErrorMessage(messageOpcode);
            }
            else {
                synchronized (data.getKey()){
                    if(!data.getUserMap().containsKey(userName)){
                        data.getUserMap().putIfAbsent(userName, new User(userName,password,true));
                        return new AckMessage(messageOpcode,"");
                    }
                    return new ErrorMessage(messageOpcode);
                }
            }
        }
        else //there is a user logged in
            return new ErrorMessage(messageOpcode);
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
