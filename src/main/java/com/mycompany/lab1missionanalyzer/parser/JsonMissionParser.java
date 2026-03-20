package com.mycompany.lab1missionanalyzer.parser;

import com.mycompany.lab1missionanalyzer.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // для даты миссии
import java.io.File;
import java.io.IOException;

public class JsonMissionParser implements MissionParser {
    private final ObjectMapper mapper;

    public JsonMissionParser() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // для даты миссии
    }

    @Override
    public Mission parse(File file) throws IOException {
        return mapper.readValue(file, Mission.class); // здесь умный джексон все делает
    }
}