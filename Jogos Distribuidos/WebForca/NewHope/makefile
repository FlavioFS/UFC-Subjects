JAVAC=javac
sources = $(wildcard Baseview.java Hangman.java Message.java Protocol.java ClientProtocol.java ServerProtocol.java Client.java Server.java)
classes = $(sources:.java=.class)

all: $(classes)

clean :
	rm -f *.class

%.class : %.java
	$(JAVAC) $<