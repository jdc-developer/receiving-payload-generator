package com.mozaiko;

import com.mozaiko.readfile.ReadFile;
import org.apache.log4j.BasicConfigurator;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        BasicConfigurator.configure();
        ReadFile readFile = new ReadFile();

        try {
            readFile.getSkusFromFile();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Erro");
        }
    }
}