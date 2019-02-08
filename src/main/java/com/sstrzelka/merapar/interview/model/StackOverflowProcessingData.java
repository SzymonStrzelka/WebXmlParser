package com.sstrzelka.merapar.interview.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StackOverflowProcessingData {
    private int overallScore;
    private int acceptedAnswerCount;
    private int count;
    private LocalDateTime youngestRow = LocalDateTime.MAX;
    private LocalDateTime oldestRow = LocalDateTime.MIN;

    public void updateData(StackOverflowRow row) {
        overallScore += row.getScore();
        count++;
        if (row.isAccepted())
            acceptedAnswerCount++;
        if (row.getCreationDate() != null) {
            if (youngestRow.compareTo(row.getCreationDate()) > 0)
                youngestRow = row.getCreationDate();
            if (oldestRow.compareTo(row.getCreationDate()) < 0)
                oldestRow = row.getCreationDate();
        }
    }

    public LocalDateTime getYoungestRow() {
        return youngestRow != LocalDateTime.MAX ? youngestRow : null;
    }

    public LocalDateTime getOldestRow() {
        return oldestRow != LocalDateTime.MIN ? oldestRow : null;
    }
}
