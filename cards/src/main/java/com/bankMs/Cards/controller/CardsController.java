package com.example.Cards.controller;

import com.example.Cards.constants.CardsConstants;
import com.example.Cards.dto.CardsDto;
import com.example.Cards.dto.CardsSupportDetailsDto;
import com.example.Cards.dto.ResponseDto;
import com.example.Cards.service.ICardsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/card", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class CardsController {

    @Autowired
    private final ICardsService cardsService;

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private Environment environment;

    @Autowired
    private CardsSupportDetailsDto cardsSupportDetailsDto;

    @Autowired
    public CardsController(ICardsService service) {
        this.cardsService = service;
    }

    @PostMapping(value = "/request-card")
    public ResponseEntity<ResponseDto> createCard( @RequestParam
                                                      @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                      String mobileNumber) {
        cardsService.createCard(mobileNumber);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(CardsConstants.STATUS_201, CardsConstants.MESSAGE_201));
    }

    @GetMapping(value = "/getCard")
    public ResponseEntity<CardsDto> getCardsDetails(@RequestParam
                                                        @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                        String mobileNumber) {
         CardsDto cards= cardsService.fetchCard(mobileNumber);
         return ResponseEntity.status(HttpStatus.OK).body(cards);
    }

    @PutMapping(value = "/update-card")
    public ResponseEntity<ResponseDto> updateCardDetails(@RequestBody @Valid CardsDto dto) {
        boolean isUpdated = cardsService.updateCard(dto);

        if (isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(CardsConstants.STATUS_417, CardsConstants.MESSAGE_417_UPDATE));
        }
    }

    @DeleteMapping(value = "delete-card")
    public ResponseEntity<ResponseDto> deleteCardDetails(@RequestParam
                                                             @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                             String mobileNumber) {
        boolean isDeleted = cardsService.deleteCard(mobileNumber);
        if (isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(CardsConstants.STATUS_417, CardsConstants.MESSAGE_417_DELETE));

        }
    }

    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(buildVersion);
    }

    @GetMapping("/get-env-variables")
    public ResponseEntity<String> getSystemInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(environment.getProperty("OS"));
    }

    @GetMapping("/get-support-details")
    public ResponseEntity<CardsSupportDetailsDto> getSupportInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(cardsSupportDetailsDto);
    }
}
