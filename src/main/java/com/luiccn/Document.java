package com.luiccn;

import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;

import java.util.ArrayList;
import java.util.Collection;


@org.springframework.data.solr.core.mapping.SolrDocument(solrCoreName = "tags")
public class Document {


    @Id
    @Indexed
    private String id;

    @Indexed
    private String path;

    @Indexed
    private String filename;

    @Indexed(name = "tags")
    private Collection<String> tags = new ArrayList<>();


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
        return Utils.unQuote(path);
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFilename() {
        return Utils.unQuote(filename);
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

    public void removeTag(String tag) {
        tags.removeIf(s -> s.equals(tag));
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Document{");
        sb.append("id='").append(id).append('\'');
        sb.append(", path='").append(path).append('\'');
        sb.append(", filename='").append(filename).append('\'');
        sb.append(", tags=").append(tags);
        sb.append('}');
        return sb.toString();
    }
}
