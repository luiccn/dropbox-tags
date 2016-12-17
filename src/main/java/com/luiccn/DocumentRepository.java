package com.luiccn;

import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.List;

@SuppressWarnings("unchecked")
public interface DocumentRepository extends SolrCrudRepository<Document, String> {

    @Override
    long count();

    Document findById(String id);

    List<Document> findByTags(String tag);

    Document save(Document d);

    List<Document> findByFilename(String filename);

    Document findByFilenameAndPath(String filename, String path);


}
