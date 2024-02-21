package ru.khvatov.learningprojects.testtasks.symbolfrequencycalculatorapi.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.khvatov.learningprojects.testtasks.symbolfrequencycalculatorapi.dto.SymbolFrequenciesDto;
import ru.khvatov.learningprojects.testtasks.symbolfrequencycalculatorapi.dto.SymbolSequenceDto;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class CalculatingSymbolFrequenciesSortedByFrequenciesInDescServiceTest {
    private final CalculatingSymbolFrequenciesSortedByFrequenciesInDescService
            service = new CalculatingSymbolFrequenciesSortedByFrequenciesInDescService();

    @Test
    void shouldThrowIllegalArgumentExceptionWhenSymbolSequenceDtoIsNull() {

        //when
        Executable whenSymbolSequenceDtoIsNull = () -> service.calc(null);

        //then
        var thrownException = Assertions.assertThrows(IllegalArgumentException.class, whenSymbolSequenceDtoIsNull);
        Assertions.assertEquals("symbolSequenceDto must be not null.", thrownException.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenSymbolSequenceFieldIsNull() {
        //given
        SymbolSequenceDto symbolSequenceDto = new SymbolSequenceDto(null);

        //when
        Executable whenSymbolSequenceFieldIsNull = () -> service.calc(symbolSequenceDto);

        //then
        var thrownException = Assertions.assertThrows(IllegalArgumentException.class, whenSymbolSequenceFieldIsNull);
        Assertions.assertEquals("symbolSequence must be not null.", thrownException.getMessage());
    }

    @Test
    void shouldReturnEmptyMapWhenSymbolSequenceIsEmpty() {
        //given
        final SymbolSequenceDto symbolSequenceDto = new SymbolSequenceDto("");

        //when
        SymbolFrequenciesDto symbolFrequenciesDto = service.calc(symbolSequenceDto);

        //then
        Assertions.assertTrue(symbolFrequenciesDto.symbolFrequencies().isEmpty());
    }

    @ParameterizedTest
    @MethodSource("argumentsForCalculatingSymbolToFrequencyEntriesSortedByTheirFrequenciesInDesc")
    void shouldReturnSymbolToFrequencyEntriesSortedByTheirFrequenciesInDescWhenCalculateSymbolFrequencies(
            SymbolSequenceDto symbolSequenceDto,
            SymbolFrequenciesDto expectedSymbolFrequenciesDto
    ) {

        //when
        final SymbolFrequenciesDto actualSymbolFrequenciesDto = service.calc(symbolSequenceDto);

        //then
        final Map<Character, Long> actualSymbolFrequencies = actualSymbolFrequenciesDto.symbolFrequencies();
        final Map<Character, Long> expectedSymbolFrequencies = expectedSymbolFrequenciesDto.symbolFrequencies();

        assertEquals(expectedSymbolFrequencies.size(), actualSymbolFrequencies.size());
        assertEquals(expectedSymbolFrequencies, actualSymbolFrequencies);

        //check that symbol:symbol_frequency entries is in correct order as well
        Iterator<Map.Entry<Character, Long>> expectedEntriesIterator = expectedSymbolFrequencies.entrySet().iterator();
        Iterator<Map.Entry<Character, Long>> actualEntriesIterator = actualSymbolFrequencies.entrySet().iterator();

        while (expectedEntriesIterator.hasNext()) {
            Map.Entry<Character, Long> expectedEntry = expectedEntriesIterator.next();
            Map.Entry<Character, Long> actualEntry = actualEntriesIterator.next();
            assertEquals(expectedEntry, actualEntry);
        }

    }

    static Stream<Arguments> argumentsForCalculatingSymbolToFrequencyEntriesSortedByTheirFrequenciesInDesc() {
        final Arguments onlyOneLetter = Arguments.of(
                "a",
                new SymbolFrequenciesDto(Map.of('a', 1L))
        );
        final Arguments usual = Arguments.of(
                "aaaaabcccc",
                new SymbolFrequenciesDto(
                        new LinkedHashMap<>() {
                            {
                                put('a', 5L);
                                put('c', 4L);
                                put('b', 1L);
                            }
                        }
                )
        );
        final Arguments containsSymbolsWithEqualSymbolsFrequencies = Arguments.of("dddfffsscc",
                new SymbolFrequenciesDto(
                        new LinkedHashMap<>() {
                            {
                                put('d', 3L);
                                put('f', 3L);
                                put('s', 2L);
                                put('c', 2L);
                            }
                        }
                )
        );
        return Stream.of(onlyOneLetter, usual, containsSymbolsWithEqualSymbolsFrequencies);
    }

    @Test
    @DisplayName("should preserve original order of symbols in symbolSequence when calculate symbol frequencies entries")
    void shouldPreserveOriginalOrderOfSymbolSequenceWhenCalculateSymbolFrequenciesContainingSymbolsWithEqualSymbolFrequencies() {
        //given
        SymbolSequenceDto symbolSequenceDto = new SymbolSequenceDto("afafafbccbg");

        //when
        SymbolFrequenciesDto symbolFrequenciesDto = service.calc(symbolSequenceDto);

        //then
        var expectedSymbolFrequenciesDtoThatPreserveOriginalOrderOfSymbolsInSymbolSequence
                = new SymbolFrequenciesDto(
                new LinkedHashMap<>() {
                    {
                        put('a', 3L);
                        put('f', 3L);
                        put('b', 2L);
                        put('c', 2L);
                        put('g', 1L);
                    }
                }
        );

        final Map<Character, Long> actualSymbolFrequencies = symbolFrequenciesDto.symbolFrequencies();
        final Map<Character, Long> expectedSymbolFrequencies =
                expectedSymbolFrequenciesDtoThatPreserveOriginalOrderOfSymbolsInSymbolSequence.symbolFrequencies();

        assertEquals(expectedSymbolFrequencies.size(), actualSymbolFrequencies.size());
        assertEquals(expectedSymbolFrequencies, actualSymbolFrequencies);

        //check that symbol:symbol_frequency entries is in correct order
        Iterator<Map.Entry<Character, Long>> expectedEntriesIterator = expectedSymbolFrequencies.entrySet().iterator();
        Iterator<Map.Entry<Character, Long>> actualEntriesIterator = actualSymbolFrequencies.entrySet().iterator();

        while (expectedEntriesIterator.hasNext()) {
            Map.Entry<Character, Long> expectedEntry = expectedEntriesIterator.next();
            Map.Entry<Character, Long> actualEntry = actualEntriesIterator.next();
            assertEquals(expectedEntry, actualEntry);
        }
    }
}