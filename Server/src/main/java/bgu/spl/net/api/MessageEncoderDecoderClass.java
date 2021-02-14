package bgu.spl.net.api;
import bgu.spl.net.impl.BGRSServer.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MessageEncoderDecoderClass implements MessageEncoderDecoder<Message> {

    private byte[] bytes = new byte[1 << 10];
    byte [] opcodeArray = new byte[2];
    private int len = 0;
    private int opcodeLen = 0;
    private short opcode = -1;
    private int opcodeCounter = -1;
    private String password = "";
    private String userName = "";

    public MessageEncoderDecoderClass() {}

    @Override
    public Message decodeNextByte(byte nextByte) {
        if (opcode == -1){
            opcodeArray[opcodeLen] = nextByte;
            if (opcodeLen==1){
                opcode = bytesToShort(opcodeArray);
                opcodeCounter = getCounter(opcode);
                opcodeLen = 0;
                if (opcode==4 || opcode==11)
                    return getOperation(opcode,nextByte);
                else
                    return null;
            }
            else {
                opcodeLen = opcodeLen + 1;
                return null;
            }

        }
        else
            return getOperation(opcode,nextByte);
    }

    private Message getOperation(short opcode, byte nextByte ) {

        if (opcode == 1) { //ADMINREG
            if (nextByte == '\0') {
                if (opcodeCounter == 2) {
                    userName = popString();
                    opcodeCounter = opcodeCounter - 1;
                    return null;
                }
                else if (opcodeCounter == 1) {
                    password = popString();
                    opcodeCounter = -1;
                    this.opcode = -1;
                    return new AdminRegMessage(userName, password);
                }
            }
            else {
                pushByte(nextByte);
                return null;
            }
        }

        if (opcode == 2){ //STUDENTREG
            if (nextByte == '\0') {
                if (opcodeCounter == 2) {
                    userName = popString();
                    opcodeCounter = opcodeCounter - 1;
                    return null;
                }
                else if (opcodeCounter == 1) {
                    password = popString();
                    opcodeCounter = -1;
                    this.opcode = -1;
                    return new StudentRegMessage(userName, password);
                }
            }
            else {
                pushByte(nextByte);
                return null;
            }
        }

        if (opcode == 3){ //LOGIN
            if (nextByte == '\0') {
                if (opcodeCounter == 2) {
                    userName = popString();
                    opcodeCounter = opcodeCounter - 1;
                    return null;
                }
                else if (opcodeCounter == 1) {
                    password = popString();
                    opcodeCounter = -1;
                    this.opcode = -1;
                    return new LoginMessage(userName, password);
                }
            }
            else {
                pushByte(nextByte);
                return null;
            }
        }

        if (opcode == 4){ //LOGOUT
            this.opcode = -1;
            opcodeCounter = -1;
            return new LogoutMessage();
        }

        if (opcode == 5){ //COURSEREG
            if (opcodeLen < 2) {
                opcodeArray[opcodeLen] = nextByte;
                if (opcodeLen == 0) {
                    opcodeLen = opcodeLen + 1;
                    return null;
                } else {
                    opcodeLen = 0;
                    Short courseNumber = bytesToShort(opcodeArray);
                    this.opcode = -1;
                    return new CourseRegMessage(courseNumber);
                }
            }
        }

        if (opcode == 6){ //KDAMCHECK
            if (opcodeLen < 2) {
                opcodeArray[opcodeLen] = nextByte;
                if (opcodeLen == 0) {
                    opcodeLen = opcodeLen + 1;
                    return null;
                } else {
                    opcodeLen = 0;
                    Short courseNumber = bytesToShort(opcodeArray);
                    this.opcode = -1;
                    return new KdamCheckMessage(courseNumber);
                }
            }
        }
        if (opcode == 7){ //COURSESTAT
            if (opcodeLen < 2) {
                opcodeArray[opcodeLen] = nextByte;
                if (opcodeLen == 0) {
                    opcodeLen = opcodeLen + 1;
                    return null;
                } else {
                    opcodeLen = 0;
                    Short courseNumber = bytesToShort(opcodeArray);
                    this.opcode = -1;
                    return new CourseStatMessage(courseNumber);
                }
            }
        }
        if (opcode == 8){ //STUDENTSTAT
            if (nextByte == '\0') {
                userName = popString();
                opcodeCounter = -1;
                this.opcode = -1;
                return new StudentStatMessage(userName);
            }
            else {
                pushByte(nextByte);
                return null;
            }
        }

        if (opcode == 9){ //ISREGISTERED
            if (opcodeLen < 2) {
                opcodeArray[opcodeLen] = nextByte;
                if (opcodeLen == 0) {
                    opcodeLen = opcodeLen + 1;
                    return null;
                } else {
                    opcodeLen = 0;
                    Short courseNumber = bytesToShort(opcodeArray);
                    this.opcode = -1;
                    return new IsRegisteredMessage(courseNumber);
                }
            }
        }

        if (opcode == 10){ //UNREGISTERED
            if (opcodeLen < 2) {
                opcodeArray[opcodeLen] = nextByte;
                if (opcodeLen == 0) {
                    opcodeLen = opcodeLen + 1;
                    return null;
                } else {
                    opcodeLen = 0;
                    Short courseNumber = bytesToShort(opcodeArray);
                    this.opcode = -1;
                    return new UnregisterMessage(courseNumber);
                }
            }
        }

        if (opcode == 11){ //MYCOURSES
            this.opcode = -1;
            opcodeCounter = -1;
            return new MyCoursesMessage();
        }
        return null;
    }

    private int getCounter(short opcode) {

        if (opcode == 1){
            opcodeCounter = 2;
            return opcodeCounter;
        }
        if (opcode == 2){
            opcodeCounter = 2;
            return opcodeCounter;
        }
        if (opcode == 3){
            opcodeCounter = 2;
            return opcodeCounter;
        }
        if (opcode == 4){
            opcodeCounter = 0;
            return opcodeCounter;
        }
        if (opcode == 5){
            opcodeCounter = 0;
            return opcodeCounter;
        }
        if (opcode == 6){
            opcodeCounter = 0;
            return opcodeCounter;
        }
        if (opcode == 7){
            opcodeCounter = 0;
            return opcodeCounter;
        }
        if (opcode == 8){
            opcodeCounter = 1;
            return opcodeCounter;
        }
        if (opcode == 9){
            opcodeCounter = 0;
            return opcodeCounter;
        }
        if (opcode == 10){
            opcodeCounter = 0;
            return opcodeCounter;
        }
        if (opcode == 11){
            opcodeCounter = 0;
            return opcodeCounter;
        }
        return opcodeCounter;
    }

    private short bytesToShort(byte [] byteArray) {
        short result = (short)((byteArray[0] & 0xff) << 8);
        result += (short)(byteArray[1] & 0xff);
        return result;
    }

    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    private String popString() {
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }

    @Override
    public byte[] encode(Message message) {
        if (message.isAck()) {
            Short ackOpcode = 12;
            byte [] ackOpcodeArray = shortToBytes(ackOpcode);
            Short messageOpcode = message.getMessageOpcode();
            byte [] messageOpcodeArray = shortToBytes(messageOpcode);
            String optional = message.getOptional();
            if (!optional.isEmpty())
                optional = "\n" + optional + "\0";
            else
                optional = optional + "\0";
            byte [] optionalArray = optional.getBytes();
            int size = 4 + optionalArray.length;
            byte [] ansArray = new byte[size];
            for (int i=0 ; i<2 ; i=i+1) {
                ansArray[i] = ackOpcodeArray[i];
            }
            for (int i=2 ; i<4 ; i=i+1) {
                ansArray[i] = messageOpcodeArray[i-2];
            }
            for (int i=4 ; i<ansArray.length ; i=i+1) {
                ansArray[i] = optionalArray[i-4];
            }
            return ansArray;
        }
        else {
            Short ackOpcode = 13;
            byte [] ackOpcodeArray = shortToBytes(ackOpcode);
            Short messageOpcode = message.getMessageOpcode();
            byte [] messageOpcodeArray = shortToBytes(messageOpcode);
            byte [] ansArray = new byte[4];
            for (int i=0 ; i<2 ; i=i+1) {
                ansArray[i] = ackOpcodeArray[i];
            }
            for (int i=2 ; i<4 ; i=i+1) {
                ansArray[i] = messageOpcodeArray[i-2];
            }
            return ansArray;
        }
    }

}
