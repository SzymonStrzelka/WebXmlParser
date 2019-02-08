package com.sstrzelka.merapar.interview.model.responses;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class StackOverflowXmlAnalysisResponse extends XmlAnalysisResponse {
    private final StackOverflowAnalysisDetails details;
}
