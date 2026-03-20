package com.mycompany.lab1missionanalyzer;

import com.mycompany.lab1missionanalyzer.parser.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите путь к папке с файлами миссий или к конкретному файлу:");
        String inputPath = scanner.nextLine().trim();

        File inputFile = new File(inputPath);

        if (!inputFile.exists()) {
            System.out.println("Указанный путь не существует: " + inputPath);
            return;
        }

        if (inputFile.isDirectory()) {
            // Обрабатываем все файлы в папке
            processDirectory(inputFile);
        } else {
            // Обрабатываем один файл
            processFile(inputFile);
        }
    }

    // Обработка папки
    private static void processDirectory(File folder) {
        File[] files = folder.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".json") ||
                name.toLowerCase().endsWith(".xml") ||
                name.toLowerCase().endsWith(".txt"));

        if (files == null || files.length == 0) {
            System.out.println("В папке нет файлов с поддерживаемыми форматами.");
            return;
        }

        for (File file : files) {
            processFile(file); // Обработка одного файла
        }
    }

    // Обработка одного файла
    private static void processFile(File file) {
        System.out.println("\n========== Обработка файла: " + file.getName() + " ==========");
        try {
            MissionParser parser = ParserFactory.getParser(file);
            Mission mission = parser.parse(file);
            printMission(mission); // вывод информации о миссии
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка парсинга (некорректные данные): " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }

    // Вывод информации о миссии
    private static void printMission(Mission mission) {
        System.out.println("ID миссии: " + mission.getMissionId());
        System.out.println("Дата: " + mission.getDate());
        System.out.println("Место: " + mission.getLocation());
        System.out.println("Исход: " + mission.getOutcome());
        System.out.println("Ущерб: " + mission.getDamageCost());

        Curse curse = mission.getCurse();
        System.out.println("Проклятие: " + curse.getName() + " (уровень: " + curse.getThreatLevel() + ")");

        System.out.println("Участники:");
        for (Sorcerer s : mission.getSorcerers()) {
            System.out.println("  - " + s.getName() + " (ранг: " + s.getRank() + ")");
        }

        System.out.println("Примененные техники:");
        for (Technique t : mission.getTechniques()) {
            System.out.println("  - " + t.getName() + " (тип: " + t.getType() +
                    ", владелец: " + t.getOwner() + ", урон: " + t.getDamage() + ")");
        }

        if (mission.getComment() != null && !mission.getComment().isEmpty()) { // вывод комментария, если он есть
            System.out.println("Комментарий: " + mission.getComment());
        }
    }
}