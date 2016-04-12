package be.kuleuven.jchr.util.writers;

import java.io.IOException;
import java.io.Writer;

/**
 * This Writer writes everything it receives to "nil", in other
 * words: it does nothing with it! To be conform the specification
 * of java.io.Writer, this stream can be closed (i.e. it cannot be
 * made a singleton...). This could, in a way, be useful for
 * debugging...
 * 
 * @author Peter Van Weert
 */
public class NilWriter extends Writer {
    /**
     * This attribute is needed to be conform the specification
     * of java.io.Writer
     */
    private boolean closed;
    
    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        if (closed) throw new IOException("Stream closed");
    }

    @Override
    public void flush() throws IOException {
        // NOP
    }

    @Override
    public void close() throws IOException {
        closed = true;
    }
    
    @Override
    public Writer append(char c) throws IOException {
        if (closed) throw new IOException("Stream closed");
        return this;
    }

    @Override
    public Writer append(CharSequence csq, int start, int end) throws IOException {
        if (closed) throw new IOException("Stream closed");
        return this;
    }

    @Override
    public Writer append(CharSequence csq) throws IOException {
        if (closed) throw new IOException("Stream closed");
        return this;
    }

    @Override
    public void write(char[] cbuf) throws IOException {
        if (closed) throw new IOException("Stream closed");
    }

    @Override
    public void write(int c) throws IOException {
        if (closed) throw new IOException("Stream closed");
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        if (closed) throw new IOException("Stream closed");
    }

    @Override
    public void write(String str) throws IOException {
        if (closed) throw new IOException("Stream closed");
    }
}
