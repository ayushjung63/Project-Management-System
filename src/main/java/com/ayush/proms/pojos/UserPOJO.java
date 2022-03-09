package com.ayush.proms.pojos;

import com.ayush.proms.enums.Faculty;
import com.ayush.proms.enums.Semester;
import com.ayush.proms.model.Project;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPOJO {
    private Long id;
    private String fullName;
    private String password;
    private String email;
    private Faculty faculty;
    private Semester semester;
    private Set<Project> projects=new HashSet<>();
}
