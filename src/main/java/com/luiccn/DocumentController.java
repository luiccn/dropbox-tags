package com.luiccn;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.Metadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.QueryParser;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("SpringJavaAutowiringInspection")
@RestController
@RequestMapping("/tags")
public class DocumentController {

    private final DocumentRepository documentRepository;
    private final DbxClientV2 dbxClient;

    @Autowired
    public DocumentController(DocumentRepository documentRepository, DbxClientV2 dropboxClient) {
        this.documentRepository = documentRepository;
        dbxClient = dropboxClient;
    }

    @RequestMapping(method = RequestMethod.GET)
    Long getCount() {
        return documentRepository.count();
    }

    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET, produces = "application/json")

    public List<Document> getByTagName(@PathVariable String name) {
        name = quote(name);
        return documentRepository.findByTags(name);
    }

    @RequestMapping(value = "/files")
    public Metadata getFileByName(@RequestParam(value = "name") String name, @RequestParam(value = "path", defaultValue = "") String path) {

        try {
            List<Metadata> entries = dbxClient.files().listFolder(path).getEntries();
            Optional<Metadata> first = entries.stream().filter(m -> m.getName().equalsIgnoreCase(name)).findFirst();

            if (first.isPresent()) {
                return first.get();
            }

        } catch (DbxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "files/{name}", method = RequestMethod.GET)
    public List<Document> findByFileName(@PathVariable String name) {
        return documentRepository.findByFilename(quote(name));
    }

    @RequestMapping(value = "add/{tag}", method = RequestMethod.GET)
    public Document addTag(@PathVariable String tag, @RequestParam(value = "name") String name, @RequestParam(value = "path", defaultValue = "") String path) {

        tag = quote(tag);
        name = quote(name);
        path = quote(path);

        Document fromSolr = documentRepository.findByFilenameAndPath(name, path);

        if (fromSolr == null) {
            Document document = new Document(null, path, name, Collections.singletonList(tag));
            return documentRepository.save(document);
        } else {
            fromSolr.addTag(tag);
            return documentRepository.save(fromSolr);
        }
    }

    private String quote(String s) {
        return "\"" + s + "\"";
    }
}
