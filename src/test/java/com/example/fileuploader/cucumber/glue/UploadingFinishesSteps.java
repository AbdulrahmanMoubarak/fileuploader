package com.example.fileuploader.cucumber.glue;

import com.example.fileuploader.ticketing.models.SystemTicketModel;
import com.example.fileuploader.ticketing.models.TicketStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.But;
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

public class UploadingFinishesSteps {
    private WireMockServer mockServer;
    private RestTemplate testRestTemplate;

    private int actualResponseStatusCode;
    @Value("${jsonTestDirPath}")
    private String jsonDir;

    @Before
    public void wireMockServerSetup() throws IOException {
        mockServer = new WireMockServer(options().dynamicPort());
        mockServer.start();
        configureFor("localhost", mockServer.port());
        stubFor(post(urlEqualTo("/upload"))
                .withHeader("content-type", equalTo("application/json"))
                .withRequestBody(containing("ticketId"))
                .withRequestBody(containing("file"))
                .withRequestBody(containing("checksum"))
                .willReturn(aResponse().withStatus(200))
        );

        stubFor(post(urlEqualTo("/upload_err"))
                .withHeader("content-type", equalTo("application/json"))
                .withRequestBody(containing("ticketId"))
                .withRequestBody(containing("file"))
                .withRequestBody(containing("checksum"))
                .willReturn(aResponse().withStatus(500))
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

    @When("^an uploading process with ticket id ([0-9]+) is for a file (.*) that has a checksum equals (.*) is finished$")
    public void whenUploadingProcessForFileWithTicketAndChecksumIsFinished(int ticketId, String file, String checksum) throws JSONException {
        HttpEntity<String> request = getUploadFileRequestObject(ticketId, file, checksum);
        actualResponseStatusCode = testRestTemplate.postForEntity("http://localhost:" + mockServer.port() + "/upload", request, String.class).getStatusCodeValue();
    }

    @Then("^the ticket status is marked (.*)$")
    public void thenTicketStatusIsMarked(TicketStatus status) throws IOException {
        SystemTicketModel ticket = getTicketFromFile();
        Assertions.assertThat(ticket.getStatus()).isEqualTo(status);
    }

    @And("^a response with status code ([0-9]+) is sent to indicated that uploading is finished$")
    public void andResponseWithStatusCodeIsSent(int expectedStatusCode) {
        Assertions.assertThat(expectedStatusCode).isEqualTo(actualResponseStatusCode);
    }

    @When("^another uploading process with ticket id ([0-9]+) is for a file (.*) that has a checksum equals (.*) is finished$")
    public void whenAnotherUploadingProcessForFileWithTicketAndChecksumIsFinished(int ticketId, String file, String checksum) throws JSONException {
        HttpEntity<String> request = getUploadFileRequestObject(ticketId, file, checksum);
        actualResponseStatusCode = testRestTemplate.postForEntity("http://localhost:" + mockServer.port() + "/upload_err", request, String.class).getStatusCodeValue();
    }

    @But("^the calculated checksum on the server is (.*)$")
    public void butTheCalculatedChecksumIs(String checksum){

    }

    @Then("^a response with status code ([0-9]+) is sent to indicated the uploading error$")
    public void thenResponseWithStatusCodeIsSent(int statusCode){
        Assertions.assertThat(statusCode).isEqualTo(actualResponseStatusCode);
    }

    private HttpEntity<String> getUploadFileRequestObject(int ticketId, String file, String checksum) throws JSONException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject personJsonObject = new JSONObject();
        personJsonObject.put("ticketId", ticketId);
        personJsonObject.put("file", file);
        personJsonObject.put("checksum", checksum);
        return new HttpEntity<String>(personJsonObject.toString(), headers);
    }

    private SystemTicketModel getTicketFromFile() throws IOException {
        return new ObjectMapper().readValue(new File(jsonDir+"ticket_uploaded.json"), SystemTicketModel.class);
    }
}
