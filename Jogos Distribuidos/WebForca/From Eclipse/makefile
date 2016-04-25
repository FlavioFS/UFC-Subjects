JAVAC=javac
sources = $(wildcard Client.java ClientProtocol.java ConsoleView.java GameLogic.java Message.java PlayerBase.java Protocol.java Server.java ServerProtocol.java)
classes = $(sources:.java=.class)

all: $(classes)

clean :
	rm -f *.class

%.class : %.java
	$(JAVAC) $<