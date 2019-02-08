package com.sstrzelka.merapar.interview.model.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class XmlAnalysisResponse {
    private final LocalDateTime analyseDate = LocalDateTime.now();
}
