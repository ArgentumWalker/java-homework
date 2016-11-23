package ru.spbau.svidchenko.hw08.task02;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Random;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class SecondPartTasks {

    private SecondPartTasks() {}

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) {
        return paths.stream().filter((s) -> s.contains(sequence)).collect(Collectors.toList());
    }

    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать, какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        Random r = new Random();
        long count = 1000000;
        return ((double)Stream.generate(() -> Math.hypot(r.nextDouble(), r.nextDouble())).limit(count).filter(d -> d <= 1.0).count()) / count;
    }

    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.

    public static String findPrinter(Map<String, List<String>> compositions) {
        class Pair {
            String fst, snd;
            Pair(String x, String y) {fst = x; snd = y;}
        }
        return compositions.entrySet().stream().map((entry) ->
                new Pair(entry.getKey(),
                        entry.getValue().stream().reduce("", String::concat)))
                .max((p1, p2) -> p1.snd.length() - p2.snd.length()).get().fst;
    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        return orders.stream().reduce(new HashMap<>(), (m1, m2) -> {
            for (String s : m2.keySet()) {
                m1.merge(s, m2.get(s), Integer::sum);
            }
            return m1;
        });
    }
}
