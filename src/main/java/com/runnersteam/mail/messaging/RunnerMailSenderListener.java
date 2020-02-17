package com.runnersteam.mail.messaging;

import static com.runnersteam.mail.messaging.RunnersMailBinding.NEW_RUNNER_REGISTRATION_INPUT;
import static com.runnersteam.mail.messaging.RunnersMailBinding.RACE_TIME_REGISTRATION_INPUT;
import static java.util.Collections.singletonList;

import com.runnersteam.mail.model.Race;
import com.runnersteam.mail.model.Runner;
import com.runnersteam.mail.model.RunnerMail;
import com.runnersteam.mail.service.RunnersMailService;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@EnableBinding(RunnersMailBinding.class)
public class RunnerMailSenderListener {

  @Autowired
  private RunnersMailService runnersMailService;

  @Value("${runnersmail.pandaRunnersMailAddress}")
  private String pandaRunnersMailAddress;


  @StreamListener(NEW_RUNNER_REGISTRATION_INPUT)
  public void sendNewRunnerRegistrationMail(Runner runner) {
    sendMail(createNewRunnerRegistrationMail(runner));
  }

  @StreamListener(RACE_TIME_REGISTRATION_INPUT)
  public void sendRaceTimeRegistrationMail(Race race) {
    sendMail(createRaceTimeRegistrationMail(race));
  }

  private void sendMail(Supplier<RunnerMail> runnerMailSupplier) {
    runnersMailService.sendMail(runnerMailSupplier.get());
  }

  private Supplier<RunnerMail> createNewRunnerRegistrationMail(Runner runner) {
    return () -> RunnerMail.builder()
        .from(pandaRunnersMailAddress)
        .to(singletonList(runner.getEmail()))
        .subject("Welcome to Panda del Muro !")
        .text("Have a warm welcome to the team, mr/ms " + runner.getNickname())
        .build();
  }

  private Supplier<RunnerMail> createRaceTimeRegistrationMail(Race race) {
    return () -> RunnerMail.builder()
        .from(pandaRunnersMailAddress)
        .to(singletonList(race.getRunnerEmail()))
        .subject("New finished race registered!")
        .text("You've run " + race.getRaceName() + " on " + race.getRaceDate()
            + " with a distance of " + race.getDistanceKm() + "!!\n"
            + "Your time was " + race.getCompletedRaceTimeSeconds())
        .build();
  }

}
