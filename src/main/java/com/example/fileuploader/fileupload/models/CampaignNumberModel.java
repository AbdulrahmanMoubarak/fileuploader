package com.example.fileuploader.fileupload.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "USER_CAMPAIGN_NUMBER")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class CampaignNumberModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    
    
    private Integer id;
    
    
    @NonNull
    private int userId;
    
    
    @NonNull
    private String phoneNumber;
    
    
    @NonNull
    private int ticketId;
}
