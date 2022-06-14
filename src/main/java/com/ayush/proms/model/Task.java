package com.ayush.proms.model;

import com.ayush.proms.enums.Status;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "task")
public class Task {

    @Id
    @SequenceGenerator(name = "task_sequence",sequenceName = "task_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "task_sequence")
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "project_id",foreignKey = @ForeignKey(name = "FK_project"))
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id",foreignKey = @ForeignKey(name = "FK_project"))
    private User user;
}

