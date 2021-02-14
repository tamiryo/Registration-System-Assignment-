package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessageEncoderDecoderClass;
import bgu.spl.net.api.MessagingProtocolClass;
import bgu.spl.net.srv.Server;

public class ReactorMain {

    public static void main (String[] args) {
        Database data = Database.getInstance();
        data.initialize("./Courses.txt");
        Server.reactor(
                Integer.parseInt(args[1]),
                Integer.parseInt(args[0]),
                ()-> new MessagingProtocolClass(),
                ()-> new MessageEncoderDecoderClass()).serve();
    }
}
