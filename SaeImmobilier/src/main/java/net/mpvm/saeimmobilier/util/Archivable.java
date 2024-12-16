package net.mpvm.saeimmobilier.util;

import java.io.IOException;

public interface Archivable {

    boolean archiver();

    class ArchivableException extends IOException{
        public ArchivableException(String message) {
            super(message);
        }
        public ArchivableException(String message, Throwable cause) {
            super(message,cause);
        }
    }
}
