package com.luiccn;

import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;

import java.util.ArrayList;
import java.util.Collection;


@org.springframework.data.solr.core.mapping.SolrDocument(solrCoreName = "tags")
public class Document {


    @Id
    @Indexed
    public String id;

    @Indexed
    public String path;

    @Indexed
    public String filename;

    @Indexed(name = "tags")
    public Collection<String> tags = new ArrayList<>();


    public Document(String id, String path, String filename, Collection<String> tags) {
        this.id = id;
        this.path = path;
        this.filename = filename;
        this.tags = tags;
    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Collection<String> getTags() {
        return tags;
    }

    public void setTags(Collection<String> tags) {
        this.tags = tags;
    }
}
