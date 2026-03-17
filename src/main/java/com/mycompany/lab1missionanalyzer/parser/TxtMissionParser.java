import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class TxtMissionParser implements MissionParser {

    @Override
    public Mission parse(File file) throws IOException {
        Map<String, String> map = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                int colonIdx = line.indexOf(':');
                if (colonIdx == -1) continue;
                String key = line.substring(0, colonIdx).trim();
                String value = line.substring(colonIdx + 1).trim();
                map.put(key, value);
            }
        }

        Mission mission = new Mission();

        // Обработка полей миссии (простые)
        mission.setMissionId(map.get("missionId"));
        mission.setDate(LocalDate.parse(map.get("date")));
        mission.setLocation(map.get("location"));
        mission.setOutcome(Outcome.valueOf(map.get("outcome")));
        mission.setDamageCost(Long.parseLong(map.get("damageCost")));

        // Проклятие
        Curse curse = new Curse();
        curse.setName(map.get("curse.name"));
        curse.setThreatLevel(ThreatLevel.valueOf(map.get("curse.threatLevel")));
        mission.setCurse(curse);

        // Маги: ключи вида sorcerer[0].name, sorcerer[0].rank
        List<Sorcerer> sorcerers = new ArrayList<>();
        // Собираем все индексы магов
        Set<Integer> sorcererIndices = new TreeSet<>();
        for (String key : map.keySet()) {
            if (key.startsWith("sorcerer[")) {
                int idxStart = key.indexOf('[') + 1;
                int idxEnd = key.indexOf(']');
                int idx = Integer.parseInt(key.substring(idxStart, idxEnd));
                sorcererIndices.add(idx);
            }
        }
        for (Integer idx : sorcererIndices) {
            Sorcerer s = new Sorcerer();
            s.setName(map.get("sorcerer[" + idx + "].name"));
            s.setRank(Rank.valueOf(map.get("sorcerer[" + idx + "].rank")));
            sorcerers.add(s);
        }
        mission.setSorcerers(sorcerers);

        // Техники
        List<Technique> techniques = new ArrayList<>();
        Set<Integer> techIndices = new TreeSet<>();
        for (String key : map.keySet()) {
            if (key.startsWith("technique[")) {
                int idxStart = key.indexOf('[') + 1;
                int idxEnd = key.indexOf(']');
                int idx = Integer.parseInt(key.substring(idxStart, idxEnd));
                techIndices.add(idx);
            }
        }
        for (Integer idx : techIndices) {
            Technique t = new Technique();
            t.setName(map.get("technique[" + idx + "].name"));
            t.setType(TechniqueType.valueOf(map.get("technique[" + idx + "].type")));
            t.setOwner(map.get("technique[" + idx + "].owner"));
            t.setDamage(Long.parseLong(map.get("technique[" + idx + "].damage")));
            techniques.add(t);
        }
        mission.setTechniques(techniques);

        // Дополнительное поле comment (может называться comment или note)
        String comment = map.get("comment");
        if (comment == null) comment = map.get("note");
        mission.setComment(comment);

        return mission;
    }
}