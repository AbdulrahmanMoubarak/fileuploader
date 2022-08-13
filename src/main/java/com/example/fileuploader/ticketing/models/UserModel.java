package com.example.fileuploader.ticketing.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "SYSTEM_USERS")
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NonNull
    private String name;
}
