/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.storage;

/**
 *
 * @author User
 */
public interface GeneralStorage<T>{
    boolean add(T item);
    T get(String id);
    // Podríamos añadir default methods para getAll y update si son comunes
    // Por ejemplo:
    // default ArrayList<T> getAll() { return new ArrayList<>(); }
    // default boolean update(T item) { return false; }
    // Pero por ahora, los dejamos en las implementaciones si son específicos.
}