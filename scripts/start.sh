#!/usr/bin/env bash

REPOSITORY=/home/ec2-user/app
PROJECT_NAME=domain-expiration

echo "> Build 파일 복사"
echo "> cp $REPOSITORY/deploy/domain-expiration-0.0.1-SNAPSHOT.jar $REPOSITORY/"
cp $REPOSITORY/deploy/domain-expiration-0.0.1-SNAPSHOT.jar $REPOSITORY/

echo "> 새 어플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"
nohup java -jar $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &