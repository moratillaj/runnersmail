package com.runnersteam.mail.controller;

import static java.net.URI.create;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.runnersteam.mail.config.RunnersMailExceptionHandler;
import com.runnersteam.mail.controller.RunnersMailControllerTest.RunnersMailControllerTestConfig;
import com.runnersteam.mail.model.RunnerMail;
import com.runnersteam.mail.service.RunnersMailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RunnersMailController.class)
@ContextConfiguration(classes = {RunnersMailController.class, RunnersMailService.class,
    RunnersMailControllerTestConfig.class, RunnersMailExceptionHandler.class})
class RunnersMailControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private JavaMailSender javaMailSender;

  @Captor
  private ArgumentCaptor<SimpleMailMessage> runnerMailArgumentCaptor;

  @Test
  public void shouldSendMail() throws Exception {
    //Given
    RunnerMail runnerMail = RunnerMail.builder()
        .subject("theSubject")
        .text("theText")
        .from("theSender")
        .to(of("runner1@email.com", "runner2@email.com"))
        .build();
    doNothing().when(javaMailSender).send(runnerMailArgumentCaptor.capture());

    //When && Then
    mockMvc.perform(post(create("/"))
        .contentType(APPLICATION_JSON)
        .content(toJson(runnerMail)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.subject", is(runnerMail.getSubject())))
        .andExpect(jsonPath("$.text", is(runnerMail.getText())))
        .andExpect(jsonPath("$.from", is(runnerMail.getFrom())))
        .andExpect(jsonPath("$.to", is(runnerMail.getTo())));

    verify(javaMailSender).send(runnerMailArgumentCaptor.capture());
    SimpleMailMessage simpleMailMessage = runnerMailArgumentCaptor.getValue();
    assertThat(simpleMailMessage)
        .extracting("subject", "text", "from", "to")
        .contains(runnerMail.getSubject(), runnerMail.getText(), runnerMail.getFrom(),
            runnerMail.getTo().toArray(new String[0]));
  }

  @Test
  public void shouldSendMailReturnErrorWhenMailSenderFail() throws Exception {
    //Given
    RunnerMail runnerMail = RunnerMail.builder()
        .subject("theSubject")
        .text("theText")
        .from("theSender")
        .to(of("runner1@email.com", "runner2@email.com"))
        .build();
    doThrow(new RuntimeException("unable to send email"))
        .when(javaMailSender).send(runnerMailArgumentCaptor.capture());

    //When && Then
    mockMvc.perform(post(create("/"))
        .contentType(APPLICATION_JSON)
        .accept(APPLICATION_JSON)
        .content(toJson(runnerMail)))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.errorMessage", is("unable to send email")));

    verify(javaMailSender).send(runnerMailArgumentCaptor.capture());
    SimpleMailMessage simpleMailMessage = runnerMailArgumentCaptor.getValue();
    assertThat(simpleMailMessage)
        .extracting("subject", "text", "from", "to")
        .contains(runnerMail.getSubject(), runnerMail.getText(), runnerMail.getFrom(),
            runnerMail.getTo().toArray(new String[0]));
  }

  private String toJson(RunnerMail runnerMail) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(runnerMail);
  }

  @Configuration
  @SpringBootApplication
  static class RunnersMailControllerTestConfig {

    @Mock
    private JavaMailSender javaMailSender;

    @Bean
    public JavaMailSender javaMailSender() {
      return javaMailSender;
    }
  }
}