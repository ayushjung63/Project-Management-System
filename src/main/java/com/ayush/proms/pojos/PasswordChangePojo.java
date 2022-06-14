package com.ayush.proms.pojos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangePojo {
    private String currentPassword;
    private String newPassword;
}
