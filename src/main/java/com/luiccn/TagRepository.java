package com.luiccn;

import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.util.Collection;

public interface TagRepository {

    Collection<Tag> findById(Long fileId) throws IOException, SolrServerException;


}
