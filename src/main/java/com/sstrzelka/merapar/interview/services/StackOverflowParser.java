package com.sstrzelka.merapar.interview.services;

import com.sstrzelka.merapar.interview.model.StackOverflowRow;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import java.time.LocalDateTime;

@Service
public class StackOverflowParser {
    private static final String SCORE_FIELD = "Score";
    private static final String ACCEPTED_ANSWER_ID_FIELD = "AcceptedAnswerId";
    private static final String CREATION_DATE_FIELD = "CreationDate";

    StackOverflowRow parseRow(@NonNull StartElement element) {
        Attribute score = element.getAttributeByName(new QName(SCORE_FIELD));
        Attribute acceptedAnswerId = element.getAttributeByName(new QName(ACCEPTED_ANSWER_ID_FIELD));
        Attribute creationDate = element.getAttributeByName(new QName(CREATION_DATE_FIELD));


        StackOverflowRow.StackOverflowRowBuilder rowBuilder = StackOverflowRow.builder()
                .isAccepted(acceptedAnswerId != null);
        if (score != null)
            rowBuilder.score(Integer.valueOf(score.getValue()));
        if (creationDate != null)
            rowBuilder.creationDate(LocalDateTime.parse(creationDate.getValue()));
        return rowBuilder.build();
    }

}
