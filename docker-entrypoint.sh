java -Duser.timezone=Asia/Seoul \
-Dspring.profiles.active=${PROFILE} \
-Djasypt.encryptor.password=${JASYPT_PASSWORD}
-jar app.jar
