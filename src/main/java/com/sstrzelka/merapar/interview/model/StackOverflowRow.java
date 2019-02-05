package com.sstrzelka.merapar.interview.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class StackOverflowRow {
    private final int score;
    private final boolean isAccepted;
    private final LocalDateTime creationDate;
}
