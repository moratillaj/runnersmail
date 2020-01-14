package com.runnersteam.mail.service;

import com.runnersteam.mail.model.RunnerMail;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RunnersMailService {

  @Autowired
  private JavaMailSender javaMailSender;

  private Function<RunnerMail, SimpleMailMessage> toSimpleMailMessage = this::createMailMessage;

  public void sendMail(RunnerMail runnerMail) {
    toSimpleMailMessage.andThen(this::send).apply(runnerMail);
  }

  private boolean send(SimpleMailMessage simpleMailMessage) {
    javaMailSender.send(simpleMailMessage);
    return true;
  }

  private SimpleMailMessage createMailMessage(RunnerMail runnerMail) {
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setSubject(runnerMail.getSubject());
    simpleMailMessage.setText(runnerMail.getText());
    simpleMailMessage.setFrom(runnerMail.getFrom());
    simpleMailMessage.setTo(runnerMail.getTo().toArray(new String[0]));

    return simpleMailMessage;
  }
}
