package com.mycompany.lab1missionanalyzer.parser;

import com.mycompany.lab1missionanalyzer.*;
import java.io.File;
import java.io.IOException;

public interface MissionParser {
    Mission parse(File file) throws IOException;
}