package com.example.fileuploader.cucumber.glue;

import com.example.fileuploader.fileupload.models.CampaignNumberModel;
import com.example.fileuploader.ticketing.models.SystemTicketModel;
import com.example.fileuploader.ticketing.models.TicketStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FileStoringSteps {
    @Value("${jsonTestDirPath}")
    private String jsonDir;

    private int process_ticketId;

    @When("^a file finishes uploading with ticket id ([0-9]+)$")
    public void whenFileFinishesUploadingWithTicket(int ticketId){
        process_ticketId = ticketId;
    }

    @Then("^storing process starts and ticket status is marked (.*)$")
    public void thenStoringStartsAndTicketStatusIsMarked(TicketStatus status) throws IOException {
        SystemTicketModel ticket = getTicketFromFile("ticket_storing.json");
        Assertions.assertThat(ticket.getTicketId()).isEqualTo(process_ticketId);
        Assertions.assertThat(ticket.getStatus()).isEqualTo(status);
    }

    @When("^a file storing process with ticket id ([0-9]+) is finished$")
    public void whenFileStoringFinishedWithTicketId(int ticketId){
        process_ticketId = ticketId;
    }

    @Then("^ticket is marked (.*)$")
    public void ticketIsMarked(TicketStatus ticketStatus) throws IOException {
        SystemTicketModel ticket = getTicketFromFile("ticket_stored.json");
        Assertions.assertThat(ticket.getTicketId()).isEqualTo(process_ticketId);
        Assertions.assertThat(ticket.getStatus()).isEqualTo(ticketStatus);
    }

    @And("^phone numbers are stored in database$")
    public void andPhoneNumbersAreStoredInDatabase() throws IOException {
        List<CampaignNumberModel> phoneNumbers = getNumbersFromDatabase();
        Assertions.assertThat(phoneNumbers.size()).isNotEqualTo(0);
        for (CampaignNumberModel phoneNumber :
                phoneNumbers) {
            Assertions.assertThat(phoneNumber.getTicketId()).isEqualTo(process_ticketId);
            Assertions.assertThat(phoneNumber.getPhoneNumber()).isNotEmpty();
        }
    }

    private SystemTicketModel getTicketFromFile(String filename) throws IOException {
        return new ObjectMapper().readValue(new File(jsonDir+filename), SystemTicketModel.class);
    }

    private List<CampaignNumberModel> getNumbersFromDatabase() throws IOException {
        return Arrays.stream(new ObjectMapper().readValue(new File(jsonDir+"phone_numbers.json"), CampaignNumberModel[].class)).toList();
    }
}
