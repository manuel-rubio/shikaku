#!/bin/sh

cd $(dirname $0)
cd ../src

javac shikaku.java shikaku/tablero/*.java
mv *.class ../bin
rm -rf ../bin/shikaku
mkdir -p ../bin/shikaku/tablero
mv shikaku/tablero/*.class ../bin/shikaku/tablero


