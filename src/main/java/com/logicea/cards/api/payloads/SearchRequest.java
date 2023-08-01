package com.logicea.cards.api.payloads;

import com.logicea.cards.api.validations.sort.SortFieldConstraint;
import com.logicea.cards.api.validations.sort.SortOrderConstrain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchRequest<T> {
    @Min(value = 1,message = "Page size must not be less than one")
    private int resultsPerPage;
    private int pageNumber;
    @SortFieldConstraint
    private String sortField;
    @SortOrderConstrain
    private String sortOrder;
    T example;
}

