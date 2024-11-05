package com.mozaiko.readfile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceivingPayload {

    @JsonProperty("_handling")
    private String handling;

    @JsonProperty("_request")
    private String request;
    private List<String> readings;
}
