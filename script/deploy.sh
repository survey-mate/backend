#! /bin/bash
REPOSITORY=/home/ubuntu/survey-mate
PROJECT_NAME=survey-mate

echo "> 현재 구동중인 어플리케이션 확인"
CURRENT_PID=$(pgrep -f "survey-mate.*\.jar" | awk '{print $1}')

echo "> 현재 구동중인 어플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
    echo "> 현재 구동중인 어플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 새 어플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR_NAME: $JAR_NAME"

echo "> $JAR_NAME에 실행 권한 추가"
chmod +x $JAR_NAME

echo "> 환경 변수 불러오기"
source ~/.bashrc

echo ">$JAR_NAME 실행"
nohup java -jar \
  $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
