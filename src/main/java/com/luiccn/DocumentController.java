package com.luiccn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tags")
public class DocumentController {

    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentController(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    Long getCount() {
        return documentRepository.count();
    }


    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET, produces = "application/json")
    Document getOne(@PathVariable String id) {
        Document byId = documentRepository.findById(id);
        return byId;
    }

    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET, produces = "application/json")
    Document getByTagName(@PathVariable String name) {
        Document byTags = documentRepository.findByTags(name);
        return byTags;
    }


}
