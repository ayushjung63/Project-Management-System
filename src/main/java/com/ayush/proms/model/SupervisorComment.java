package com.ayush.proms.model;

import com.ayush.proms.utils.AuditAbstract;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "supervisor_comment")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SupervisorComment extends AuditAbstract {

    @Id
    @SequenceGenerator(name = "comment_sequnce",sequenceName = "comment_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "comment_sequence")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;

    @Column(name = "comment",columnDefinition = "TEXT")
    private String comment;
}
