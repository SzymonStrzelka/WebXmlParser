package com.sstrzelka.merapar.interview.services;

import com.sstrzelka.merapar.interview.exceptions.HostUnreachableException;
import com.sstrzelka.merapar.interview.exceptions.InvalidUrlException;
import com.sstrzelka.merapar.interview.exceptions.InvalidXmlException;
import com.sstrzelka.merapar.interview.model.StackOverflowProcessingData;
import com.sstrzelka.merapar.interview.model.StackOverflowRow;
import com.sstrzelka.merapar.interview.model.requests.XmlAnalysisRequest;
import com.sstrzelka.merapar.interview.model.responses.StackOverflowSummary;
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
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

@Service
@RequiredArgsConstructor
@Slf4j
//TODO check multithreading behaviour
public class StackOverflowXmlAnalysisService implements XmlAnalysisService {

    private static final String ROW_FIELD = "row";

    private final XMLInputFactory factory = XMLInputFactory.newInstance();
    private final StackOverflowParser stackOverflowParser;

    @Override
    public XmlAnalysisResponse parse(XmlAnalysisRequest request) throws InvalidUrlException, InvalidXmlException, HostUnreachableException {
        StackOverflowProcessingData finalState;
        try {
            final URL url = new URL(request.getUrl());
            try (var bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                BomSkipper.skip(bufferedReader);
                final XMLEventReader reader = factory.createXMLEventReader(bufferedReader);
                finalState = parseStream(reader);
            }
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
            throw new InvalidUrlException();
        } catch (XMLStreamException e) {
            log.error(e.getMessage());
            throw new InvalidXmlException();
        } catch (IOException e) {
            throw new HostUnreachableException();
        }
        var summary = StackOverflowSummary.builder()
                .firstPost(finalState.getYoungestRow())
                .lastPost(finalState.getOldestRow())
                .avgScore(calculateAvgScore(finalState))
                .totalAcceptedPosts(finalState.getAcceptedAnswerCount())
                .totalPosts(finalState.getCount())
                .build();
        return new XmlAnalysisResponse(summary);
    }

    private double calculateAvgScore(StackOverflowProcessingData finalState) {
        if (finalState.getCount() != 0)
            return (double) finalState.getOverallScore() / finalState.getCount();
        return 0;
    }

    private StackOverflowProcessingData parseStream(XMLEventReader reader) throws XMLStreamException {
        var currentState = new StackOverflowProcessingData();
        while (reader.hasNext()) {
            final XMLEvent event = reader.nextEvent();
            if (isEventARow(event)) {
                StackOverflowRow row = stackOverflowParser.parseRow(event.asStartElement());
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

