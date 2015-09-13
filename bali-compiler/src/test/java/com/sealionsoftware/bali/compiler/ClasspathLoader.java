package com.sealionsoftware.bali.compiler;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

public class ClasspathLoader {

    public byte[] resourceAsBytes(URL url) throws Exception{
        if (url  == null){
            return null;
        }
        FileInputStream in = null;
        try {
            File file = new File(url.toURI());
            byte fileContent[] = new byte[(int) file.length()];
            in = new FileInputStream(file);
            in.read(fileContent);
            return fileContent;
        } finally {
            if (in != null) in.close();
        }
    }

}
