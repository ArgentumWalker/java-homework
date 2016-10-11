package ru.spbau.svidchenko.hw04.task01;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Read Numbers and write their sqrs. If read not-number, write "null"
 */
public class NumSquaresWriter {
    private static final String STRING_IF_NOTHING = "null";
    private ArrayList<Maybe<Double>> elements;

    /** Read numbers from stream until stream ends */
    public NumSquaresWriter(InputStream in) {
        elements = new ArrayList<Maybe<Double>>();
        Maybe<Double> maybe;
        Scanner input = new Scanner(in);
        while(input.hasNextLine()) {
            String s = input.nextLine();
            try {
                Double num = Double.parseDouble(s);
                num *= num;
                maybe = Maybe.just(num);
            } catch (Exception e) {
                maybe = Maybe.nothing();
            }
            elements.add(maybe);
        }
    }

    /** Write contained numbers to stream */
    public void write(OutputStream out) throws IOException {
        DataOutputStream output = new DataOutputStream(out);
        for (Maybe<Double> maybe : elements) {
            try {
                output.writeChars(maybe.get().toString() + "\n");
            }
            catch (MaybeNothingException e) {
                output.writeChars(STRING_IF_NOTHING + "\n");
            }
        }
    }

    /** Read and write numbers without creating object */
    public static void readAndWrite(InputStream in, OutputStream out) throws IOException {
        Maybe<Double> maybe;
        Scanner input = new Scanner(in);
        DataOutputStream output = new DataOutputStream(out);
        while(input.hasNextLine()) {
            String s = input.nextLine();
            try {
                Double num = Double.parseDouble(s);
                num *= num;
                maybe = Maybe.just(num);
            }
            catch (Exception e) {
                maybe = Maybe.nothing();
            }
            try {
                output.writeChars(maybe.get().toString() + "\n");
            }
            catch (MaybeNothingException e) {
                output.writeChars(STRING_IF_NOTHING + "\n");
            }
        }
    }
}
