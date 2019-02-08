package com.sstrzelka.merapar.interview.services;

import com.sstrzelka.merapar.interview.exceptions.InvalidXmlException;
import com.sstrzelka.merapar.interview.model.StackOverflowProcessingData;
import com.sstrzelka.merapar.interview.model.StackOverflowRow;
import com.sstrzelka.merapar.interview.model.responses.StackOverflowAnalysisDetails;
import com.sstrzelka.merapar.interview.model.responses.StackOverflowXmlAnalysisResponse;
import com.sstrzelka.merapar.interview.model.responses.XmlAnalysisResponse;
import com.sstrzelka.merapar.interview.utils.BomSkipper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
@RequiredArgsConstructor
@Slf4j
public class StackOverflowXmlAnalysisService implements XmlAnalysisService {

    private static final String ROW_FIELD = "row";

    private final XMLInputFactory factory = XMLInputFactory.newInstance();
    private final StackOverflowParser stackOverflowParser;

    @Override
    public XmlAnalysisResponse parse(InputStream inputStream) throws InvalidXmlException {
        StackOverflowProcessingData finalState;
        try {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                BomSkipper.skip(bufferedReader);
                final XMLEventReader reader = factory.createXMLEventReader(bufferedReader);
                finalState = parseStream(reader);
            }
        } catch (XMLStreamException | IOException e) {
            log.error(e.getMessage());
            throw new InvalidXmlException();
        }
        StackOverflowAnalysisDetails summary = StackOverflowAnalysisDetails.builder()
                .firstPost(finalState.getYoungestRow())
                .lastPost(finalState.getOldestRow())
                .avgScore(calculateAvgScore(finalState))
                .totalAcceptedPosts(finalState.getAcceptedAnswerCount())
                .totalPosts(finalState.getCount())
                .build();
        return new StackOverflowXmlAnalysisResponse(summary);
    }

    private double calculateAvgScore(StackOverflowProcessingData finalState) {
        if (finalState.getCount() != 0)
            return (double) finalState.getOverallScore() / finalState.getCount();
        return 0;
    }

    private StackOverflowProcessingData parseStream(XMLEventReader reader) throws XMLStreamException {
        final StackOverflowProcessingData currentState = new StackOverflowProcessingData();
        while (reader.hasNext()) {
            final XMLEvent event = reader.nextEvent();
            if (isEventARow(event)) {
                final StackOverflowRow row = stackOverflowParser.parseRow(event.asStartElement());
                currentState.updateData(row);
            }
        }
        return currentState;
    }

    private boolean isEventARow(XMLEvent event) {
        return event.isStartElement() && event.asStartElement().getName()
                .getLocalPart().equals(ROW_FIELD);
    }
}

