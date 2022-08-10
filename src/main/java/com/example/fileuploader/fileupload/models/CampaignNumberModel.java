package com.example.fileuploader.fileupload.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "USER_CAMPAIGN_NUMBER")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class CampaignNumberModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Integer id;
    @Getter
    @Setter
    @NonNull
    private int userId;
    @Getter
    @Setter
    @NonNull
    private String phoneNumber;
    @Getter
    @Setter
    @NonNull
    private int ticketId;
}
