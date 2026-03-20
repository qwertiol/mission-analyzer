package com.mycompany.lab1missionanalyzer.parser;

import com.mycompany.lab1missionanalyzer.Mission;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // для даты миссии
import java.io.File;
import java.io.IOException;

public class XmlMissionParser implements MissionParser {
    private final XmlMapper xmlMapper;

    public XmlMissionParser() {
        xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new JavaTimeModule()); // для даты миссии
    }

    @Override
    public Mission parse(File file) throws IOException {
        return xmlMapper.readValue(file, Mission.class); // здесь умный джексон все делает
    }
}