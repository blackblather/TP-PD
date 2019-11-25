package common.observable;

import common.observer.IObserver;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable {
    public enum NotificationType{
        successfulLogin,
        failedLogin
    }

    private List<IObserver> observers = new ArrayList<>();

    public synchronized void AddObserver(IObserver o){
        if(observers.contains(o))
            throw new IllegalArgumentException("Observer is already registered.");
        observers.add(o);
    }

    public synchronized void RemoveObserver(IObserver o){
        if(!observers.contains(o))
            throw new IllegalArgumentException("Observer not found.");
        observers.remove(o);
    }

    protected void Notify(Object o){
        for (IObserver obs : observers)
            obs.Update(o);
    }

    protected void Notify(Object ref, NotificationType type){
        for (IObserver obs : observers){
            switch (type){
                case successfulLogin: obs.OnSuccessfulLogin(ref); break;
                case failedLogin: obs.OnFailedLogin(ref); break;
            }
        }
    }

    protected void Notify(NotificationType type){
        Notify(null, type);
    }
}
