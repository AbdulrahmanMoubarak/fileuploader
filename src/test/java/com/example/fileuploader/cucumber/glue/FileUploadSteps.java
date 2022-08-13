package com.example.fileuploader.cucumber.glue;

import com.example.fileuploader.ticketing.models.SystemTicketModel;
import com.example.fileuploader.ticketing.models.TicketStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class FileUploadSteps {
    private WireMockServer mockServer;
    private RestTemplate testRestTemplate;
    @Value("${jsonTestDirPath}")
    private String jsonDir;
    private int actualResponseStatusCode;

    @Before
    public void wireMockServerSetup() throws IOException {
        mockServer = new WireMockServer(options().dynamicPort());
        mockServer.start();
        configureFor("localhost", mockServer.port());
        stubFor(post(urlEqualTo("/activateTicket"))
                .withHeader("content-type", equalTo("application/json"))
                .withRequestBody(containing("ticketId"))
                .willReturn(aResponse().withStatus(200))
        );
    }

    @Before
    public void restTemplateSetup() {
        testRestTemplate = new RestTemplateBuilder().build();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        testRestTemplate.setMessageConverters(messageConverters);
    }

    @After
    public void stopServer() {
        mockServer.stop();
    }

    @When("^the user clicks on the upload button with a ticket id ([0-9]+)$")
    public void whenTheUserClicksOnUploadButtonWithTicketId(int ticketId) throws JSONException {
        HttpEntity<String> request = getUpdateTicketRequestObject(ticketId);
        actualResponseStatusCode = testRestTemplate.postForEntity("http://localhost:" + mockServer.port() + "/activateTicket", request, String.class).getStatusCodeValue();
    }

    @Then("^The ticket is activated and it's status is marked (.*) and the uploading process starts$")
    public void thenTicketStatusIsMarked(TicketStatus status) throws IOException {
        SystemTicketModel ticket = getTicketFromFile();
        Assertions.assertThat(ticket.getStatus()).isEqualTo(status);
    }

    @And("^a response with status code ([0-9]+) is sent to indicated that ticket status is updated$")
    public void andResponseWithStatusCodeIsSent(int expectedStatusCode) {
        Assertions.assertThat(expectedStatusCode).isEqualTo(actualResponseStatusCode);
    }

    private HttpEntity<String> getUpdateTicketRequestObject(int id) throws JSONException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject personJsonObject = new JSONObject();
        personJsonObject.put("ticketId", id);
        return new HttpEntity<String>(personJsonObject.toString(), headers);
    }

    private SystemTicketModel getTicketFromFile() throws IOException {
        return new ObjectMapper().readValue(new File(jsonDir+"ticket_uploading.json"), SystemTicketModel.class);
    }
}
