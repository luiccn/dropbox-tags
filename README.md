sudo mvn clean package docker:build
sudo docker run -p 8080:8080 -t luiccn/dropbox-tags
