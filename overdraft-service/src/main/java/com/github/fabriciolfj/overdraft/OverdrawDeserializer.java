package com.github.fabriciolfj.overdraft;


import io.quarkus.kafka.client.serialization.JsonbDeserializer;

public class OverdrawDeserializer extends JsonbDeserializer<OverdrawDTO> {

    public OverdrawDeserializer() {
        super(OverdrawDTO.class);
    }
}
