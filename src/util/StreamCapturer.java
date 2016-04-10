package util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import util.out.Logger;

/*
 * Class overrides a stream
 */
public class StreamCapturer extends OutputStream {

	private StringBuilder buffer;		//string builder
    private String prefix;				//prefix
    private Logger logger;				//logger
    private PrintStream old;			//old print stream

    public StreamCapturer(String prefix, Logger logger, PrintStream old) {
        this.prefix = prefix;
        this.logger = logger;
        this.old = old;
        
        buffer = new StringBuilder(128);
        buffer.append("[").append(prefix).append("] ");
    }

    @Override
    /*
     * writes values to the logger
     */
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