package com.luiccn;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

    @Autowired
    private AppConfig appConfig;

    private final DocumentRepository documentRepository;
    private final DbxClientV2 dbxClient;

    @Autowired
    public DocumentController(DocumentRepository documentRepository, DbxClientV2 dropboxClient) {
        this.documentRepository = documentRepository;
        dbxClient = dropboxClient;
    }

    @RequestMapping(value = "/find/{tags}", method = RequestMethod.GET, produces = "application/json")
    public Page<Document> findByTags(@PathVariable List<String> tags, @RequestParam(value = "type", defaultValue = "AND") String type, Pageable pageable) {

        if (type.equals("OR")) {
            return documentRepository.findByTagsIn(tags, pageable);
        } else {
            return documentRepository.findByTagsInExclusive(tags, pageable);
        }
    }

    @RequestMapping(value = "/download/{tags}", method = RequestMethod.GET)
    public void downloadByTags(@PathVariable List<String> tags,
                               @RequestParam(value = "type", defaultValue = "AND") String type,
                               HttpServletResponse response) {

        List<Document> toDownload;
        int downloadSize = 0;

        if (type.equals("OR")) {
            toDownload = documentRepository.findByTagsIn(tags);
        } else {
            toDownload = documentRepository.findByTagsInExclusive(tags);
        }

        response.addHeader("Content-disposition", "attachment;"+tags.stream().collect(Collectors.joining("-"))+".zip");
        response.setContentType("application/zip");

        if (toDownload.size() != 0) {
            try {

                IOUtils.copy(getDownloadAsStream(response, toDownload, downloadSize), response.getOutputStream());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private ByteArrayInputStream getDownloadAsStream(HttpServletResponse response, List<Document> toDownload, int downloadSize) throws DbxException, java.io.IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(out);

        for (Document document : toDownload) {
            if (isDocumentInDropbox(document.getFilename(), document.getPath())) {
                ByteArrayOutputStream downloadStream = new ByteArrayOutputStream();
                dbxClient.files().download(getDownloadPath(document)).download(downloadStream);
                ZipEntry z = new ZipEntry(document.getFilename());
                zos.putNextEntry(z);
                zos.write(downloadStream.toByteArray());
                zos.closeEntry();

                downloadSize+= downloadStream.size();
                if (downloadSize >= appConfig.getMaximumDownloadSize() ) {
                    zos.close();
                    response.setStatus(500);
                }
            }
        }
        zos.close();
        response.setStatus(200);
        return new ByteArrayInputStream(out.toByteArray());
    }

    private String getDownloadPath(Document document) {
        return document.getPath() + "/" + document.getFilename();
    }

    @RequestMapping(value = "/{tag}", method = RequestMethod.PUT)
    public Document addTag(@PathVariable String tag, @RequestParam(value = "name") String name, @RequestParam(value = "path", defaultValue = "") String path) {

        name = quote(name);
        path = quote(path);

        if (isDocumentInDropbox(name, path)) {
            Document fromSolr = documentRepository.findByFilenameAndPath(name, path);

            if (fromSolr == null) {
                Document document = new Document(null, path, name, Collections.singletonList(tag));
                return documentRepository.save(document);
            } else {
                fromSolr.addTag(tag);
                return documentRepository.save(fromSolr);
            }
        } else {
            return null;
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

    boolean isDocumentInDropbox(String name, String path) {

        long count = 0;
        try {
            count = (long) dbxClient.files()
                    .search(unQuote(path), unQuote(name))
                    .getMatches()
                    .size();

        } catch (DbxException e) {
            e.printStackTrace();
        }

        return count != 0;
    }
}
