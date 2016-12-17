sudo mvn clean package docker:build
sudo docker run -p 8080:8080 -t luiccn/dropbox-tags


sudo mvn clean package docker:build
sudo docker rm -f java

sudo docker run -e SOLR_HOST="http://my_solr:8983/solr" -e DROPBOX_DOWNLOADSIZE=1  -p 8080:8080 --name java --link my_solr:my_solr -t luiccn/dropbox-tags