package com.sstrzelka.merapar.interview.controllers;

import com.sstrzelka.merapar.interview.exceptions.HostUnreachableException;
import com.sstrzelka.merapar.interview.exceptions.InvalidUrlException;
import com.sstrzelka.merapar.interview.exceptions.InvalidXmlException;
import com.sstrzelka.merapar.interview.model.requests.XmlAnalysisRequest;
import com.sstrzelka.merapar.interview.model.responses.XmlAnalysisResponse;
import com.sstrzelka.merapar.interview.services.XmlAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@RestController
@RequiredArgsConstructor
@Slf4j
public class XmlAnalysisController {

    private final XmlAnalysisService xmlAnalysisServiceService;

    @PostMapping(
            value = "/analyze",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public XmlAnalysisResponse analyzeStackOverflowXml(@RequestBody @Valid XmlAnalysisRequest request)
            throws InvalidUrlException, InvalidXmlException, HostUnreachableException {

        try {
            final URL url = new URL(request.getUrl());
            return xmlAnalysisServiceService.parse(url.openStream());
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
            throw new InvalidUrlException();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new HostUnreachableException();
        }
    }
}
