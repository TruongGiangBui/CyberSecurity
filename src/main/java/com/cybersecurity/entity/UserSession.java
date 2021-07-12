package com.cybersecurity.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "usersession")
public class UserSession {
    @Id
    @Column(name = "sessionid")
    private String sessionID;
    String username;
}
