package ru.khvatov.learningprojects.testtasks.symbolfrequencycalculatorapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.LinkedHashMap;
import java.util.Map;

public record SymbolFrequenciesDto(
        @Schema(description = "`symbol:its_frequency` entries sorted in descending order by their frequency value",
                implementation = LinkedHashMap.class,
                example = """
                        {
                            "a": 8,
                            "b": 5,
                            "c": 3,
                            "d": 1
                        }
                        """
        )
        Map<Character, Long> symbolFrequencies) {
}
