all:
	mkdir -p exec/
	g++ -o exec/q2 src/q2/q2.cpp -lpthread
	
	mkdir -p class/q3
	javac -d class/q3 -sourcepath src/q3 src/q3/DateClient.java
	javac -d class/q3 -sourcepath src/q3 src/q3/DateServer.java
	javac -d class/q3 -sourcepath src/q3 src/q3/Worker.java
	
	mkdir -p class/q4
	javac -d class/q4 -sourcepath src/q4 src/q4/Worker.java
	javac -d class/q4 -sourcepath src/q4 src/q4/DateClient.java
	javac -d class/q4 -sourcepath src/q4 src/q4/DateServer.java