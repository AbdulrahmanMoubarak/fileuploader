package com.example.fileuploader.fileupload.repositories;

import com.example.fileuploader.fileupload.models.CampaignNumberModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignNumbersRepository extends JpaRepository<CampaignNumberModel, Integer> {
}
