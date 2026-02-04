#!/bin/bash

# Tester la connexion JDBC directement
cd LALANA-SIG

# Compiler et tester
javac -cp "lib/*:build/classes" -d build/classes src/main/java/TestConnexion.java 2>&1

java -cp "lib/*:build/classes" TestConnexion 2>&1

