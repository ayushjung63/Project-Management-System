package com.ayush.proms.model;

import com.ayush.proms.enums.Faculty;
import com.ayush.proms.enums.Semester;
import com.ayush.proms.utils.AuditAbstract;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.util.*;

import javax.persistence.*;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User extends AuditAbstract {
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

}
