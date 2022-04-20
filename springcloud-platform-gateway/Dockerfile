FROM 10.9.127.243:30002/public/jdk11:1.0-dev
VOLUME /var/log
ADD target/*.jar app.jar
ARG JAVA_OPTS="-Xms2g -Xmx2g -Xmn1g "
ARG JVM_ARGS="-server \
-XX:+DisableExplicitGC \
-XX:+HeapDumpOnOutOfMemoryError \
-XX:+PrintGCDetails \
-XX:+PrintGCTimeStamps \
-XX:+PrintHeapAtGC \
-XX:+PrintGCApplicationStoppedTime \
-XX:+PrintGCDateStamps \
-XX:ErrorFile=/var/log/hs_err_pid%p.log \
-XX:HeapDumpPath=/var/log -Xloggc:/var/log/gc%t.log"
ENV TZ=Asia/Shanghai
ENV JAVA_OPTS=${JAVA_OPTS}
ENV JVM_ARGS=${JVM_ARGS}
ENTRYPOINT exec java ${JAVA_OPTS}  ${JVM_SERVER_ARGS}  -jar /app.jar  --spring.profiles.active=dev
