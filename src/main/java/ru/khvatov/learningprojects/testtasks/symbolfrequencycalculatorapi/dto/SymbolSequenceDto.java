package ru.khvatov.learningprojects.testtasks.symbolfrequencycalculatorapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record SymbolSequenceDto(
        @NotEmpty(message = "symbolSequence must be present and not be empty.")
        @Size(min = 1, max = 300, message = "symbolSequence must have at least 1 character but not more than 300 as well.")
        String symbolSequence
) {
}
