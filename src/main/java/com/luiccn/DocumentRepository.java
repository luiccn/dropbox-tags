package com.luiccn;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.Collection;

@SuppressWarnings("unchecked")
public interface DocumentRepository extends SolrCrudRepository<Document, String> {

    @Override
    long count();



    Document findById(String id);

    Page<Document> findByTags(String tag, Pageable pageable);

    Document save(Document d);

    Page<Document> findByFilename(String filename, Pageable pageable);

    Document findByFilenameAndPath(String filename, String path);

    Page<Document> findByTagsIn(Collection<String> tags, Pageable pageable);

    @Query(value = "tags:?0",defaultOperator = org.springframework.data.solr.core.query.Query.Operator.AND)
    Page<Document> findByTagsInExclusive(Collection<String> tags, Pageable pageable);




}
