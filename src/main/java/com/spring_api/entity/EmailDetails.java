package com.spring_api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Data
@AllArgsConstructor
@NoArgsConstructor
 
public class EmailDetails {

  public EmailDetails(String recipient, String msgBody, String subject) {
    this.recipient = recipient;
    this.msgBody = msgBody;
    this.subject = subject;
  }

    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
}
