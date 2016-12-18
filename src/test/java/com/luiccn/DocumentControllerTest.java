package com.luiccn;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

        matches.add(new SearchMatch(SearchMatchType.BOTH, new Metadata("cc", "/aa/bb", "/aa/bb", "123")));

        when(dropbox.files()).thenReturn(files);
        when(files.search(anyString(), anyString())).thenReturn(searchResult);
        when(searchResult.getMatches()).thenReturn(matches);

        DocumentRepository documentRepository = mock(DocumentRepository.class);
        when(documentRepository.findByFilenameAndPath("\"cc\"", "\"/aa/bb\""))
                .thenReturn(new Document("id", "/aa/bb", "cc", new ArrayList<>()));
        Document mockDocument = new Document("id", "/aa/bb", "cc", Collections.singleton("luiz"));
        when(documentRepository.save(any(Document.class))).thenReturn(mockDocument);

        documentController = new DocumentController(documentRepository, dropbox);


        Document document = documentController.addTag("luiz", "cc", "/aa/bb");
        System.out.println(document);

    }


}
