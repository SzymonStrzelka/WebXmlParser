package com.sstrzelka.merapar.interview.services;

import com.sstrzelka.merapar.interview.exceptions.InvalidXmlException;
import com.sstrzelka.merapar.interview.model.StackOverflowRow;
import com.sstrzelka.merapar.interview.model.responses.XmlAnalysisResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

public class StackOverflowXmlAnalysisServiceTest {

    private final int SCORE = 70;
    private final LocalDateTime CREATION_DATE = LocalDateTime.parse("2017-03-21T21:34");
    private byte[] VALID_XML = "<posts><row Score=\"80\" CreationDate=\"2017-03-01\" /></posts>".getBytes();
    private byte[] INVALID_XML = "<tag1></tag2>".getBytes();

    @Mock
    private StackOverflowParser stackOverflowParser;

    private StackOverflowXmlAnalysisService service;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        service = new StackOverflowXmlAnalysisService(stackOverflowParser);
        StackOverflowRow row = StackOverflowRow.builder()
                .score(SCORE)
                .creationDate(CREATION_DATE)
                .build();
        Mockito.when(stackOverflowParser.parseRow(any())).thenReturn(row);
    }

    @Test
    public void testParseValidStreamShouldReturnValidResponse() throws Exception {
        XmlAnalysisResponse response = service.parse(new ByteArrayInputStream(VALID_XML));

        assertEquals(SCORE, response.getDetails().getAvgScore(), 0.1);
        assertEquals(0, response.getDetails().getTotalAcceptedPosts());
        assertEquals(1, response.getDetails().getTotalPosts());
        assertEquals(CREATION_DATE, response.getDetails().getFirstPost());
        assertEquals(CREATION_DATE, response.getDetails().getLastPost());
    }

    @Test
    public void testParseValidStreamShouldReturnValidResponseAvgScore0() throws Exception {
        StackOverflowRow row = StackOverflowRow.builder()
                .score(0)
                .creationDate(CREATION_DATE)
                .build();
        Mockito.when(stackOverflowParser.parseRow(any())).thenReturn(row);

        XmlAnalysisResponse response = service.parse(new ByteArrayInputStream(VALID_XML));

        assertEquals(0, response.getDetails().getAvgScore(), 0.1);
        assertEquals(0, response.getDetails().getTotalAcceptedPosts());
        assertEquals(1, response.getDetails().getTotalPosts());
        assertEquals(CREATION_DATE, response.getDetails().getFirstPost());
        assertEquals(CREATION_DATE, response.getDetails().getLastPost());
    }

    @Test(expected = InvalidXmlException.class)
    public void testParseInvalidXmlShouldThrowException() throws Exception {
        service.parse(new ByteArrayInputStream(INVALID_XML));
    }
}