package com.ayush.proms.model;
import com.ayush.proms.enums.ProjectStatus;
import com.ayush.proms.utils.AuditAbstract;
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
public class Project extends AuditAbstract {
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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "project_users",
            joinColumns = {@JoinColumn(name = "project_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private List<User> students=new ArrayList<>();

    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_supervisor"),name = "supervisor_id")
    private User supervisor;

    @Enumerated(value = EnumType.STRING)
    private ProjectStatus projectStatus;
}
