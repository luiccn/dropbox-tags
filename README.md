sudo mvn clean package docker:build
sudo docker run -p 8080:8080 -t luiccn/dropbox-tags

java -jar -Ddropbox.download_size=500 target/blabla

sudo mvn clean package docker:build
sudo docker rm -f java
sudo sudo docker run -p 8080:8080 --name java --link my_solr:my_solr -t luiccn/dropbox-tags
