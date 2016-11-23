package ru.spbau.svidchenko.hw08.task02;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class FirstPartTasks {

    private FirstPartTasks() {}

    // Список названий альбомов
    public static List<String> allNames(Stream<Album> albums) {
        return albums.map(Album::getName).collect(Collectors.toList());
    }

    // Список названий альбомов, отсортированный лексикографически по названию
    public static List<String> allNamesSorted(Stream<Album> albums) {
        return albums.map(Album::getName).sorted().collect(Collectors.toList());
    }

    // Список треков, отсортированный лексикографически по названию, включающий все треки альбомов из 'albums'
    public static List<String> allTracksSorted(Stream<Album> albums) {
        return albums.map(Album::getTracks).reduce(new ArrayList<Track>(), (l1, l2) -> {
            l1.addAll(l2);
            return l1;
        }).stream().map(Track::getName).sorted().collect(Collectors.toList());
    }

    // Список альбомов, в которых есть хотя бы один трек с рейтингом более 95, отсортированный по названию
    public static List<Album> sortedFavorites(Stream<Album> s) {
        return s.filter(album -> album.getTracks().stream().filter(t -> t.getRating() > 95).count() > 0)
                .sorted((a1, a2) -> a1.getName().compareTo(a2.getName()))
                .collect(Collectors.toList());
    }

    // Сгруппировать альбомы по артистам
    public static Map<Artist, List<Album>> groupByArtist(Stream<Album> albums) {
        return albums.collect(Collectors.groupingBy(Album::getArtist));
    }

    // Сгруппировать альбомы по артистам (в качестве значения вместо объекта 'Artist' использовать его имя)
    public static Map<Artist, List<String>> groupByArtistMapName(Stream<Album> albums) {
        return albums.collect(Collectors.groupingBy(Album::getArtist, Collectors.mapping(Album::getName, Collectors.toList())));
    }

    // Число повторяющихся альбомов в потоке
    public static long countAlbumDuplicates(Stream<Album> albums) {
        return albums.collect(Collectors.groupingBy(Function.identity(), Collectors.toList()))
                .entrySet().stream().filter(entry -> entry.getValue().size() > 1).count();
    }

    // Альбом, в котором максимум рейтинга минимален
    // (если в альбоме нет ни одного трека, считать, что максимум рейтинга в нем --- 0)
    public static Optional<Album> minMaxRating(Stream<Album> albums) {
        return albums.min((al1, al2) ->
                {
                    Optional<Track> rate1 = al1.getTracks().stream().max((t1, t2) -> t1.getRating() - t2.getRating()); //max rating
                    Optional<Track> rate2 = al2.getTracks().stream().max((t1, t2) -> t1.getRating() - t2.getRating());
                    Integer r1 = 0;
                    Integer r2 = 0;
                    if (rate1.isPresent()) {
                        r1 = rate1.get().getRating();
                    }
                    if (rate2.isPresent()) {
                        r2 = rate2.get().getRating();
                    }
                    return (r1 - r2);
                }
        );
    }

    // Список альбомов, отсортированный по убыванию среднего рейтинга его треков (0, если треков нет)
    public static List<Album> sortByAverageRating(Stream<Album> albums) {
        return albums.sorted((a1, a2) -> {
            Double rate1 = 0.0;
            Double rate2 = 0.0;
            if (a1.getTracks().size() > 0) {
                rate1 = ((double)a1.getTracks().stream().mapToInt(Track::getRating).reduce(0, Integer::sum)) / a1.getTracks().size();
            }
            if (a2.getTracks().size() > 0) {
                rate2 = ((double)a2.getTracks().stream().mapToInt(Track::getRating).reduce(0, Integer::sum)) / a2.getTracks().size();
            }
            return rate2.compareTo(rate1);
        }).collect(Collectors.toList());
    }

    // Произведение всех чисел потока по модулю 'modulo'
    // (все числа от 0 до 10000)
    public static int moduloProduction(IntStream stream, int modulo) {
        return stream.reduce(1, (i1, i2) -> (i1 * i2) % modulo);
    }

    // Вернуть строку, состояющую из конкатенаций переданного массива, и окруженную строками "<", ">"
    // см. тесты
    public static String joinTo(String... strings) {
        return Arrays.stream(strings).collect(Collectors.joining(", ", "<", ">"));
    }

    // Вернуть поток из объектов класса 'clazz'
    public static <R> Stream<R> filterIsInstance(Stream<?> s, Class<R> clazz) {
        return s.filter(clazz::isInstance).map(clazz::cast);
    }
}