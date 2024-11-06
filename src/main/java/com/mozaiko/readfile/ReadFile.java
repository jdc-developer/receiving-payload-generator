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
import java.util.Arrays;
import java.util.List;

public class ReadFile {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final String USER_DIR = System.getProperty("user.dir");

    private static final String FILES_FOLDER = "filesReceiving";

    private static final String TARGET_FILE_NAME = "receiving-payload.json";

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadFile.class);

    public static void generatePayloadForReceiving() throws FileNotFoundException {
        File sourceFolder = new File(USER_DIR + "/" + FILES_FOLDER);
        LOGGER.info("Iniciando a leitura dos arquivos localizados na pasta: " + sourceFolder.getAbsoluteFile());

        if (!sourceFolder.exists()) throw new FileNotFoundException("Não foi possível encontrar a pasta " + FILES_FOLDER + ". Por favor, tente novamente após criar a pasta.");

        File[] files = sourceFolder.listFiles();
        if (files == null || files.length == 0) throw new FileNotFoundException("A pasta de arquivos de entrada está vazia. Por favor, popule a pasta.");
        List<ReceivingPayload> receivingPayloads = new ArrayList<>();
        Arrays.stream(files).forEach(file -> receivingPayloads.addAll(readFile(file)));

        try {
            LOGGER.info("Escrevendo arquivo...");
            File file = new File(USER_DIR + "/" + TARGET_FILE_NAME);
            MAPPER.writeValue(file, receivingPayloads);
            LOGGER.info("Aplicação encerrada.");
        } catch (IOException ex) {
            LOGGER.error("Ocorreu um erro na criação do arquivo de payload");
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static List<ReceivingPayload> readFile(File file) {
        List<ReceivingPayload> payloadList = new ArrayList<>();
        try {
            String fileContent = getFileContent(file);
            EpcListFile result = MAPPER.readValue(fileContent, EpcListFile.class);
            payloadList = getPayloadList(result);
        } catch (Exception ex) {
            LOGGER.error("Ocorreu um erro na leitura dos arquivos de entrada.");
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }

        return payloadList;
    }

    private static String getFileContent(File file) throws IOException, FileNotFoundException {
        LOGGER.debug("Buscando arquivo de entrada...");
        if (!file.exists()) throw new FileNotFoundException();

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

    private static List<ReceivingPayload> getPayloadList(EpcListFile epcListFile) {
        LOGGER.info("Iniciando criação do arquivo de payload.");
        LOGGER.info("_handling: " + epcListFile.getHandling());
        LOGGER.info("_request: " + epcListFile.getRequest());
        LOGGER.info("qty: " + epcListFile.getQty());

        int startIndex = 0;
        List<ReceivingPayload> payloadList = new ArrayList<>();
        while (startIndex < epcListFile.getEpcs().size()) {
            int controlIndex = 0;
            ReceivingPayload payload = new ReceivingPayload(epcListFile.getHandling(), epcListFile.getRequest(), new ArrayList<>());

            for (int i = startIndex; i < epcListFile.getEpcs().size(); i++) {
                payload.addReading(epcListFile.getEpcs().get(i));
                controlIndex++;
                startIndex++;
                if (controlIndex == epcListFile.getQty()) break;
            }

            payloadList.add(payload);
        }

        LOGGER.info("Payload criado.");
        return payloadList;
    }
}
