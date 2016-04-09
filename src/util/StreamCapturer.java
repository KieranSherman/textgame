package util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import util.out.Logger;

public class StreamCapturer extends OutputStream {

	private StringBuilder buffer;
    private String prefix;
    private Logger logger;
    private PrintStream old;

    public StreamCapturer(String prefix, Logger logger, PrintStream old) {
        this.prefix = prefix;
        this.logger = logger;
        this.old = old;
        
        buffer = new StringBuilder(128);
        buffer.append("[").append(prefix).append("] ");
    }

    @Override
    public void write(int b) throws IOException {
        char c = (char) b;
        String value = Character.toString(c);
        buffer.append(value);
        
        if (value.equals("\n")) {
            logger.appendText(buffer.toString());
            buffer.delete(0, buffer.length());
            buffer.append("[").append(prefix).append("] ");
        }
        
        old.print(c);
    }  
    
}    