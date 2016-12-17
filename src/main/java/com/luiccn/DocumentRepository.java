package com.luiccn;

import org.springframework.data.solr.repository.SolrCrudRepository;

public interface DocumentRepository extends SolrCrudRepository<Document, String> {

    @Override
    long count();

    Document findById(String id);

    Document findByTags(String tag);
}
