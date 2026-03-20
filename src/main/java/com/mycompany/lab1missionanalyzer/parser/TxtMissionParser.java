package com.mycompany.lab1missionanalyzer.parser;

import com.mycompany.lab1missionanalyzer.*;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class TxtMissionParser implements MissionParser {

    @Override
    public Mission parse(File file) throws IOException {
        Map<String, String> map = new LinkedHashMap<>(); // создаем карту для хранения пар ключ-значение
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) { // читаем файл по строкам, пока не дойдем до последней
                line = line.trim(); // чистка строки от пробелов
                if (line.isEmpty()) continue; // пропуск пустых строк
                int colonIdx = line.indexOf(':');
                if (colonIdx == -1) continue; // пропуск строк без разделителя ключа и значения
                String key = line.substring(0, colonIdx).trim(); // ключ это подстрока до двоеточия
                String value = line.substring(colonIdx + 1).trim(); // значение это подстрока после двоеточия
                map.put(key, value); // кладем пару в карту
            }
        }

        Mission mission = new Mission();

        // Обработка полей миссии (простые)
        mission.setMissionId(map.get("missionId"));
        mission.setDate(LocalDate.parse(map.get("date"))); // каст к дате
        mission.setLocation(map.get("location"));
        mission.setOutcome(map.get("outcome"));
        mission.setDamageCost(Long.parseLong(map.get("damageCost"))); // каст к лонгу

        // Проклятие
        Curse curse = new Curse();
        curse.setName(map.get("curse.name"));
        curse.setThreatLevel(map.get("curse.threatLevel"));
        mission.setCurse(curse);

        // Маги
        List<Sorcerer> sorcerers = new ArrayList<>();
        // Собираем все индексы магов
        Set<Integer> sorcererIndices = new TreeSet<>();
        for (String key : map.keySet()) {
            if (key.startsWith("sorcerer[")) {
                int idxStart = key.indexOf('[') + 1;
                int idxEnd = key.indexOf(']');
                int idx = Integer.parseInt(key.substring(idxStart, idxEnd)); // каст к инту
                sorcererIndices.add(idx);
            }
        }
        // Для каждого индекса создаем мага и заполняем его поля
        for (Integer idx : sorcererIndices) {
            Sorcerer s = new Sorcerer();
            s.setName(map.get("sorcerer[" + idx + "].name"));
            s.setRank(map.get("sorcerer[" + idx + "].rank"));
            sorcerers.add(s);
        }
        mission.setSorcerers(sorcerers);

        // Техники (точно так же, как маги)
        List<Technique> techniques = new ArrayList<>();
        Set<Integer> techIndices = new TreeSet<>();
        for (String key : map.keySet()) {
            if (key.startsWith("technique[")) {
                int idxStart = key.indexOf('[') + 1;
                int idxEnd = key.indexOf(']');
                int idx = Integer.parseInt(key.substring(idxStart, idxEnd)); // каст к инту
                techIndices.add(idx);
            }
        }
        for (Integer idx : techIndices) {
            Technique t = new Technique();
            t.setName(map.get("technique[" + idx + "].name"));
            t.setType(map.get("technique[" + idx + "].type"));
            t.setOwner(map.get("technique[" + idx + "].owner"));
            t.setDamage(Long.parseLong(map.get("technique[" + idx + "].damage"))); // каст к лонгу
            techniques.add(t);
        }
        mission.setTechniques(techniques);

        // Дополнительное поле комментария
        String comment = map.get("comment"); // если записано как comment
        if (comment == null) comment = map.get("note"); // если записано как note
        mission.setComment(comment);

        return mission;
    }
}