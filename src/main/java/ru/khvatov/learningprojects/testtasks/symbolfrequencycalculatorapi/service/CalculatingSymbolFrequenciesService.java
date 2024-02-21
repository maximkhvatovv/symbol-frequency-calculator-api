package ru.khvatov.learningprojects.testtasks.symbolfrequencycalculatorapi.service;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import ru.khvatov.learningprojects.testtasks.symbolfrequencycalculatorapi.dto.SymbolFrequenciesDto;
import ru.khvatov.learningprojects.testtasks.symbolfrequencycalculatorapi.dto.SymbolSequenceDto;

@Validated
public interface CalculatingSymbolFrequenciesService {
    SymbolFrequenciesDto calc(@Valid final SymbolSequenceDto symbolSequence);
}
