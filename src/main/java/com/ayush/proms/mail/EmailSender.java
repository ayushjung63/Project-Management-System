package com.ayush.proms.mail;

import com.ayush.proms.pojos.UserPOJO;

import java.util.*;

public interface EmailSender {
    void  sendLoginCredential(Email email);

    void sendSupervisorAssignedMail(Email email);
}
