@echo off
@title World Server
set CLASSPATH=.;lib\MapleStory.jar;lib\mina-core.jar;lib\slf4j-api.jar;lib\slf4j-jdk14.jar;lib\mysql-connector-java-bin.jar
java -Xmx100m -Dwzpath=wz\ -Djavax.net.ssl.keyStore=filename.keystore -Djavax.net.ssl.keyStorePassword=passwd -Djavax.net.ssl.trustStore=filename.keystore -Djavax.net.ssl.trustStorePassword=passwd -Drecvops=recvops.properties -Dsendops=sendops.properties net.world.WorldServer
pause