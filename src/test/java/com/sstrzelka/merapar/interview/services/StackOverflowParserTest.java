package com.sstrzelka.merapar.interview.services;

import com.sstrzelka.merapar.interview.model.StackOverflowRow;
import com.sun.xml.internal.stream.events.AttributeImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.xml.namespace.QName;
import javax.xml.stream.events.StartElement;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class StackOverflowParserTest {

    private static final int SCORE = 30;
    private static final int ACCEPTED_ANSWER_ID = 171;
    private static final String CREATE_DATE = "2018-01-01T18:27";
    private static final String SCORE_FIELD = "Score";
    private static final String ACCEPTED_ANSWER_ID_FIELD = "AcceptedAnswerId";
    private static final String CREATION_DATE_FIELD = "CreationDate";
    
    @Mock
    private StartElement VALID_START_ELEMENT;

    private StackOverflowParser parser;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        parser = new StackOverflowParser();
        Mockito.when(VALID_START_ELEMENT.getAttributeByName(new QName(SCORE_FIELD))).
                thenReturn(new AttributeImpl(SCORE_FIELD, String.valueOf(SCORE)));
        Mockito.when(VALID_START_ELEMENT.getAttributeByName(new QName(ACCEPTED_ANSWER_ID_FIELD))).
                thenReturn(new AttributeImpl(ACCEPTED_ANSWER_ID_FIELD, String.valueOf(ACCEPTED_ANSWER_ID)));
        Mockito.when(VALID_START_ELEMENT.getAttributeByName(new QName(CREATION_DATE_FIELD))).
                thenReturn(new AttributeImpl(CREATION_DATE_FIELD,CREATE_DATE));
    }

    @Test
    public void testParseRowValidStartElementShouldReturnValidRow() {
        StackOverflowRow row = parser.parseRow(VALID_START_ELEMENT);
        assertEquals(SCORE, row.getScore());
        assertTrue(row.isAccepted());
        assertEquals(row.getCreationDate(), LocalDateTime.parse(CREATE_DATE));
    }

    @Test
    public void testParseRowValidStartElementWithNoAcceptedAnswerShouldReturnValidRow() {
        Mockito.when(VALID_START_ELEMENT.getAttributeByName(new QName(ACCEPTED_ANSWER_ID_FIELD))).
                thenReturn(null);
        StackOverflowRow row = parser.parseRow(VALID_START_ELEMENT);
        assertEquals(SCORE, row.getScore());
        assertFalse(row.isAccepted());
        assertEquals(row.getCreationDate(), LocalDateTime.parse(CREATE_DATE));
    }
    @Test
    public void testParseRowValidStartElementWithNoCreateDateShouldReturnValidRow() {
        Mockito.when(VALID_START_ELEMENT.getAttributeByName(new QName(CREATION_DATE_FIELD))).
                thenReturn(null);
        StackOverflowRow row = parser.parseRow(VALID_START_ELEMENT);
        assertEquals(SCORE, row.getScore());
        assertTrue(row.isAccepted());
        assertNull(row.getCreationDate());
    }
}