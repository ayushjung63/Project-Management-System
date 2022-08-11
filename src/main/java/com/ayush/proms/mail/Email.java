package com.ayush.proms.mail;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Email {
    private String from;
    private String to;
    private String subject;
    private String body;
}
