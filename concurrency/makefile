# Assignment 4 makefile
# FRBMAR009
# 27/09/2019

JAVAC = javac
JFLAGS = -g
SRC = src/
DOC = doc/
BIN = bin/
JAVADOC = javadoc

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) -cp .:./bin -d $(BIN) $(JFLAGS) $*.java

CLASSES =$(SRC)*.java

default: classes

classes: $(CLASSES:.java=.class)

run: 
	java -cp bin WordApp 15 7 7

docs: $(JAVADOC) -d $(DOC) $(SRC)*.java

clean: $(RM) $(BIN)*.class
