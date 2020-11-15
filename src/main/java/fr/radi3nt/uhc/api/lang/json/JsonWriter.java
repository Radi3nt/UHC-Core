// 
// Decompiled by Procyon v0.5.36
// 

package fr.radi3nt.uhc.api.lang.json;

import java.io.IOException;
import java.io.Writer;

class JsonWriter {
    private static final int CONTROL_CHARACTERS_END = 31;
    private static final char[] QUOT_CHARS;
    private static final char[] BS_CHARS;
    private static final char[] LF_CHARS;
    private static final char[] CR_CHARS;
    private static final char[] TAB_CHARS;
    private static final char[] UNICODE_2028_CHARS;
    private static final char[] UNICODE_2029_CHARS;
    private static final char[] HEX_DIGITS;

    static {
        QUOT_CHARS = new char[]{'\\', '\"'};
        BS_CHARS = new char[]{'\\', '\\'};
        LF_CHARS = new char[]{'\\', 'n'};
        CR_CHARS = new char[]{'\\', 'r'};
        TAB_CHARS = new char[]{'\\', 't'};
        UNICODE_2028_CHARS = new char[]{'\\', 'u', '2', '0', '2', '8'};
        UNICODE_2029_CHARS = new char[]{'\\', 'u', '2', '0', '2', '9'};
        HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    }

    protected final Writer writer;

    JsonWriter(final Writer writer) {
        this.writer = writer;
    }

    private static char[] getReplacementChars(final char ch) {
        if (ch > '\\') {
            if (ch < '\u2028' || ch > '\u2029') {
                return null;
            }
            return (ch == '\u2028') ? JsonWriter.UNICODE_2028_CHARS : JsonWriter.UNICODE_2029_CHARS;
        } else {
            if (ch == '\\') {
                return JsonWriter.BS_CHARS;
            }
            if (ch > '\"') {
                return null;
            }
            if (ch == '\"') {
                return JsonWriter.QUOT_CHARS;
            }
            if (ch > '\u001f') {
                return null;
            }
            if (ch == '\n') {
                return JsonWriter.LF_CHARS;
            }
            if (ch == '\r') {
                return JsonWriter.CR_CHARS;
            }
            if (ch == '\t') {
                return JsonWriter.TAB_CHARS;
            }
            return new char[]{'\\', 'u', '0', '0', JsonWriter.HEX_DIGITS[ch >> 4 & 0xF], JsonWriter.HEX_DIGITS[ch & '\u000f']};
        }
    }

    protected void writeLiteral(final String value) throws IOException {
        this.writer.write(value);
    }

    protected void writeNumber(final String string) throws IOException {
        this.writer.write(string);
    }

    protected void writeString(final String string) throws IOException {
        this.writer.write(34);
        this.writeJsonString(string);
        this.writer.write(34);
    }

    protected void writeArrayOpen() throws IOException {
        this.writer.write(91);
    }

    protected void writeArrayClose() throws IOException {
        this.writer.write(93);
    }

    protected void writeArraySeparator() throws IOException {
        this.writer.write(44);
    }

    protected void writeObjectOpen() throws IOException {
        this.writer.write(123);
    }

    protected void writeObjectClose() throws IOException {
        this.writer.write(125);
    }

    protected void writeMemberName(final String name) throws IOException {
        this.writer.write(34);
        this.writeJsonString(name);
        this.writer.write(34);
    }

    protected void writeMemberSeparator() throws IOException {
        this.writer.write(58);
    }

    protected void writeObjectSeparator() throws IOException {
        this.writer.write(44);
    }

    protected void writeJsonString(final String string) throws IOException {
        final int length = string.length();
        int start = 0;
        for (int index = 0; index < length; ++index) {
            final char[] replacement = getReplacementChars(string.charAt(index));
            if (replacement != null) {
                this.writer.write(string, start, index - start);
                this.writer.write(replacement);
                start = index + 1;
            }
        }
        this.writer.write(string, start, length - start);
    }
}
