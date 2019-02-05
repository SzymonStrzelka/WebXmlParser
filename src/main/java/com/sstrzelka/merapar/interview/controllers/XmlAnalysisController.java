package com.sstrzelka.merapar.interview.controllers;

import com.sstrzelka.merapar.interview.exceptions.InvalidUrlException;
import com.sstrzelka.merapar.interview.exceptions.InvalidXmlException;
import com.sstrzelka.merapar.interview.model.requests.XmlAnalysisRequest;
import com.sstrzelka.merapar.interview.model.responses.XmlAnalysisResponse;
import com.sstrzelka.merapar.interview.services.XmlAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class XmlAnalysisController {

    private final XmlAnalysisService xmlAnalysisServiceService;

    @PostMapping(
            value = "/analyze",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public XmlAnalysisResponse analyzeStackOverflowXml(@RequestBody XmlAnalysisRequest request) throws InvalidUrlException, InvalidXmlException {
        return xmlAnalysisServiceService.parse(request);
    }
}
