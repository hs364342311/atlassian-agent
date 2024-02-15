package io.zhile.crack.atlassian;

import java.io.*;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

public class DefaultPropertiesPersister {

    public DefaultPropertiesPersister() {
    }

    public void load(Properties props, InputStream is) throws IOException {
        props.load(is);
    }

    public void load(Properties props, Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);

        while(true) {
            String line;
            char firstChar;
            do {
                do {
                    do {
                        line = in.readLine();
                        if (line == null) {
                            return;
                        }

                        line = StringUtils.trimLeadingWhitespace(line);
                    } while(line.length() <= 0);

                    firstChar = line.charAt(0);
                } while(firstChar == '#');
            } while(firstChar == '!');

            while(this.endsWithContinuationMarker(line)) {
                String nextLine = in.readLine();
                line = line.substring(0, line.length() - 1);
                if (nextLine != null) {
                    line = line + StringUtils.trimLeadingWhitespace(nextLine);
                }
            }

            int separatorIndex = line.indexOf("=");
            if (separatorIndex == -1) {
                separatorIndex = line.indexOf(":");
            }

            String key = separatorIndex != -1 ? line.substring(0, separatorIndex) : line;
            String value = separatorIndex != -1 ? line.substring(separatorIndex + 1) : "";
            key = StringUtils.trimTrailingWhitespace(key);
            value = StringUtils.trimLeadingWhitespace(value);
            props.put(this.unescape(key), this.unescape(value));
        }
    }

    protected boolean endsWithContinuationMarker(String line) {
        boolean evenSlashCount = true;

        for(int index = line.length() - 1; index >= 0 && line.charAt(index) == '\\'; --index) {
            evenSlashCount = !evenSlashCount;
        }

        return !evenSlashCount;
    }

    protected String unescape(String str) {
        StringBuffer outBuffer = new StringBuffer(str.length());

        char c;
        for(int index = 0; index < str.length(); outBuffer.append(c)) {
            c = str.charAt(index++);
            if (c == '\\') {
                c = str.charAt(index++);
                if (c == 't') {
                    c = '\t';
                } else if (c == 'r') {
                    c = '\r';
                } else if (c == 'n') {
                    c = '\n';
                } else if (c == 'f') {
                    c = '\f';
                }
            }
        }

        return outBuffer.toString();
    }

    public void store(Properties props, OutputStream os, String header) throws IOException {
        props.store(os, header);
    }

    public void store(Properties props, Writer writer, String header) throws IOException {
        this.store(props, writer, header, false);
    }

    public void store(Properties props, Writer writer, String header, boolean omitDate) throws IOException {
        BufferedWriter out = new BufferedWriter(writer);
        if (header != null) {
            out.write("#" + header);
            out.write(10);
        }

        if (!omitDate) {
            out.write("#" + new Date());
            out.write(10);
        }

        Enumeration keys = props.keys();

        while(keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            String val = props.getProperty(key);
            out.write(this.escape(key, true) + "=" + this.escape(val, false));
            out.write(10);
        }

        out.flush();
    }

    protected String escape(String str, boolean isKey) {
        int len = str.length();
        StringBuffer outBuffer = new StringBuffer(len * 2);

        for(int index = 0; index < len; ++index) {
            char c = str.charAt(index);
            switch(c) {
                case '\t':
                    outBuffer.append("\\t");
                    break;
                case '\n':
                    outBuffer.append("\\n");
                    break;
                case '\f':
                    outBuffer.append("\\f");
                    break;
                case '\r':
                    outBuffer.append("\\r");
                    break;
                case ' ':
                    if (index == 0 || isKey) {
                        outBuffer.append('\\');
                    }

                    outBuffer.append(' ');
                    break;
                case '\\':
                    outBuffer.append("\\\\");
                    break;
                default:
                    if ("=: \t\r\n\f#!".indexOf(c) != -1) {
                        outBuffer.append('\\');
                    }

                    outBuffer.append(c);
            }
        }

        return outBuffer.toString();
    }
}
