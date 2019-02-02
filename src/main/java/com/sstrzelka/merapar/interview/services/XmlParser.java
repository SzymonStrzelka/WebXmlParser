package com.sstrzelka.merapar.interview.services;

import com.sstrzelka.merapar.interview.model.requests.XmlAnalysisRequest;
import com.sstrzelka.merapar.interview.model.responses.XmlAnalysisResponse;

public interface XmlParser {
    XmlAnalysisResponse parseStackOverflowData(XmlAnalysisRequest request);
}
