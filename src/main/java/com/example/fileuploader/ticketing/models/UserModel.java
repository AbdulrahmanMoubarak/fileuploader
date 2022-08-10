package com.example.fileuploader.ticketing.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "SYSTEM_USERS")
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    @NonNull
    private String name;
}
