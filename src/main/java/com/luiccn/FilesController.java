package com.luiccn;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.Metadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.luiccn.Utils.quote;

@RestController
@RequestMapping("/files")
public class FilesController {

    private final DocumentRepository documentRepository;
    private final DbxClientV2 dbxClient;

    @Autowired
    public FilesController(DocumentRepository documentRepository, DbxClientV2 dropboxClient) {
        this.documentRepository = documentRepository;
        dbxClient = dropboxClient;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Metadata getFileByName(@RequestParam(value = "name") String name, @RequestParam(value = "path", defaultValue = "") String path) {

        try {
            List<Metadata> entries = dbxClient.files().listFolder(path).getEntries();
            Optional<Metadata> first = entries.stream().filter(m -> m.getName().equals(name)).findFirst();

            if (first.isPresent()) {
                return first.get();
            }

        } catch (DbxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public Page<Document> findByFileName(@PathVariable String name, Pageable pageable) {
        return documentRepository.findByFilename(quote(name), pageable);
    }






}
