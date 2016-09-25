package com.sealionsoftware.bali.compiler;

import bali.Character;
import bali.Text;
import bali.Writer;

import java.util.ArrayList;
import java.util.List;

import static bali.Character.NEW_LINE;
import static bali.logic.Primitive.convert;
import static bali.text.Primitive.convert;

public class ListTextBufferWriter implements Writer, TextBuffer {

    private List<Text> written = new ArrayList<>();
    private StringBuffer lineBuffer = new StringBuffer();

    public void write(Character in) {
        if (convert(in.equalTo(NEW_LINE))){
            written.add(drainLineBuffer());
        } else {
            lineBuffer.append(convert(in));
        }
    }

    public void writeLine(Text in) {
        Text line = in;
        if (lineBuffer.length() > 0){
            line = line.concatenate(drainLineBuffer());
        }
        written.add(line);
    }

    private Text drainLineBuffer(){
        Text ret = convert(lineBuffer.toString());
        lineBuffer = new StringBuffer();
        return ret;
    }

    public List<Text> getWrittenLines(){
        List<Text> ret = new ArrayList<>(written);
        if (lineBuffer.length() > 0){
            ret.add(convert(lineBuffer.toString()));
        }
        return ret;
    }

    public String toString() {
        return getWrittenLines().toString();
    }
}
