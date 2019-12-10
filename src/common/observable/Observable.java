package common.observable;

import common.observer.IObserver;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable {
    public enum NotificationType{
        loginSuccess,
        loginInvalidCredentials,
        registerSuccess,
        registerPasswordsNotMatching,
        registerUsernameNotUnique,
        exception
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
                case loginSuccess: obs.OnLoginSuccess(ref); break;
                case loginInvalidCredentials: obs.OnInvalidCredentials(ref); break;
                case registerSuccess: obs.OnRegisterSuccess(ref); break;
                case registerPasswordsNotMatching: obs.OnPasswordsNotMatching(ref); break;
                case registerUsernameNotUnique: obs.OnUsernameNotUnique(ref); break;
                case exception: obs.OnExceptionOccurred(ref); break;
            }
        }
    }

    protected void Notify(NotificationType type){
        Notify(null, type);
    }
}
