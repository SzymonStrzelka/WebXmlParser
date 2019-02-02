package com.sstrzelka.merapar.interview.controlers;

import com.sstrzelka.merapar.interview.model.requests.XmlAnalysisRequest;
import com.sstrzelka.merapar.interview.model.responses.XmlAnalysisResponse;
import com.sstrzelka.merapar.interview.services.XmlParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class XmlAnalysisController {

    private final XmlParser xmlParserService;

    @PostMapping(
            value = "/analyze",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public XmlAnalysisResponse analyzeStackOverflowXml(XmlAnalysisRequest request) {
        return xmlParserService.parseStackOverflowData(request);
    }
}
