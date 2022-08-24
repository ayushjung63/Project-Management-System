package com.ayush.proms.pojos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SupervisorCommentPOJO {
    private Long id;

    private Long documentId;

    private String comment;
}
