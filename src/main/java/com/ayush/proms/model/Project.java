package com.ayush.proms.model;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "project")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Project {
    @Id
    @SequenceGenerator(name = "project_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "project_sequence")
    private Long id;
    private String title;

    @ElementCollection
    @JoinTable(name = "project_tool_id")
    private List<String> projectTools;
    private String shortName;

    @Temporal(TemporalType.DATE)
    private Date start_date;

    @Temporal(TemporalType.DATE)
    private Date end_date;

    @ManyToMany(mappedBy = "projects")
    private Set<User> students=new HashSet<>();

    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_supervisor"),name = "supervisor_id")
    private User supervisor;
}
