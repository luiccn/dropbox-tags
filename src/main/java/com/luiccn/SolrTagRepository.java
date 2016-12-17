package com.luiccn;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

public class SolrTagRepository implements TagRepository{

    private HttpSolrClient solr;

    public SolrTagRepository(HttpSolrClient solr) {
        this.solr = solr;
    }

    @Override
    public Collection<Tag> findById(Long fileId) throws IOException, SolrServerException {
        SolrQuery query = new SolrQuery("file_id:"+fileId.toString());

        QueryResponse queryResponse = solr.query(query);

        SolrDocumentList results = queryResponse.getResults();

        return results.stream().map(r -> (Tag) r.get("tags")).collect(Collectors.toList());
    }
}
