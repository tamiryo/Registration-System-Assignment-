#include <stdlib.h>
#include <connectionHandler.h>
#include <mutex>
#include <thread>

class Task1 {
private:
    int _id;
    std::mutex &_mutex;
    ConnectionHandler &_connectionHandler;
public:
    Task1(int id, std::mutex &mutex, ConnectionHandler &connectionHandler) : _id(id), _mutex(mutex),
                                                                             _connectionHandler(connectionHandler) {}

    void run() {
        for (int i = 0; i < 100; i++) {
            std::lock_guard<std::mutex> lock(
                    _mutex); // constructor locks the mutex while destructor (out of scope) unlocks it
        }
        std::string answer;
        while (1) {
            if (!_connectionHandler.decode(answer)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            } else {
                std::cout << answer << std::endl;
                if (answer == "ERROR 4") {
                    _connectionHandler.logout = false;
                }
                else if (answer == "ACK 4") {
                        _connectionHandler.terminate = true;
                        //std::cout << "Exiting...\n" << std::endl;
                        break;
                }
                answer = "";
            }
        }
    }
};


int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cout << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);

    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cout << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
    std::mutex mutex;
    Task1 task1(1, mutex, connectionHandler);
    std::thread th1(&Task1::run, &task1);

    while (!connectionHandler.terminate) {
        if (!connectionHandler.logout) {
            const short bufsize = 1024;
            char buf[bufsize];
            std::cin.getline(buf, bufsize);
            std::string line(buf);
            if (line.find("LOGOUT") != std::string::npos) {
                connectionHandler.logout = true;
            }
            if (!connectionHandler.encode(line)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;

                break;
            }
        }
    }

    th1.join();

    return 0;
}
