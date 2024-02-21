package ru.khvatov.learningprojects.testtasks.symbolfrequencycalculatorapi.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.khvatov.learningprojects.testtasks.symbolfrequencycalculatorapi.dto.SymbolFrequenciesDto;
import ru.khvatov.learningprojects.testtasks.symbolfrequencycalculatorapi.dto.SymbolSequenceDto;
import ru.khvatov.learningprojects.testtasks.symbolfrequencycalculatorapi.service.CalculatingSymbolFrequenciesService;

@RequestMapping("/calculate")
@RestController
@RequiredArgsConstructor
public class SymbolFrequenciesController {
    private final CalculatingSymbolFrequenciesService calculator;

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<SymbolFrequenciesDto> calcFrequency(@RequestParam(name = "symbolSequence") SymbolSequenceDto symbolSequenceDto) {

        final var result = calculator.calc(symbolSequenceDto);

        return ResponseEntity.ok(result);
    }
}
