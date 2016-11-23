package ru.spbau.svidchenko.hw08.task02;

public class Track {

    private final String name;
    private final int rating;

    public Track(String name, int rating) {
        this.name = name;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public int getRating() {
        return rating;
    }
}