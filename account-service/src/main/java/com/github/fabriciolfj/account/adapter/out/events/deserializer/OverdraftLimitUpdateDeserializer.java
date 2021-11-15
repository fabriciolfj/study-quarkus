package com.github.fabriciolfj.account.adapter.out.events.deserializer;


import com.github.fabriciolfj.account.adapter.out.events.dto.OverdraftLimitUpdateDTO;
import io.quarkus.kafka.client.serialization.JsonbDeserializer;

public class OverdraftLimitUpdateDeserializer extends JsonbDeserializer<OverdraftLimitUpdateDTO> {
    public OverdraftLimitUpdateDeserializer() {
        super(OverdraftLimitUpdateDTO.class);
    }
}