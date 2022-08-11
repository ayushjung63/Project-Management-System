package com.ayush.proms.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "email_crendential")
public class EmailCredential {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String password;
}
