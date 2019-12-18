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
        addSongSuccess,
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

    protected void Notify(Object ref, NotificationType type, String param){
        for (IObserver obs : observers){
            if(param == null){
                switch (type){
                    case loginInvalidCredentials: obs.OnInvalidCredentials(ref); break;
                    case registerPasswordsNotMatching: obs.OnPasswordsNotMatching(ref); break;
                    case registerUsernameNotUnique: obs.OnUsernameNotUnique(ref); break;
                    case addSongSuccess: obs.OnAddSongSuccess(ref); break;
                }
            } else {
                switch (type){
                    case loginSuccess: obs.OnLoginSuccess(ref, param); break;
                    case registerSuccess: obs.OnRegisterSuccess(ref, param); break;
                    case exception: obs.OnExceptionOccurred(ref, param); break;
                }
            }
        }
    }

    protected void Notify(Object ref, NotificationType type){
        Notify(ref, type, null);
    }

    protected void Notify(NotificationType type, String param){
        Notify(null, type, param);
    }

    protected void Notify(NotificationType type){
        Notify(null, type, null);
    }
}
