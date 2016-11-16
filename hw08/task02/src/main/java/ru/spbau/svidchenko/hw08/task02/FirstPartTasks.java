package ru.spbau.svidchenko.hw08.task02;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
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
        return albums.map(Album::getTracks).collect(
                new Collector<List<Track>, Stream<String>, Stream<String>>() {
                    @Override
                    public Supplier<Stream<String>> supplier() {
                        return new List<String>.stream();
                    }

                    @Override
                    public BiConsumer<Stream<String>, List<Track>> accumulator() {
                        return (str, list -> Stream.concat(str, list.stream().map(Track::getName)));
                    }

                    @Override
                    public BinaryOperator<Stream<String>> combiner() {
                        return Stream::concat;
                    }

                    @Override
                    public Function<Stream<String>, Stream<String>> finisher() {
                        return Function.identity();
                    }
                    @Override
                    public Set<Characteristics> characteristics() {
                        return null;
                    }
                }
        ).collect(Collectors.toList());
    }

    // Список альбомов, в которых есть хотя бы один трек с рейтингом более 95, отсортированный по названию
    public static List<Album> sortedFavorites(Stream<Album> s) {
        return s.filter(album -> album.getTracks().stream().map(Track::getRating).max(Integer::compare).isPresent() &&
                album.getTracks().stream().map(Track::getRating).max(Integer::compare).get() > 95).collect(Collectors.toList());
    }

    // Сгруппировать альбомы по артистам
    public static Map<Artist, List<Album>> groupByArtist(Stream<Album> albums) {
        return albums.collect(new Collector<Album, Map<Artist, List<Album>>, Map<Artist, List<Album>>>() {

            @Override
            public Supplier<Map<Artist, List<Album>>> supplier() {
                return HashMap<Artist, List<Album>>::new;
            }

            @Override
            public BiConsumer<Map<Artist, List<Album>>, Album> accumulator() {
                return new BiConsumer<Map<Artist, List<Album>>, Album>() {
                    @Override
                    public void accept(Map<Artist, List<Album>> artistListMap, Album album) {
                        artistListMap.merge(album.getArtist(),
                                () -> {
                                    List<> list = new LinkedList<Album>;
                                    list.add(album);
                                    return list;
                                },
                                (l1, l2) -> {
                                    l1.addAll(l2);
                                    return l1;
                                });
                    }
                };
            }

            @Override
            public BinaryOperator<Map<Artist, List<Album>>> combiner() {
                return null;
            }

            @Override
            public Function<Map<Artist, List<Album>>, Map<Artist, List<Album>>> finisher() {
                return Function.identity();
            }

            @Override
            public Set<Characteristics> characteristics() {
                return null;
            }
        });
    }

    // Сгруппировать альбомы по артистам (в качестве значения вместо объекта 'Artist' использовать его имя)
    public static Map<Artist, List<String>> groupByArtistMapName(Stream<Album> albums) {
        throw new UnsupportedOperationException();
    }

    // Число повторяющихся альбомов в потоке
    public static long countAlbumDuplicates(Stream<Album> albums) {
        throw new UnsupportedOperationException();
    }

    // Альбом, в котором максимум рейтинга минимален
    // (если в альбоме нет ни одного трека, считать, что максимум рейтинга в нем --- 0)
    public static Optional<Album> minMaxRating(Stream<Album> albums) {
        throw new UnsupportedOperationException();
    }

    // Список альбомов, отсортированный по убыванию среднего рейтинга его треков (0, если треков нет)
    public static List<Album> sortByAverageRating(Stream<Album> albums) {
        throw new UnsupportedOperationException();
    }

    // Произведение всех чисел потока по модулю 'modulo'
    // (все числа от 0 до 10000)
    public static int moduloProduction(IntStream stream, int modulo) {
        throw new UnsupportedOperationException();
    }

    // Вернуть строку, состояющую из конкатенаций переданного массива, и окруженную строками "<", ">"
    // см. тесты
    public static String joinTo(String... strings) {
        throw new UnsupportedOperationException();
    }

    // Вернуть поток из объектов класса 'clazz'
    public static <R> Stream<R> filterIsInstance(Stream<?> s, Class<R> clazz) {
        throw new UnsupportedOperationException();
    }
}