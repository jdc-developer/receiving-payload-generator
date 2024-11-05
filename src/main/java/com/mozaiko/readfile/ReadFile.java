package com.mozaiko.readfile;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mozaiko.readfile.dto.ReceivingPayload;
import com.mozaiko.readfile.dto.SkuListFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile {

    private static final String USER_DIR = System.getProperty("user.dir");

    private static final String FILE_NAME = "sales-skus.json";

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadFile.class);

    public static void generatePayloadForReceiving() {
        LOGGER.info("Starting reading");
        File file = new File(USER_DIR + "/" + FILE_NAME);
        if (file.exists()) {

            try {
                BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()));
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                String fileContent = sb.toString();
                ObjectMapper mapper = new ObjectMapper();

                SkuListFile result = mapper.readValue(fileContent, SkuListFile.class);

                ReceivingPayload payload = new ReceivingPayload();
                result.getSkus().forEach(sku -> System.out.println(sku));

                br.close();
            } catch (IOException e) {

            }
        }
    }
}
