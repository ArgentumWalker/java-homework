package ru.spbau.svidchenko.hw08.task02;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() {
        assertTrue(
                SecondPartTasks.findQuotes(Stream.of("lolololol", "kekekek", "lolkekelol").collect(Collectors.toList()),
                        "lol").equals(Stream.of("lolololol", "lolkekelol").collect(Collectors.toList()))
        );
    }

    @Test
    public void testPiDividedBy4() {
        Double pi = SecondPartTasks.piDividedBy4()*4;
        System.out.println(pi);
        System.out.println(Math.PI);
        assertTrue(
            Math.abs(pi - Math.PI) < 0.001
        );
    }

    @Test
    public void testFindPrinter() {
        HashMap<String, List<String>> map = new HashMap<>();
        map.put("Starswirl", Stream.of("BookAboutStars", "BookAboutMagic").collect(Collectors.toList()));
        map.put("Luna", Stream.of("BookAboutStars", "BookAboutMagic").collect(Collectors.toList()));
        map.put("Sparkle", Stream.of("BookAboutMagic").collect(Collectors.toList()));
        map.put("Pie", new ArrayList<>());
        assertTrue(
                SecondPartTasks.findPrinter(map).equals("Starswirl") ||
                        SecondPartTasks.findPrinter(map).equals("Luna")
        );
    }

    @Test
    public void testCalculateGlobalOrder() {
        HashMap<String, Integer> orderSweetAppleAcers = new HashMap<>();
        HashMap<String, Integer> orderSugarCorner = new HashMap<>();
        orderSweetAppleAcers.put("Sugar", 100);
        orderSugarCorner.put("Sugar", 200);
        orderSweetAppleAcers.put("Wood", 21);
        orderSugarCorner.put("Hugs'n'Smiles", 9000);
        Map<String, Integer> summary = SecondPartTasks.calculateGlobalOrder(
                Stream.of(orderSugarCorner, orderSweetAppleAcers).collect(Collectors.toList()));
        assertTrue(summary.get("Changelings") == null);
        assertTrue(summary.get("Sugar") == 300);
        assertTrue(summary.get("Wood") == 21);
        assertTrue(summary.get("Hugs'n'Smiles") == 9000);
    }
}