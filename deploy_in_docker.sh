# 暂未调试完成
mvn clean
mvn package -P prod
sudo docker pull java:8
sudo docker stop coding_first_api_sys
sudo docker rm coding_first_api_sys
sudo docker rmi coding_first_api_sys
sudo docker build -t coding_first_api_sys .
sudo docker run -p 8886:8886 -d --name coding_first_api_sys coding_first_api_sys

