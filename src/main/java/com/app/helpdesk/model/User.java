package com.app.helpdesk.model;

import com.app.helpdesk.model.enums.Role;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"firstname", "lastname", "role", "email", "password"})
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "first_name", nullable = false)
    private String firstname;

    @Column(name = "last_name", nullable = false)
    private String lastname;

    @Column(name = "role_id")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "assignee")
    private List<Ticket> ticket = new ArrayList<>();

    @OneToMany(mappedBy = "approver")
    private List<Ticket> approved = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<Ticket> ticketOwned = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Feedback> feedback = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

}
