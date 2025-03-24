cd server && mvn clean package && cd -

cd client && mvn clean package && cd -

java -jar server/target/server.jar &

java -jar client/target/client.jar


