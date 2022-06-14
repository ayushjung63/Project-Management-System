package com.ayush.proms.model;

import com.ayush.proms.enums.Faculty;
import com.ayush.proms.enums.Role;
import com.ayush.proms.enums.Semester;
import com.ayush.proms.utils.AuditAbstract;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlEnum;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User extends AuditAbstract implements UserDetails {
    @Id
    @SequenceGenerator(name = "users_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "users_sequence")
    private Long id;


    private String fullName;


    private String password;


    private String email;

    @Enumerated(value = EnumType.STRING)
    private Faculty faculty;

    @Enumerated(value = EnumType.STRING)

    private Semester semester;

    @ManyToMany(mappedBy = "students")
    @JsonIgnore
    private List<Project> projectList;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String address;

    private String contact;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User(Long id) {
        this.id = id;
    }
}
