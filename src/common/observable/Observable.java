package common.observable;

import common.model.Response;
import common.observer.IObserver;

import java.util.ArrayList;
import java.util.List;

public class Observable {
    private List<IObserver> observers = new ArrayList<>();

    protected void Notify(Response response){
        if(response != null)
            for (IObserver observer : observers)
                observer.Update(response);
    }

    public synchronized void AddObserver(IObserver obs){
        if(observers.contains(obs))
            throw new IllegalArgumentException("Observer is already registered.");
        observers.add(obs);
    }
}
