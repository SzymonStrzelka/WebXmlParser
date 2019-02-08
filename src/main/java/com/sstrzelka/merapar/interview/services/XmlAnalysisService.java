package com.sstrzelka.merapar.interview.services;

import com.sstrzelka.merapar.interview.exceptions.HostUnreachableException;
import com.sstrzelka.merapar.interview.exceptions.InvalidUrlException;
import com.sstrzelka.merapar.interview.exceptions.InvalidXmlException;
import com.sstrzelka.merapar.interview.model.requests.XmlAnalysisRequest;
import com.sstrzelka.merapar.interview.model.responses.XmlAnalysisResponse;
import org.springframework.stereotype.Service;

@Service
public interface XmlAnalysisService {
    XmlAnalysisResponse parse(XmlAnalysisRequest request) throws InvalidUrlException, InvalidXmlException, HostUnreachableException;
}
