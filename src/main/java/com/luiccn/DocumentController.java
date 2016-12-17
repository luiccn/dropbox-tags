package com.luiccn;

import com.dropbox.core.v2.DbxClientV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.luiccn.Utils.quote;
import static com.luiccn.Utils.unQuote;

@SuppressWarnings("SpringJavaAutowiringInspection")
@RestController
@RequestMapping("/tags")
public class DocumentController {

    private static final int MAX_DOWNLOAD_SIZE = 1024 * 1024 * 500; //500Mbytes
    private final DocumentRepository documentRepository;
    private final DbxClientV2 dbxClient;

    @Autowired
    public DocumentController(DocumentRepository documentRepository, DbxClientV2 dropboxClient) {
        this.documentRepository = documentRepository;
        dbxClient = dropboxClient;
    }

    @RequestMapping(value = "find/{tags}", method = RequestMethod.GET, produces = "application/json")
    public Page<Document> getByTags(@PathVariable List<String> tags, @RequestParam(value = "type", defaultValue = "AND") String type, Pageable pageable) {

        if (type.equals("OR")) {
            return documentRepository.findByTagsIn(tags, pageable);
        } else {
            return documentRepository.findByTagsInExclusive(tags, pageable);
        }
    }

    @RequestMapping(value = "download/{tags}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> downloadByTags(@PathVariable List<String> tags, @RequestParam(value = "type", defaultValue = "AND") String type) {

        List<Document> toDownload;
        int downloadSize = 0;

        if (type.equals("OR")) {
            toDownload = documentRepository.findByTagsIn(tags);
        } else {
            toDownload = documentRepository.findByTagsInExclusive(tags);
        }

        File file = new File(tags.stream().collect(Collectors.joining("-"))+".zip");
        try {

            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ZipOutputStream zos = new ZipOutputStream(bos);


            for (Document document : toDownload) {
                ByteArrayOutputStream downloadStream = new ByteArrayOutputStream();
                dbxClient.files().download(getDownloadPath(document)).download(downloadStream);
                ZipEntry z = new ZipEntry(document.getFilename());
                zos.putNextEntry(z);
                zos.write(downloadStream.toByteArray());
                zos.closeEntry();

                downloadSize+= downloadStream.size();
                if (downloadSize >= MAX_DOWNLOAD_SIZE) {

                    zos.close();
                    file.delete();
                }
            }
            zos.close();
            return ResponseEntity.ok("Downloaded "+ downloadSize + " B");
        } catch (Exception e) {
            e.printStackTrace();
            file.delete();
            return ResponseEntity.status(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED).body("Files are too large: "+ downloadSize + " B");
        }
    }

    private String getDownloadPath(Document document) {
        return document.getPath() + "/" + document.getFilename();
    }

    @RequestMapping(value = "/{tag}", method = RequestMethod.PUT)
    public Document addTag(@PathVariable String tag, @RequestParam(value = "name") String name, @RequestParam(value = "path", defaultValue = "") String path, Pageable pageable) {

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

    @RequestMapping(value = "/{tag}", method = RequestMethod.DELETE)
    public Document deleteTag(@PathVariable String tag, @RequestParam(value = "name") String name, @RequestParam(value = "path", defaultValue = "") String path) {

        name = quote(name);
        path = quote(path);

        Document fromSolr = documentRepository.findByFilenameAndPath(name, path);

        if (fromSolr != null) {
            fromSolr.removeTag(tag);
            return documentRepository.save(fromSolr);
        }

        return null;
    }
}
