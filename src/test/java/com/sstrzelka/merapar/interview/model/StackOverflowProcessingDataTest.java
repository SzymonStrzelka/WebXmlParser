package com.sstrzelka.merapar.interview.model;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class StackOverflowProcessingDataTest {

    private static final StackOverflowProcessingData data = new StackOverflowProcessingData();
    private static StackOverflowRow VALID_ROW_1;
    private static StackOverflowRow VALID_ROW_2;

    @Before
    public void setUp() throws Exception {
        VALID_ROW_1 = StackOverflowRow.builder()
                .score(50)
                .isAccepted(true)
                .creationDate(LocalDateTime.of(2012, 1, 1, 12, 0))
                .build();

        VALID_ROW_2 = StackOverflowRow.builder()
                .score(4)
                .isAccepted(false)
                .creationDate(LocalDateTime.of(2018, 10, 28, 12, 0))
                .build();
    }

    @Test
    public void updateDataShouldReturnCorrectValueOnCorrectInput() {
        data.updateData(VALID_ROW_1);
        data.updateData(VALID_ROW_2);

        assertEquals(2, data.getCount());
        assertEquals(1, data.getAcceptedAnswerCount());
        assertEquals(VALID_ROW_1.getScore() + VALID_ROW_2.getScore(), data.getOverallScore());
        assertEquals(VALID_ROW_1.getCreationDate(), data.getYoungestRow());
        assertEquals(VALID_ROW_2.getCreationDate(), data.getOldestRow());
    }

    @Test
    public void updateShouldDoNothingOnNullParameter() {
        StackOverflowProcessingData unchangedData = new StackOverflowProcessingData();
        data.updateData(null);
        assertEquals(data, unchangedData);
    }
}