all:
	mkdir -p bin/
	javac -d bin/ -sourcepath src/ src/Machine.java
	javac -d bin/ -sourcepath src/ src/PageSwapper.java
	javac -d bin/ -sourcepath src/ src/SwapperOptimal.java
	javac -d bin/ -sourcepath src/ src/SwapperFIFO.java
	javac -d bin/ -sourcepath src/ src/SwapperLFU.java
	javac -d bin/ -sourcepath src/ src/SwapperLRU.java
	javac -d bin/ -sourcepath src/ src/SwapperMFU.java
	javac -d bin/ -sourcepath src/ src/PageSwapExample.java
	chmod +x test-everything.sh