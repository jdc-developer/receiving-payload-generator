package com.mozaiko.readfile;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class ReadFile {

    private static final String USER_DIR = System.getProperty("user.dir");

    private static final String FILE_NAME = "sales-skus.txt";

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadFile.class);

    public void getSkusFromFile() throws IOException {
        LOGGER.info("Starting reading");
        File file = new File(USER_DIR + "/" + FILE_NAME);
        if (file.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                String everything = sb.toString();
                System.out.println(everything);
            } finally {
                br.close();
            }
        }
    }
}
