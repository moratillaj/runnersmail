package com.runnersteam.mail.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Race {

  private String raceName;

  private String runnerEmail;

  private LocalDate raceDate;

  private Float distanceKm;

  private boolean completed;

  private Integer completedRaceTimeSeconds;

}
