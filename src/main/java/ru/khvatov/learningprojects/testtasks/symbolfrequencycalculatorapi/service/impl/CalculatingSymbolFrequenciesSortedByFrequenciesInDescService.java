package ru.khvatov.learningprojects.testtasks.symbolfrequencycalculatorapi.service.impl;

import org.springframework.stereotype.Service;
import ru.khvatov.learningprojects.testtasks.symbolfrequencycalculatorapi.dto.SymbolFrequenciesDto;
import ru.khvatov.learningprojects.testtasks.symbolfrequencycalculatorapi.dto.SymbolSequenceDto;
import ru.khvatov.learningprojects.testtasks.symbolfrequencycalculatorapi.service.CalculatingSymbolFrequenciesService;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import static java.util.Collections.reverseOrder;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

@Service
public class CalculatingSymbolFrequenciesSortedByFrequenciesInDescService
        implements CalculatingSymbolFrequenciesService {
    @Override
    public SymbolFrequenciesDto calc(final SymbolSequenceDto symbolSequenceDto) {
        validate(symbolSequenceDto);
        Map<Character, Long> symbolFrequencies = symbolSequenceDto.symbolSequence()
                .codePoints()
                .mapToObj(codePoint -> (char) codePoint)
                .collect(
                        groupingBy(Function.identity(), LinkedHashMap::new, counting())
                );

        Map<Character, Long> sortedSymbolFrequencies = symbolFrequencies.entrySet().stream()
                .sorted(reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (o, n) -> o, LinkedHashMap::new)
                );

        return new SymbolFrequenciesDto(sortedSymbolFrequencies);

    }

    private void validate(SymbolSequenceDto symbolSequenceDto) {
        if (isNull(symbolSequenceDto)) {
            throw new IllegalArgumentException("symbolSequenceDto must be not null.");
        }
        if (isNull(symbolSequenceDto.symbolSequence())) {
            throw new IllegalArgumentException("symbolSequence must be not null.");
        }
    }

}
