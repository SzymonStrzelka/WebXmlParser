package com.sstrzelka.merapar.interview.services;

import com.sstrzelka.merapar.interview.model.requests.XmlAnalysisRequest;
import com.sstrzelka.merapar.interview.model.responses.XmlAnalysisResponse;
import org.springframework.stereotype.Service;

@Service
public class StackOverflowXmlParser implements XmlParser {

    @Override
    public XmlAnalysisResponse parseStackOverflowData(XmlAnalysisRequest request) {
        return null;
    }
}
