package com.luiccn;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalAnswers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class DocumentControllerTest {

    DocumentController documentController;
    DbxClientV2 dropbox;
    DbxUserFilesRequests files;
    SearchResult searchResult;
    List<SearchMatch> matches;

    @Before
    public void setUp() throws Exception {
        dropbox = mock(DbxClientV2.class);
        files = mock(DbxUserFilesRequests.class);
        searchResult = mock(SearchResult.class);
        matches = new ArrayList<>();

    }

    @Test
    public void addTag_happy() throws Exception {

        //GIVEN document present at dropbox
        matches.add(new SearchMatch(SearchMatchType.BOTH, new Metadata("cc", "/aa/bb", "/aa/bb", "123")));
        when(dropbox.files()).thenReturn(files);
        when(files.search(anyString(), anyString())).thenReturn(searchResult);
        when(searchResult.getMatches()).thenReturn(matches);

        DocumentRepository documentRepository = mock(DocumentRepository.class);
        when(documentRepository.findByFilenameAndPath("\"cc\"", "\"/aa/bb\""))
                .thenReturn(new Document("id", "/aa/bb", "cc", new ArrayList<>()));

        when(documentRepository.save(any(Document.class))).then(AdditionalAnswers.returnsFirstArg());
        documentController = new DocumentController(documentRepository, dropbox);

        //WHEN adding a tag
        Document document = documentController.addTag("luiz", "cc", "/aa/bb");

        //THEN should return document with the added tag
        assertEquals(1, document.getTags().size());
        assertEquals("luiz", document.getTags().stream().findAny().orElseThrow(RuntimeException::new));
        assertEquals("cc", document.getFilename());
        assertEquals("/aa/bb", document.getPath());

    }

    @Test
    public void addTag_sad() throws Exception {

        //GIVEN no document in dropbox
        when(dropbox.files()).thenReturn(files);
        when(files.search(anyString(), anyString())).thenReturn(searchResult);
        when(searchResult.getMatches()).thenReturn(matches);

        DocumentRepository documentRepository = mock(DocumentRepository.class);
        when(documentRepository.findByFilenameAndPath("\"cc\"", "\"/aa/bb\""))
                .thenReturn(new Document("id", "/aa/bb", "cc", new ArrayList<>()));

        when(documentRepository.save(any(Document.class))).then(AdditionalAnswers.returnsFirstArg());
        documentController = new DocumentController(documentRepository, dropbox);

        //WHEN trying to add a tag to a non existent document
        Document document = documentController.addTag("luiz", "cc", "/aa/bb");

        //THEN should return null
        assertNull(document);

    }

    @Test
    public void removeTag_happy() throws Exception {

        //GIVEN document at SOLR with Tags, no dropbox check as it is checked on insert time
        List<String> tags = new ArrayList<>();
        tags.add("luiz");
        tags.add("carlos");

        DocumentRepository documentRepository = mock(DocumentRepository.class);
        when(documentRepository.findByFilenameAndPath("\"cc\"", "\"/aa/bb\""))
                .thenReturn(new Document("id", "/aa/bb", "cc", tags));

        when(documentRepository.save(any(Document.class))).then(AdditionalAnswers.returnsFirstArg());
        documentController = new DocumentController(documentRepository, dropbox);

        //WHEN deleting a tag
        Document document = documentController.deleteTag("luiz", "cc", "/aa/bb");

        //THEN should return document without the tag
        assertEquals(1, document.getTags().size());
        assertEquals("carlos", document.getTags().stream().findAny().orElseThrow(RuntimeException::new));
        assertEquals("cc", document.getFilename());
        assertEquals("/aa/bb", document.getPath());
    }

    @Test
    public void removeTag_sad() throws Exception {

        //GIVEN document at SOLR with NO Tags, no dropbox check as it is checked on insert time
        matches.add(new SearchMatch(SearchMatchType.BOTH, new Metadata("cc", "/aa/bb", "/aa/bb", "123")));
        when(dropbox.files()).thenReturn(files);
        when(files.search(anyString(), anyString())).thenReturn(searchResult);
        when(searchResult.getMatches()).thenReturn(matches);

        List<String> tags = new ArrayList<>();
        tags.add("luiz");
        tags.add("carlos");

        DocumentRepository documentRepository = mock(DocumentRepository.class);
        when(documentRepository.findByFilenameAndPath("\"cc\"", "\"/aa/bb\""))
                .thenReturn(new Document("id", "/aa/bb", "cc", tags));

        when(documentRepository.save(any(Document.class))).then(AdditionalAnswers.returnsFirstArg());
        documentController = new DocumentController(documentRepository, dropbox);

        //WHEN deleting a tag
        Document document = documentController.deleteTag("mobilabs", "cc", "/aa/bb");

        //THEN should return same document
        assertEquals(2, document.getTags().size());
        assertTrue(document.getTags().containsAll(tags));
        assertEquals("cc", document.getFilename());
        assertEquals("/aa/bb", document.getPath());
    }

    @Test
    public void removeTag_sad2() throws Exception {

        //GIVEN document at SOLR with NO Tags, no dropbox check as it is checked on insert time
        matches.add(new SearchMatch(SearchMatchType.BOTH, new Metadata("cc", "/aa/bb", "/aa/bb", "123")));
        when(dropbox.files()).thenReturn(files);
        when(files.search(anyString(), anyString())).thenReturn(searchResult);
        when(searchResult.getMatches()).thenReturn(matches);

        List<String> tags = new ArrayList<>();

        DocumentRepository documentRepository = mock(DocumentRepository.class);
        when(documentRepository.findByFilenameAndPath("\"cc\"", "\"/aa/bb\""))
                .thenReturn(new Document("id", "/aa/bb", "cc", tags));

        when(documentRepository.save(any(Document.class))).then(AdditionalAnswers.returnsFirstArg());
        documentController = new DocumentController(documentRepository, dropbox);

        //WHEN deleting a tag
        Document document = documentController.deleteTag("mobilabs", "cc", "/aa/bb");

        //THEN should return same document
        assertEquals(0, document.getTags().size());
        assertEquals("cc", document.getFilename());
        assertEquals("/aa/bb", document.getPath());
    }




}
