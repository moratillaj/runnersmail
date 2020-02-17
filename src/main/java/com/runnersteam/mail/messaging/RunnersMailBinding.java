package com.runnersteam.mail.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

public interface RunnersMailBinding {

  String NEW_RUNNER_REGISTRATION_INPUT = "newRunnerRegistrationInput";
  String RACE_TIME_REGISTRATION_INPUT = "raceTimeRegistrationInput";

  @Input(NEW_RUNNER_REGISTRATION_INPUT)
  MessageChannel newRunnerRegistrationInput();

  @Input(RACE_TIME_REGISTRATION_INPUT)
  MessageChannel raceTimeRegistrationInput();

}
