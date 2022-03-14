package com.ayush.proms.pojos;

import com.ayush.proms.enums.Faculty;
import com.ayush.proms.enums.Semester;
import com.ayush.proms.model.Project;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPOJO {
    private Long id;

    @NotNull(message = "Full Name is compulsory.")
    @Size(min = 5,message = "Full Name cannot be less than 5 characters..")
    private String fullName;

    @NotNull(message = "Password is compulsory.")
    @Size(min = 8,message = "Password cannot be less than 8 characters.")
    private String password;

    @NotNull(message = "Email is compulsory.")
    private String email;

    @NotNull(message = "Faculty is compulsory.")
    private Faculty faculty;

    @NotNull(message = "Semester is compulsory.")
    private Semester semester;
    private Set<Project> projects=new HashSet<>();
}
