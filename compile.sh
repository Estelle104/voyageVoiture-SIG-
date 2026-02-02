javac -d bin -cp ".:lib/*" model/*.java dao/*.java service/*.java utildb/*.java sig/*.java affichage/*.java

java -cp "bin:lib/*" TestConnexion