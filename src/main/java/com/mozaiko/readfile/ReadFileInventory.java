package com.mozaiko.readfile;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mozaiko.readfile.dto.EpcListFileInventory;
import com.mozaiko.readfile.dto.InventoryPayload;
import com.mozaiko.readfile.exception.FileNotFoundException;
import com.mozaiko.readfile.exception.OutOfBoundsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadFileInventory {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final String USER_DIR = System.getProperty("user.dir");

    private static final String FILES_FOLDER = "filesInventory";

    private static final String TARGET_FILE_NAME = "inventory-payload.json";

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadFileInventory.class);

    public static void generatePayloadForInventory() throws FileNotFoundException {
        File sourceFolder = new File(USER_DIR + "/" + FILES_FOLDER);
        LOGGER.info("Iniciando a leitura dos arquivos localizados na pasta: " + sourceFolder.getAbsoluteFile());

        if (!sourceFolder.exists()) throw new FileNotFoundException("Não foi possível encontrar a pasta " + FILES_FOLDER + ". Por favor, tente novamente após criar a pasta.");

        File[] files = sourceFolder.listFiles();
        if (files == null || files.length == 0) throw new FileNotFoundException("A pasta de arquivos de entrada está vazia. Por favor, popule a pasta.");
        List<InventoryPayload> inventoryPayloads = new ArrayList<>();
        Arrays.stream(files).forEach(file -> inventoryPayloads.addAll(readFile(file)));

        try {
            LOGGER.info("Escrevendo arquivo...");
            File file = new File(USER_DIR + "/" + TARGET_FILE_NAME);
            MAPPER.writeValue(file, inventoryPayloads);
            LOGGER.info("Aplicação encerrada.");
        } catch (IOException ex) {
            LOGGER.error("Ocorreu um erro na criação do arquivo de payload");
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static List<InventoryPayload> readFile(File file) {
        List<InventoryPayload> payloadList = new ArrayList<>();
        try {
            String fileContent = getFileContent(file);
            EpcListFileInventory result = MAPPER.readValue(fileContent, EpcListFileInventory.class);
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

    private static List<InventoryPayload> getPayloadList(EpcListFileInventory epcListFileInventory) throws OutOfBoundsException {
        LOGGER.info("Iniciando criação do arquivo de payload.");
        LOGGER.info("_inventory: " + epcListFileInventory.getInventory());
        LOGGER.info("_local: " + epcListFileInventory.getLocal());
        LOGGER.info("qty: " + epcListFileInventory.getQty());

        int startIndex = 0;
        List<InventoryPayload> payloadList = new ArrayList<>();
        while (startIndex < epcListFileInventory.getEpcs().size()) {
            int controlIndex = 0;
            InventoryPayload payload = new InventoryPayload(epcListFileInventory.getInventory(),
                    epcListFileInventory.getLocal(), new ArrayList<>());

            if (epcListFileInventory.getQty() > epcListFileInventory.getEpcs().size()) throw new OutOfBoundsException();
            for (int i = startIndex; i < epcListFileInventory.getEpcs().size(); i++) {
                payload.addEpc(epcListFileInventory.getEpcs().get(i));
                controlIndex++;
                startIndex++;
                if (controlIndex == epcListFileInventory.getQty()) break;
            }

            payloadList.add(payload);
        }

        LOGGER.info("Payload criado.");
        return payloadList;
    }
}
