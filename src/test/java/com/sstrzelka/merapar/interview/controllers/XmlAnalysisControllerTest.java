package com.sstrzelka.merapar.interview.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sstrzelka.merapar.interview.model.requests.XmlAnalysisRequest;
import com.sstrzelka.merapar.interview.model.responses.StackOverflowAnalysisDetails;
import com.sstrzelka.merapar.interview.services.StackOverflowParser;
import com.sstrzelka.merapar.interview.services.StackOverflowXmlAnalysisService;
import com.sstrzelka.merapar.interview.services.XmlAnalysisService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockserver.integration.ClientAndServer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


public class XmlAnalysisControllerTest {

    private static final String HTTP_LOCALHOST = "http://localhost";
    private static final int PORT = 1080;
    private static final String VALID_XML_ENDPOINT = "/valid-xml";
    private static final String EMPTY_XML_ENDPOINT = "/empty-xml";
    private static final String INVALID_XML_ENDPOINT = "/invalid-xml";
    private static final String INVALID_URL_ENDPOINT = "invalid@xml";
    private static final String UNREACHABLE_ENDPOINT = "/unreachable";
    private static final String VALID_XML = "<posts><row Id=\"1\" PostTypeId=\"1\" AcceptedAnswerId=\"5\" CreationDate=\"2015-07-14T18:39:27.757\" Score=\"4\" ViewCount=\"123\" Body=\"&lt;p&gt;The proposal for this site only mentions Arabic. Which Arabic?&lt;/p&gt; &lt;p&gt;There is a common type of Arabic called 'classical Arabic' (fusha) which stretches back at least 1000 years back. It's still used today for most media (eg. newspapers, textbooks) and in universities.&lt;/p&gt; &lt;p&gt;Beyond this, each country has its own dialect (or slang) version of Arabic. This includes both use of vocabulary, and also actual grammar (or ignoring the classical grammar).&lt;/p&gt; &lt;p&gt;While some of the sample questions on Area 51 talk about Arabic in a specific area, the question is really how we should plan and organize questions, or whether we should focus exclusively on classical Arabic.&lt;/p&gt; \" OwnerUserId=\"20\" LastEditorUserId=\"20\" LastEditDate=\"2015-07-14T22:27:41.087\" LastActivityDate=\"2015-07-15T16:43:23.787\" Title=\"Which Arabic are we talking about?\" Tags=\"&lt;discussion&gt;\" AnswerCount=\"5\" CommentCount=\"4\"/></posts>";
    private static final String INVALID_XML = "<tag1><tag2></tag1>";
    private static final String NO_ROWS_XML = "<tag></tag>";

    private static StackOverflowAnalysisDetails VALID_RESPONSE;
    private static StackOverflowAnalysisDetails VALID_RESPONSE_EMPTY_XML;
    private static ClientAndServer mockServer = startClientAndServer(PORT);

    private XmlAnalysisService analysisService = new StackOverflowXmlAnalysisService(new StackOverflowParser());
    private MockMvc mockMvc = standaloneSetup(new XmlAnalysisController(analysisService)).build();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() throws Exception {
        VALID_RESPONSE = StackOverflowAnalysisDetails.builder()
                .firstPost(LocalDateTime.parse("2015-07-14T18:39:27.757"))
                .lastPost(LocalDateTime.parse("2015-07-14T18:39:27.757"))
                .totalPosts(1)
                .avgScore(4.0)
                .totalAcceptedPosts(1)
                .build();
        VALID_RESPONSE_EMPTY_XML = StackOverflowAnalysisDetails.builder()
                .totalPosts(0)
                .avgScore(0)
                .totalAcceptedPosts(0)
                .build();
    }

    @Test
    public void analyzeCorrectRequestShouldReturnValidResponse() throws Exception {
        createEndpoint(VALID_XML_ENDPOINT, HttpStatus.OK, VALID_XML);
        performPositiveScenario(VALID_XML_ENDPOINT, VALID_RESPONSE);
    }

    @Test
    public void analyzeEmptyXmlShouldReturnValidResponse() throws Exception {
        createEndpoint(EMPTY_XML_ENDPOINT, HttpStatus.OK, NO_ROWS_XML);
        performPositiveScenario(EMPTY_XML_ENDPOINT, VALID_RESPONSE_EMPTY_XML);
    }

    @Test
    public void analyzeInvalidXmlShouldReturn400ErrorCode() throws Exception {
        createEndpoint(INVALID_XML_ENDPOINT, HttpStatus.OK, INVALID_XML);
        performNegativeScenario(INVALID_XML_ENDPOINT, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void analyzeInvalidUrlShouldReturn422ErrorCode() throws Exception {
        createEndpoint(INVALID_URL_ENDPOINT, HttpStatus.OK, VALID_XML);
        performNegativeScenario(INVALID_URL_ENDPOINT, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void analyzeUrlNotFoundShouldReturn422ErrorCode() throws Exception {
        performNegativeScenario(VALID_XML_ENDPOINT + EMPTY_XML_ENDPOINT, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void analyzeMockServerReturning500ShouldReturn422ErrorCode() throws Exception {
        createEndpoint(UNREACHABLE_ENDPOINT, HttpStatus.INTERNAL_SERVER_ERROR, VALID_XML);
        performNegativeScenario(UNREACHABLE_ENDPOINT, HttpStatus.UNPROCESSABLE_ENTITY);
    }


    private void createEndpoint(String url, HttpStatus statusCode, String xml) {
        mockServer.when(
                request()
                        .withMethod("GET")
                        .withPath(url)
        )
                .respond(
                        response()
                                .withStatusCode(statusCode.value())
                                .withBody(xml)
                );
    }
    private void performPositiveScenario(String endpoint, StackOverflowAnalysisDetails response) throws Exception {
        XmlAnalysisRequest request = prepareRequest(endpoint);
        mockMvc.perform(post("/analyze")
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.parseMediaType("application/json"))
                .contentType("application/json")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string(Matchers.containsString(objectMapper.writeValueAsString(response)))
                );
    }
    private void performNegativeScenario(String endpoint, HttpStatus statusCode) throws Exception {
        XmlAnalysisRequest request = prepareRequest(endpoint);
        mockMvc.perform(post("/analyze")
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.parseMediaType("application/json"))
                .contentType("application/json"))
                .andExpect(status().is(statusCode.value()));
    }
    private XmlAnalysisRequest prepareRequest(String endpoint) {
        return new XmlAnalysisRequest(
                HTTP_LOCALHOST + ":" + PORT + endpoint
        );
    }
}
