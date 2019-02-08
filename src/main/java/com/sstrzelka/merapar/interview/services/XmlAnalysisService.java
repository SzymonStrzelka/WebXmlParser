package com.sstrzelka.merapar.interview.services;

import com.sstrzelka.merapar.interview.exceptions.InvalidXmlException;
import com.sstrzelka.merapar.interview.model.responses.XmlAnalysisResponse;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public interface XmlAnalysisService {
    XmlAnalysisResponse parse(InputStream inputStream) throws InvalidXmlException;
}
