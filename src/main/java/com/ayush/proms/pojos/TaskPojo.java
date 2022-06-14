package com.ayush.proms.pojos;

import com.ayush.proms.enums.Status;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskPojo {
    private Long id;
    private String title;
    private String description;
    private Status status;
    private Long projectId;
    private String addedBy;
}
