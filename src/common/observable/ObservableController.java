package common.observable;

import common.controller.IController;
import common.observer.IControllerObserver;

public abstract class ObservableController extends Observable<IControllerObserver> implements IController {
    public enum NotificationType{
        successfulLogin,
        failedLogin
    }

    protected void Notify(Object ref, NotificationType type){
        for (IControllerObserver obs : observers){
            switch (type){
                case successfulLogin: obs.OnSuccessfulLogin(ref); break;
                case failedLogin: obs.OnFailedLogin(ref); break;
            }
        }
    }
}
