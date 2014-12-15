/*
 * 
 */
package com.idot.twitter;

import java.io.EOFException;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Set;

/**
 * @author 
 */
public abstract class DelimitedTwitterStreamProcessor
        implements TwitterStreamProcessor {

    @Override
    public void processTwitterStream(InputStream is, String credentials,
            Set<String> ids)
            throws InterruptedException, IOException {
        DataInputStream in = new DataInputStream(is);
        while (true) {
            byte[] bl = new byte[10];
            if (readLine(in, bl, 0, bl.length) == -1) {
                throw new EOFException();
            }
            String lengthBytes = new String(bl).trim();
            if (lengthBytes.length() > 0) {
                byte[] bytes = new byte[Integer.parseInt(lengthBytes)];
                in.readFully(bytes);
                processTwitterUpdate(bytes, credentials, ids);
            }
        }
    }

    @Override
    public boolean consumesDelimitedStream() {
        return true;
    }

    /**
     * Processes a twitter update.  This method should return fairly
     * quickly.
     *
     * @param bytes the bytes to process
     * @param credentials the credentials used to create the stream,
     *   for logging purposes
     * @param ids the twitter ids this stream is following
     */
    protected abstract void processTwitterUpdate(byte[] bytes,
            String credentials,
            Set<String> ids)
            throws InterruptedException, IOException;

    private int readLine(InputStream in, byte[] b, int off, int len)
            throws IOException {
        if (len <= 0) {
            return 0;
        }
        int count = 0, c;
        while ((c = in.read()) != -1) {
            b[off++] = (byte) c;
            count++;
            if (c == '\n' || count == len) {
                break;
            }
        }
        return count > 0 ? count : -1;
    }
}