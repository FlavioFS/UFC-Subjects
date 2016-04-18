JAVAC=javac
sources = $(wildcard ClientStructure.java Baseview.java Hangman.java HangmanClient.java HangmanServer.java)
classes = $(sources:.java=.class)

all: $(classes)

clean :
	rm -f *.class

%.class : %.java
	$(JAVAC) $<