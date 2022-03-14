package com.ayush.proms.model;

import com.ayush.proms.enums.DocumentStatus;
import com.ayush.proms.enums.DocumentType;
import com.ayush.proms.enums.MIMEType;
import com.ayush.proms.utils.AuditAbstract;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "document")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Document extends AuditAbstract {
    @Id
    @SequenceGenerator(name = "document_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "document_sequence")
    private Long id;
    private String title;
    private String url;

    @Enumerated(value = EnumType.STRING)
    private DocumentType documentType;

    @Enumerated(value = EnumType.STRING)
    private MIMEType mimeType;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",foreignKey = @ForeignKey(name = "FK_user"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id",foreignKey = @ForeignKey(name = "FK_project"))
    private Project project;

    @Enumerated(value = EnumType.STRING)
    private DocumentStatus documentStatus;
}
