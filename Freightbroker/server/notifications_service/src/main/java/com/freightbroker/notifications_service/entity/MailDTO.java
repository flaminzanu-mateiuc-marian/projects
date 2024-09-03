package com.freightbroker.notifications_service.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MailDTO {
    String recipient = "";
    String subject = "";
    String content = "";

}
