package com.luiccn;

import org.apache.solr.common.SolrDocument;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;

import java.util.ArrayList;
import java.util.Collection;


@org.springframework.data.solr.core.mapping.SolrDocument(solrCoreName = "tags")
public class Document extends SolrDocument{


    @Id
    @Indexed
    private String id;

    @Indexed(name = "tags")
    private Collection<String> tags = new ArrayList<>();

    public Document(String id, Collection<String> tags) {
        this.id = id;
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public Collection<String> getTags() {
        return tags;
    }

    public void setId(String id) {
        this.id = id;
    }


    public void setTags(Collection<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Document{");
        sb.append("id='").append(id).append('\'');
        sb.append(", tags=").append(tags);
        sb.append('}');
        return sb.toString();
    }
}
