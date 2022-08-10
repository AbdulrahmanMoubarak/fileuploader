package com.example.fileuploader.fileupload.models;

import javax.persistence.*;

@Entity
@Table(name = "USER_CAMPAIGN_NUMBER")
public class CampaignNumberModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private int userId;
    private String campNumber;
    private int ticketId;

    public CampaignNumberModel() {
        this.id = -1;
        this.userId = -1;
        this.campNumber = null;
        this.ticketId = -1;
    }

    public CampaignNumberModel(int userId, String campNumber, int ticketId) {
        this.id = 0;
        this.userId = userId;
        this.campNumber = campNumber;
        this.ticketId = ticketId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCampNumber() {
        return campNumber;
    }

    public void setCampNumber(String campNumber) {
        this.campNumber = campNumber;
    }
}
