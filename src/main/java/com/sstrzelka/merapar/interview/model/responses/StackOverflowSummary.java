package com.sstrzelka.merapar.interview.model.responses;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class StackOverflowSummary {
    private final LocalDateTime firstPost;
    private final LocalDateTime lastPost;
    private final long totalPosts;
    private final long totalAcceptedPosts;
    private final double avgScore;
}
