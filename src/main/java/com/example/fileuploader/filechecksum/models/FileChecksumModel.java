package com.example.fileuploader.filechecksum.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FILES_CHECKSUM")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FileChecksumModel {
    @Id
      private Integer ticketId;
      private String fileChecksum;
}
