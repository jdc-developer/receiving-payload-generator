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
public class InventoryPayload {

    @JsonProperty("_inventory")
    private String inventory;

    @JsonProperty("_local")
    private String local;
    private List<String> epcs = new ArrayList<>();

    public void addEpc(String epc) {
        epcs.add(epc);
    }
}
