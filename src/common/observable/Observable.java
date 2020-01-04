package common.observable;

import common.model.Music;
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
        readyForUpload,
        readyForDownload,
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

    public void Notify(Object ref, NotificationType type, Object... params){
        for (IObserver obs : observers){
            if(params.length == 0){
                switch (type){
                    case loginInvalidCredentials: obs.OnInvalidCredentials(ref); break;
                    case registerPasswordsNotMatching: obs.OnPasswordsNotMatching(ref); break;
                    case registerUsernameNotUnique: obs.OnUsernameNotUnique(ref); break;
                }
            } else {
                switch (type){
                    case loginSuccess:{
                        //params[0] -> (String) token
                        if(params.length == 1 & params[0] instanceof String)
                            obs.OnLoginSuccess(ref, (String) params[0]);
                    } break;
                    case registerSuccess:{
                        //params[0] -> (String) token
                        if(params.length == 1 & params[0] instanceof String)
                            obs.OnRegisterSuccess(ref, (String) params[0]);
                    } break;
                    case readyForUpload:{
                        //params[0] -> (String) hostname
                        //params[1] -> (Integer) port
                        if (params.length == 2 && params[0] instanceof String && params[1] instanceof Integer)
                            obs.OnReadyForUpload(ref, (String) params[0], (Integer) params[1]);
                    } break;
                    case readyForDownload:{
                        //params[0] -> (String) hostname
                        //params[1] -> (Integer) port
                        if (params.length == 2 && params[0] instanceof String && params[1] instanceof Integer)
                            obs.OnReadyForDownload(ref, (String) params[0], (Integer) params[1]);
                    } break;
                    case addSongSuccess:{
                        //params[0] -> (Music) music
                        if(params.length == 1 && params[0] instanceof Music)
                            obs.OnAddSongSuccess(ref, (Music) params[0]);
                    } break;
                    case exception:{
                        //params[0] -> (String) exception simple name
                        if(params.length == 1 & params[0] instanceof String)
                            obs.OnExceptionOccurred(ref, (String) params[0]);
                    } break;
                }
            }
        }
    }

    protected void Notify(NotificationType type, Object... param){
        Notify(null, type, param);
    }

    protected void Notify(NotificationType type){
        Notify((Object) null, type);
    }
}
