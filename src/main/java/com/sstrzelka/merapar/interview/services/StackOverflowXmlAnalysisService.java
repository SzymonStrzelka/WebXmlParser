package com.sstrzelka.merapar.interview.services;

import com.sstrzelka.merapar.interview.exceptions.InvalidUrlException;
import com.sstrzelka.merapar.interview.exceptions.InvalidXmlException;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;

@Service
@RequiredArgsConstructor
@Slf4j
//TODO check multithreading behaviour
public class StackOverflowXmlAnalysisService implements XmlAnalysisService {

    private static final String ROW_FIELD = "row";

    private final XMLInputFactory factory = XMLInputFactory.newInstance();
    private final StackOverflowParser stackOverflowParser;

    @Override
    public XmlAnalysisResponse parse(XmlAnalysisRequest request) throws InvalidUrlException, InvalidXmlException {
//        TODO storing all elements seems like an overkill, just calculate necessary data
        List<StackOverflowRow> rows = new ArrayList<>();
        try {
            final URL url = new URL(request.getUrl());
            try (var bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                BomSkipper.skip(bufferedReader);
                final XMLEventReader reader = factory.createXMLEventReader(bufferedReader);
                parseStream(rows, reader);
            }
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
            throw new InvalidUrlException();
        } catch (XMLStreamException | IOException e) {
            log.error(e.getMessage());
            throw new InvalidXmlException();
        }
        rows.sort(Comparator.comparing(StackOverflowRow::getCreationDate));
        var summary = StackOverflowSummary.builder()
                .firstPost(rows.get(0).getCreationDate())
                .lastPost(rows.get(rows.size() - 1).getCreationDate())
                .avgScore(getAvgScore(rows).orElse(-1))
                .totalAcceptedPosts(getAcceptedAnserwsCount(rows))
                .totalPosts(rows.size())
                .build();
        return new XmlAnalysisResponse(summary);
    }

    private void parseStream(List<StackOverflowRow> rows, XMLEventReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            final XMLEvent event = reader.nextEvent();
            if (isEventARow(event)) {
                rows.add(stackOverflowParser.parseRow(event.asStartElement()));
            }
        }
    }

    private boolean isEventARow(XMLEvent event) {
        return event.isStartElement() && event.asStartElement().getName()
                .getLocalPart().equals(ROW_FIELD);
    }

    private long getAcceptedAnserwsCount(List<StackOverflowRow> rows) {
        return rows.stream().filter(StackOverflowRow::isAccepted).count();
    }

    private OptionalDouble getAvgScore(List<StackOverflowRow> rows) {
        return rows.stream().mapToInt(StackOverflowRow::getScore).average();
    }
}

