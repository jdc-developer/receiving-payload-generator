package com.mozaiko;

import com.mozaiko.readfile.ReadFile;
import com.mozaiko.readfile.ReadType;
import org.apache.log4j.BasicConfigurator;

public class Main {
    public static void main(String[] args) {
        BasicConfigurator.configure();

        ReadType readType = ReadType.toEnum(Integer.parseInt(args[0]));
        switch (readType) {
            case SALE:
                ReadFile.generatePayloadForReceiving();
                break;
        }
    }
}