import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // Путь к папке с миссиями
        String folderPath = "/Users/olyakulagina/Desktop/учеба/3 курс/Java 2/Лабораторная 1/Данные о миссиях";
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Папка не найдена: " + folderPath);
            return;
        }

        File[] files = folder.listFiles((dir, name) -> 
            name.endsWith(".json") || name.endsWith(".xml") || name.endsWith(".txt"));
        if (files == null || files.length == 0) {
            System.out.println("В папке нет файлов с поддерживаемыми форматами.");
            return;
        }

        for (File file : files) {
            System.out.println("\n========== Обработка файла: " + file.getName() + " ==========");
            try {
                MissionParser parser = ParserFactory.getParser(file);
                Mission mission = parser.parse(file);
                printMission(mission);
            } catch (IOException e) {
                System.out.println("Ошибка при чтении файла: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Ошибка парсинга: " + e.getMessage());
            }
        }
    }

    private static void printMission(Mission mission) {
        System.out.println("ID миссии: " + mission.getMissionId());
        System.out.println("Дата: " + mission.getDate());
        System.out.println("Место: " + mission.getLocation());
        System.out.println("Исход: " + mission.getOutcome());
        System.out.println("Ущерб: " + mission.getDamageCost() + " йен");

        Curse curse = mission.getCurse();
        System.out.println("Проклятие: " + curse.getName() + " (уровень: " + curse.getThreatLevel() + ")");

        System.out.println("Участники:");
        for (Sorcerer s : mission.getSorcerers()) {
            System.out.println("  - " + s.getName() + " (ранг: " + s.getRank() + ")");
        }

        System.out.println("Применённые техники:");
        for (Technique t : mission.getTechniques()) {
            System.out.println("  - " + t.getName() + " (тип: " + t.getType() + 
                               ", владелец: " + t.getOwner() + ", урон: " + t.getDamage() + ")");
        }

        if (mission.getComment() != null && !mission.getComment().isEmpty()) {
            System.out.println("Комментарий: " + mission.getComment());
        }
    }
}