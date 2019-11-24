package common.observable;

import common.observer.IObserver;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable<T extends IObserver> {
    List<T> observers = new ArrayList<>();

    protected void Notify(Object o){
        for (T obs : observers)
            obs.Update(o);
    }

    public synchronized void AddObserver(T o){
        if(observers.contains(o))
            throw new IllegalArgumentException("Observer is already registered.");
        observers.add(o);
    }

    public synchronized void RemoveObserver(T o){
        if(!observers.contains(o))
            throw new IllegalArgumentException("Observer not found.");
        observers.remove(o);
    }
}
