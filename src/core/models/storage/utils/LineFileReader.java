/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.storage.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author User
 */
public class LineFileReader {

    public static String readFile(String path) throws IOException { //
        // Utilizar try-with-resources para asegurar el cierre del BufferedReader
        try (BufferedReader br = new BufferedReader(new FileReader(path))) { //
            StringBuilder sb = new StringBuilder(); //
            String linea; //
            while ((linea = br.readLine()) != null) { //
                sb.append(linea); //
            }
            return sb.toString(); //
        }
    }

    // El constructor por defecto no es necesario si solo tienes un método estático
    // public LineFileReader() {
    // }
}