package core.patterns.observer;

import java.util.ArrayList;

public abstract class Observable {
    protected ArrayList<Observer> observers; // Consider using generics like ArrayList<Observer<T>>

    public Observable() {
        this.observers = new ArrayList<>();
    }
    
    public void addObserver(Observer observer) { // Removed boolean return
        this.observers.add(observer);
        observer.setObservable(this); // Assuming setObservable is a method in Observer
    }
    
    protected void notifyAll(int value) {
        for (Observer observer : this.observers) {
            observer.notify(value);
        }
    }
}