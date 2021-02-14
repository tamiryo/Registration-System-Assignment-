CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude 
LDFLAGS:=-lboost_system -lboost_thread -pthread


all: bin/BGRSclient


bin/BGRSclient: bin/echoClient.o bin/connectionHandler.o
	@echo 'Building target: Client'
	@echo 'Invoking: C++ Linker'
	g++ -Wall -Weffc++ -o bin/BGRSclient bin/echoClient.o bin/connectionHandler.o $(LDFLAGS)
	@echo 'Finished building target: Client'
	@echo ' '

bin/echoClient.o: src/echoClient.cpp
	g++ $(CFLAGS) -o bin/echoClient.o src/echoClient.cpp

bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp $(LDFLAGS)

clean:
	rm -f bin/*
