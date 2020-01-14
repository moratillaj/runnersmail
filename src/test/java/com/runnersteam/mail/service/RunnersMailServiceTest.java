package com.runnersteam.mail.service;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.runnersteam.mail.model.RunnerMail;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class RunnersMailServiceTest {

  private static final String THE_SUBJECT = "theSubject";
  private static final String THE_TEXT = "theText";
  private static final List<String> THE_RECIPIENTS = of("runner1@mail.com", "runner2@mail.com");
  private static final String THE_SENDER = "theSender";

  @InjectMocks
  private RunnersMailService runnersMailService;

  @Mock
  private JavaMailSender javaMailSender;

  private RunnerMail runnerMail;

  @Captor
  private ArgumentCaptor<SimpleMailMessage> simpleMailMessageArgumentCaptor;

  @BeforeEach
  private void setUp() {
    runnerMail = RunnerMail.builder()
        .subject(THE_SUBJECT)
        .text(THE_TEXT)
        .from(THE_SENDER)
        .to(THE_RECIPIENTS)
        .build();
  }

  @Test
  public void shouldSendEmail() {
    //Given
    doNothing().when(javaMailSender).send(simpleMailMessageArgumentCaptor.capture());

    //When
    runnersMailService.sendMail(runnerMail);

    //Then
    verify(javaMailSender).send(simpleMailMessageArgumentCaptor.getValue());

    SimpleMailMessage simpleMailMessageSent = simpleMailMessageArgumentCaptor.getValue();
    assertThat(simpleMailMessageSent).extracting("subject", "text", "from", "to")
        .contains(THE_SUBJECT, THE_TEXT, THE_SENDER, THE_RECIPIENTS.toArray(new String[0]));

  }

  @Test
  public void shouldSendMailFailWhenSenderFails() {
    //Given
    String errorMessage = "Unable to send mail";
    doThrow(new RuntimeException(errorMessage)).when(javaMailSender)
        .send(simpleMailMessageArgumentCaptor.capture());

    //When && Then
    assertThatThrownBy(() -> runnersMailService.sendMail(runnerMail))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Unable to send mail");

    //Then
    verify(javaMailSender).send(simpleMailMessageArgumentCaptor.getValue());
    SimpleMailMessage simpleMailMessageSent = simpleMailMessageArgumentCaptor.getValue();
    assertThat(simpleMailMessageSent).extracting("subject", "text", "from", "to")
        .contains(THE_SUBJECT, THE_TEXT, THE_SENDER, THE_RECIPIENTS.toArray(new String[0]));
  }

}