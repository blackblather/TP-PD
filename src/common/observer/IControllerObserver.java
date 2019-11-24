package common.observer;

public interface IControllerObserver extends IObserver{
    void OnSuccessfulLogin(Object ref);
    void OnFailedLogin(Object ref);
}
