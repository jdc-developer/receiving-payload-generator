package com.mozaiko.readfile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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
    private List<Epc> readings = new ArrayList<>();

    public void addReading(String epc) {
        readings.add(new Epc(epc));
    }
}
