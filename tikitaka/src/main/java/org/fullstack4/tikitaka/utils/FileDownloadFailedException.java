package org.fullstack4.tikitaka.utils;

// FileDownloadFailedException.java
public class FileDownloadFailedException extends RuntimeException {
    public FileDownloadFailedException() {
        super();
    }

    public FileDownloadFailedException(String message) {
        super(message);
    }

    public FileDownloadFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileDownloadFailedException(Throwable cause) {
        super(cause);
    }
}