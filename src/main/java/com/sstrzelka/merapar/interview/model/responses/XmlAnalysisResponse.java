package com.sstrzelka.merapar.interview.model.responses;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class XmlAnalysisResponse {
    private final LocalDateTime analyseDate = LocalDateTime.now();
    private final StackOverflowSummary details;
}
