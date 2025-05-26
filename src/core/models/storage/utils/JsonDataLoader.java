/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.storage.utils;

import java.io.IOException; // Importar IOException

/**
 *
 * @author User
 */
public interface JsonDataLoader<T> {
    // Propagar la excepción IOException si la lectura del archivo falla
    // También podríamos añadir un método para indicar que el jsonStr podría venir de un archivo.
    void loadFromFile(String jsonStr) throws IOException; //
}