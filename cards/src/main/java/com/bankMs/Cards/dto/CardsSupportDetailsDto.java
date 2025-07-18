package com.example.Cards.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "cards")
@Getter @Setter
public class CardsSupportDetailsDto {

    private String message;
    private Map<String, String> contactDetails;
    private List<String> onCallSupport;
}

