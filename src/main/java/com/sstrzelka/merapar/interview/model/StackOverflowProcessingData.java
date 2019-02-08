package com.sstrzelka.merapar.interview.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StackOverflowProcessingData {
    private int overallScore;
    private int acceptedAnswerCount;
    private int count;
    private LocalDateTime youngestRow;
    private LocalDateTime oldestRow;

    public void updateData(StackOverflowRow row) {
        if (row == null)
            return;
        overallScore += row.getScore();
        count++;
        if (row.isAccepted())
            acceptedAnswerCount++;
        if (row.getCreationDate() != null) {
            if (youngestRow == null && oldestRow == null)
                youngestRow = oldestRow = row.getCreationDate();
            else {
                if (youngestRow.compareTo(row.getCreationDate()) > 0)
                    youngestRow = row.getCreationDate();
                if (oldestRow.compareTo(row.getCreationDate()) < 0)
                    oldestRow = row.getCreationDate();
            }
        }
    }
}
