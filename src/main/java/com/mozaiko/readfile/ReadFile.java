package com.mozaiko.readfile;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mozaiko.readfile.dto.EpcListFile;
import com.mozaiko.readfile.dto.ReceivingPayload;
import com.mozaiko.readfile.exception.FileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {

    private static ObjectMapper mapper = new ObjectMapper();

    private static final String USER_DIR = System.getProperty("user.dir");

    //private static final String FILE_NAME = "receivement-epcs.json";

    //private static final String TARGET_FILE_NAME = "receivement-payload.json";

    private static final String JSON_EXTENSION = ".json";

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadFile.class);

    public static void generatePayloadForReceiving(String handling, String request, Integer qty, String sourceFileName) {
        File file = new File(USER_DIR + "/" + sourceFileName);
        LOGGER.info("Starting to read file: " + file.getAbsoluteFile());

        try {
            String fileContent = getFileContent(file);
            EpcListFile result = mapper.readValue(fileContent, EpcListFile.class);
            List<ReceivingPayload> payloadList = getPayloadList(result, handling, request, qty);

            LOGGER.info("Escrevendo arquivo...");
            String targetFileName = sourceFileName.replace(JSON_EXTENSION, "") + "-payload" + JSON_EXTENSION;
            mapper.writeValue(new File(USER_DIR + "/" + targetFileName), payloadList);

            LOGGER.info("Aplicação encerrada.");
        } catch (Exception ex) {
            LOGGER.error("Ocorreu um erro na leitura e criação do arquivo de payload");
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }

    }

    private static String getFileContent(File file) throws IOException, FileNotFoundException {
        LOGGER.debug("Buscando arquivo de entrada...");
        if (file.exists()) {
            LOGGER.debug("Arquivo encontrado, iniciando leitura");

            BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            br.close();

            LOGGER.debug("Leitura finalizada.");
            return sb.toString();
        }
        throw new FileNotFoundException();
    }

    private static List<ReceivingPayload> getPayloadList(EpcListFile result, String handling,
                                               String request, Integer qty) throws IOException {
        LOGGER.info("Iniciando criação do arquivo de payload.");
        LOGGER.info("_handling: " + handling);
        LOGGER.info("_request: " + request);
        LOGGER.info("qty: " + qty);

        int startIndex = 0;
        List<ReceivingPayload> payloadList = new ArrayList<>();
        while (startIndex < result.getEpcs().size()) {
            int controlIndex = 0;
            ReceivingPayload payload = new ReceivingPayload(handling, request, new ArrayList<>());

            for (int i = startIndex; i < result.getEpcs().size(); i++) {
                payload.addReading(result.getEpcs().get(i));
                controlIndex++;
                startIndex++;
                if (controlIndex == qty) break;
            }

            payloadList.add(payload);
        }

        LOGGER.info("Payload criado.");
        return payloadList;
    }
}
