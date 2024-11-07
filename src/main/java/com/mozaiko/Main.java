package com.mozaiko;

import com.mozaiko.readfile.ReadFile;
import com.mozaiko.readfile.ReadFileInventory;
import com.mozaiko.readfile.ReadType;
import com.mozaiko.readfile.exception.FileNotFoundException;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        BasicConfigurator.configure();

        ReadType type = ReadType.toEnum(Integer.parseInt(args[0]));
        try {
            switch (type) {
                case INVENTORY:
                    ReadFileInventory.generatePayloadForInventory();
                    break;
                case RECEIVING:
                    ReadFile.generatePayloadForReceiving();
                    break;
            }
        } catch (FileNotFoundException ex) {
            LOGGER.error("Um ou mais arquivos não foram encontrados.");
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }
}