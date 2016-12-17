package com.luiccn;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("unchecked")
public interface DocumentRepository extends SolrCrudRepository<Document, String> {

    Document save(Document d);

    Page<Document> findByFilename(String filename, Pageable pageable);

    Document findByFilenameAndPath(String filename, String path);

    Page<Document> findByTagsIn(Collection<String> tags, Pageable pageable);

    @Query(value = "tags:?0",defaultOperator = org.springframework.data.solr.core.query.Query.Operator.AND)
    Page<Document> findByTagsInExclusive(Collection<String> tags, Pageable pageable);

    List<Document> findByTagsIn(Collection<String> tags);

    @Query(value = "tags:?0",defaultOperator = org.springframework.data.solr.core.query.Query.Operator.AND)
    List<Document> findByTagsInExclusive(Collection<String> tags);




}
