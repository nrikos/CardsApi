package com.logicea.cards.api.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.logicea.cards.api.payloads.CardStatus;
import com.logicea.cards.api.validations.color.ColorConstraint;
import com.logicea.cards.api.validations.status.StatusConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDTO  {


    private Long cardId;
    @StatusConstraint
    private CardStatus status;
    @ColorConstraint
    private String color;
    private String description;
    @NotBlank(message = "Card name is mandatory")
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date createdAt;


    public static List<String> getNonPatchAbleAttributes() {
        return Arrays.asList("/cardId", "/createdAt");
    }
}
