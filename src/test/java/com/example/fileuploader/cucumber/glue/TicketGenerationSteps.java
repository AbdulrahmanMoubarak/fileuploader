package com.example.fileuploader.cucumber.glue;

import com.example.fileuploader.ticketing.models.SystemTicketModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.http.Body;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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

public class TicketGenerationSteps {

    private WireMockServer mockServer;
    private SystemTicketModel createdTicket;
    private SystemTicketModel expectedTicket;
    private RestTemplate testRestTemplate;

    @Value("${jsonTestDirPath}")
    private String jsonDir;

    @Before
    public void wireMockServerSetup() throws IOException {
        mockServer = new WireMockServer(options().dynamicPort());
        mockServer.start();
        configureFor("localhost", mockServer.port());
        stubFor(post(urlEqualTo("/requestTicket"))
                .withHeader("content-type", equalTo("application/json"))
                .withRequestBody(containing("userId"))
                .withRequestBody(containing("fileName"))
                .withRequestBody(containing("fileSize"))
                .willReturn(aResponse().withStatus(200).withResponseBody(
                        new Body(
                                new ObjectMapper().writeValueAsString(getTicketFromFile())
                        )
                ))
        );
    }

    @Before
    public void restTemplateSetup(){
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


    @Given("^The following ticket$")
    public void givenTheFollowingTicket(SystemTicketModel ticket) {
        expectedTicket = ticket;
    }

    @When("^a user with id ([0-9]+) selects a file at the front end application with a name (.*) and a size of ([0-9]+) bytes$")
    public void whenUserWithIdSelectsAFileWithNameAndSize(int id, String name, float size) throws JSONException {
        HttpEntity<String> request = getGenerateTicketRequestObject(id, name, size);
        createdTicket = testRestTemplate.postForEntity("http://localhost:" + mockServer.port() + "/requestTicket", request, SystemTicketModel.class).getBody();
    }

    @Then("^a ticket id generated for this operation$")
    public void thenTicketIsGeneratedForThisOperation() {
        Assertions.assertThat(createdTicket.getTicketId()).isEqualTo(expectedTicket.getTicketId());
        Assertions.assertThat(createdTicket.getFileName()).isEqualTo(expectedTicket.getFileName());
        Assertions.assertThat(createdTicket.getSize()).isEqualTo(expectedTicket.getSize());
        Assertions.assertThat(createdTicket.getStatus()).isEqualTo(expectedTicket.getStatus());
    }

    private HttpEntity<String> getGenerateTicketRequestObject(int id, String name, float size) throws JSONException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject personJsonObject = new JSONObject();
        personJsonObject.put("userId", id);
        personJsonObject.put("fileName", name);
        personJsonObject.put("fileSize", size);
        return new HttpEntity<String>(personJsonObject.toString(), headers);
    }
    private SystemTicketModel getTicketFromFile() throws IOException {
        return new ObjectMapper().readValue(new File(jsonDir+"ticket.json"), SystemTicketModel.class);
    }
}
