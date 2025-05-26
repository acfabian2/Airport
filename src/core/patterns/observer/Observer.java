/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.patterns.observer;

/**
 * Abstract base class for observers in the Observer design pattern.
 * Observers are notified of changes in an observed object.
 *
 * @author User
 */
public abstract class Observer {
    protected Observable observable;

    public Observer() {
        this.observable = null; // Initializing to null is good practice
    }

    /**
     * Called by the Observable when a change occurs.
     * Subclasses must implement this method to react to updates.
     *
     * @param value An integer representing the type or nature of the update.
     * Consider using constants for clarity (e.g., in an enum).
     */
    public abstract void notify(int value);

    /**
     * Sets the Observable object that this Observer is attached to.
     *
     * @param observable The Observable instance.
     */
    public void setObservable(Observable observable) {
        this.observable = observable;
    }
}