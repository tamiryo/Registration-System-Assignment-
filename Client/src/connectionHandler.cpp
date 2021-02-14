#include <connectionHandler.h>
 
using boost::asio::ip::tcp;

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;
 
ConnectionHandler::ConnectionHandler(string host, short port): host_(host), port_(port), io_service_(), socket_(io_service_), logout(false), terminate(false){}
    
ConnectionHandler::~ConnectionHandler() {
    close();
}
 
bool ConnectionHandler::connect() {
    std::cout << "Starting connect to " 
        << host_ << ":" << port_ << std::endl;
    try {
		tcp::endpoint endpoint(boost::asio::ip::address::from_string(host_), port_); // the server endpoint
		boost::system::error_code error;
		socket_.connect(endpoint, error);
		if (error)
			throw boost::system::system_error(error);
    }
    catch (std::exception& e) {
        std::cout << "Connection failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::getBytes(char bytes[], unsigned int bytesToRead) {
    size_t tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToRead > tmp ) {
			tmp += socket_.read_some(boost::asio::buffer(bytes+tmp, bytesToRead-tmp), error);			
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cout << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::sendBytes(const char bytes[], int bytesToWrite) {
    int tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToWrite > tmp ) {
			tmp += socket_.write_some(boost::asio::buffer(bytes + tmp, bytesToWrite - tmp), error);
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cout << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::getLine(std::string& line) {
    return getFrameAscii(line, '\0');
}

bool ConnectionHandler::sendLine(std::string& line) {
    return sendFrameAscii(line, '\0');
}
 

bool ConnectionHandler::getFrameAscii(std::string& frame, char delimiter) {
    char ch;
    // Stop when we encounter the null character.
    // Notice that the null character is not appended to the frame string.
    try {
	do{
		if(!getBytes(&ch, 1))
		{
			return false;
		}
		if(ch!='\0')  
			frame.append(1, ch);
	}while (delimiter != ch);
    } catch (std::exception& e) {
	std::cout << "recv failed2 (Error: " << e.what() << ')' << std::endl;
	return false;
    }
    return true;
}
 
 
bool ConnectionHandler::sendFrameAscii(const std::string& frame, char delimiter) {
	bool result=sendBytes(frame.c_str(),frame.length());
	if(!result) return false;
	return sendBytes(&delimiter,1);
}
 
// Close down the connection properly.
void ConnectionHandler::close() {
    try{
        socket_.close();
    } catch (...) {
        std::cout << "closing failed: connection already closed" << std::endl;
    }
}

short ConnectionHandler::bytesToShort(char* bytesArr)
{
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}

void ConnectionHandler::shortToBytes(short num, char* bytesArr)
{
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}

bool ConnectionHandler::encode(string line) {
    short opcode = 0;
    int index = 0;
    if (line.find("ADMINREG") != std::string::npos) {
        opcode = 1;
        char opcodeArray[2];
        shortToBytes(opcode, opcodeArray);
        sendBytes(opcodeArray, 2);
        index = line.find_first_of(" ", 0);
        line.erase(0, index + 1);
        line = replaceChar(line, ' ', '\0');
        return sendLine(line);
    }
    if (line.find("STUDENTREG") != std::string::npos) {
        opcode = 2;
        char opcodeArray[2];
        shortToBytes(opcode, opcodeArray);
        sendBytes(opcodeArray, 2);
        index = line.find_first_of(' ', 0);
        line.erase(0, index + 1);
        line = replaceChar(line,' ', '\0');
        return sendLine(line);
    }
    if (line.find("LOGIN") != std::string::npos) {
        opcode = 3;
        char opcodeArray[2];
        shortToBytes(opcode, opcodeArray);
        sendBytes(opcodeArray, 2);
        index = line.find_first_of(' ', 0);
        line.erase(0, index + 1);
        line = replaceChar(line,' ', '\0');
        return sendLine(line);
    }
    if (line.find("LOGOUT") != std::string::npos) {
        opcode = 4;
        char opcodeArray[2];
        shortToBytes(opcode, opcodeArray);
        return sendBytes(opcodeArray, 2);
    }
    if (line.find("COURSEREG") != std::string::npos) {
        opcode = 5;
        char opcodeArray[2];
        shortToBytes(opcode, opcodeArray);
        sendBytes(opcodeArray, 2);
        index = line.find_first_of(' ', 0);
        line.erase(0, index + 1);
        short course = (short) std::stoi(line);
        char courseNumber[2];
        shortToBytes(course, courseNumber);
        return sendBytes(courseNumber, 2);
    }
    if (line.find("KDAMCHECK") != std::string::npos) {
        opcode = 6;
        char opcodeArray[2];
        shortToBytes(opcode, opcodeArray);
        sendBytes(opcodeArray, 2);
        index = line.find_first_of(' ', 0);
        line.erase(0, index + 1);
        short course = (short) std::stoi(line);
        char courseNumber[2];
        shortToBytes(course, courseNumber);
        return sendBytes(courseNumber, 2);
    }
    if (line.find("COURSESTAT") != std::string::npos) {
        opcode = 7;
        char opcodeArray[2];
        shortToBytes(opcode, opcodeArray);
        sendBytes(opcodeArray, 2);
        index = line.find_first_of(' ', 0);
        line.erase(0, index + 1);
        short course = (short) std::stoi(line);
        char courseNumber[2];
        shortToBytes(course, courseNumber);
        return sendBytes(courseNumber, 2);
    }
    if (line.find("STUDENTSTAT") != std::string::npos) {
        opcode = 8;
        char opcodeArray[2];
        shortToBytes(opcode, opcodeArray);
        sendBytes(opcodeArray, 2);
        index = line.find_first_of(' ', 0);
        line.erase(0, index + 1);
        return sendLine(line);
    }
    if (line.find("ISREGISTERED") != std::string::npos) {
        opcode = 9;
        char opcodeArray[2];
        shortToBytes(opcode, opcodeArray);
        sendBytes(opcodeArray, 2);
        index = line.find_first_of(' ', 0);
        line.erase(0, index + 1);
        short course = (short) std::stoi(line);
        char courseNumber[2];
        shortToBytes(course, courseNumber);
        return sendBytes(courseNumber, 2);
    }
    if (line.find("UNREGISTER") != std::string::npos) {
        opcode = 10;
        char opcodeArray[2];
        shortToBytes(opcode, opcodeArray);
        sendBytes(opcodeArray, 2);
        index = line.find_first_of(' ', 0);
        line.erase(0, index + 1);
        short course = (short) std::stoi(line);
        char courseNumber[2];
        shortToBytes(course, courseNumber);
        return sendBytes(courseNumber, 2);
    }
    if (line.find("MYCOURSES") != std::string::npos) {
        opcode = 11;
        char opcodeArray[2];
        shortToBytes(opcode, opcodeArray);
        return sendBytes(opcodeArray, 2);
    }
    return false;
}

std::string ConnectionHandler::replaceChar(string str, char ch1, char ch2) {
    int length = str.length();
    for (int i = 0; i < length; ++i) {
        if (str[i] == ch1)
            str[i] = ch2;
    }
    return str;
}

bool ConnectionHandler::decode(string &line) {
    char opcode [2];
    getBytes(opcode , 2);
    short opcodeNumber = bytesToShort(opcode);
    if (opcodeNumber == 12) {
        char operationOpcode [2];
        getBytes(operationOpcode,2);
        short operationNumber = bytesToShort(operationOpcode);
        std::string toPrint;
        getLine(toPrint);
        if (toPrint.empty())
            line = "ACK " + std::to_string(operationNumber);
        else
            line = "ACK " + std::to_string(operationNumber) + toPrint;
    }
    else {
        char operationOpcode [2];
        getBytes(operationOpcode,2);
        short operationNumber = bytesToShort(operationOpcode);
        line = "ERROR " + std::to_string(operationNumber);
    }
    return true;
}
