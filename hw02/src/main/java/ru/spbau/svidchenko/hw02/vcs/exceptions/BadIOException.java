package ru.spbau.svidchenko.hw02.vcs.exceptions;

import java.io.IOException;

/**
 * This exception shows, that something wrong with IO. See cause for more information
 */
public class BadIOException extends VCSException {
    public BadIOException(IOException cause) {
        initCause(cause);
    }
}
