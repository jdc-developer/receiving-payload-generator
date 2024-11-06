package com.mozaiko;

import com.mozaiko.readfile.ReadFile;
import com.mozaiko.readfile.exception.FileNotFoundException;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        BasicConfigurator.configure();

        try {
            ReadFile.generatePayloadForReceiving();
        } catch (FileNotFoundException ex) {
            LOGGER.error("Um ou mais arquivos não foram encontrados.");
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }
}