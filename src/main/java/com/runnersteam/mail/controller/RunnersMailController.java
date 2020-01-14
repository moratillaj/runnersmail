package com.runnersteam.mail.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.runnersteam.mail.model.RunnerMail;
import com.runnersteam.mail.service.RunnersMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Slf4j
public class RunnersMailController {

  @Autowired
  private RunnersMailService runnersMailService;

  @PostMapping("/")
  @ResponseStatus(CREATED)
  public RunnerMail sendMail(@RequestBody RunnerMail runnerMail) {
    log.info("POST-sendMail-" + runnerMail);
    runnersMailService.sendMail(runnerMail);
    return runnerMail;
  }

  private RunnerMail sendRunnersMail(RunnerMail runnerMail) {
    runnersMailService.sendMail(runnerMail);
    return runnerMail;
  }

}
