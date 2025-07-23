JAVAC   := javac
CC      := gcc

CFLAGS  := -I. \
           -fPIC \
           -I"C:/Program Files/Java/jdk-21/include" \
           -I"C:/Program Files/Java/jdk-21/include/win32"
LDFLAGS := -shared

all: sorter.dll Sorter.class

Sorter.class: src/Sorter.java
	$(JAVAC) -d . src/Sorter.java

Sorter.h: src/Sorter.java
	$(JAVAC) -h . -d . src/Sorter.java

sorter.dll: native/sorter.c Sorter.h
	$(CC) $(CFLAGS) native/sorter.c -o sorter.dll -shared

run: all
	java -Djava.library.path=. Sorter

clean:
	del Sorter.class Sorter.h sorter.dll 2>nul || rm -f Sorter.class Sorter.h sorter.dll
