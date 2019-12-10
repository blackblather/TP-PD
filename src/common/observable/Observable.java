package common.observable;

import common.observer.IObserver;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable {
    public enum NotificationType{
        successfulLogin,
        invalidCredentials,
        registerSuccess,
        registerError,
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
                case successfulLogin: obs.OnSuccessfulLogin(ref); break;
                case invalidCredentials: obs.OnInvalidCredentials(ref); break;
                case registerSuccess: obs.OnRegisterSuccess(ref); break;
                case registerError: obs.OnRegisterError(ref); break;
            }
        }
    }

    protected void Notify(Object ref, NotificationType type, Object... param){
        for (IObserver obs : observers){
            switch (type){
                case exception:{
                    //param[0] (Integer) -> Error code
                    //param[1] (String)-> Message
                    if(param.length == 2 && param[0] instanceof Integer && param[1] instanceof String)
                        obs.OnExceptionOccurred(ref, (Integer) param[0], (String) param[1]);
                } break;
            }
        }
    }

    protected void Notify(NotificationType type){
        Notify(null, type);
    }

    protected void Notify(NotificationType type, Object... param){
        Notify(null, type, param);
    }
}
