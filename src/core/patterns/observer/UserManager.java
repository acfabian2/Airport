/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.patterns.observer;

import core.models.Passenger;

/**
 * Manages the current active user in the application.
 * Implements the Singleton pattern to ensure only one instance exists.
 * Extends Observable to notify observers when the current user changes.
 * @author User
 */
public class UserManager extends Observable {

    private static UserManager instance;
    private Passenger currentUser;

    /**
     * Constant representing the notification value when the current user changes.
     */
    public static final int USER_CHANGED = 3; //

    /**
     * Private constructor to enforce the Singleton pattern.
     * Initializes currentUser to null.
     */
    private UserManager() {
        this.currentUser = null;
    }

    /**
     * Returns the singleton instance of UserManager.
     * Creates the instance if it doesn't already exist (lazy initialization).
     * @return The single instance of UserManager.
     */
    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    /**
     * Sets the current user and notifies all registered observers.
     * @param user The Passenger object representing the new current user.
     */
    public void setCurrentUser(Passenger user) {
        this.currentUser = user;
        // Notifies observers about the user change
        notifyAll(USER_CHANGED);
    }

    /**
     * Returns the current active user.
     * @return The Passenger object representing the current user, or null if no user is set.
     */
    public Passenger getCurrentUser() {
        return currentUser;
    }

    // Note: The addObserver method is inherited from Observable.
    // The boolean return in Observable's addObserver could be removed for cleaner code,
    // as its success is implied if no exception is thrown.
}