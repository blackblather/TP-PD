package common.observer;

public interface IObserver {
    void Update(Object o);
    void OnSuccessfulLogin(Object ref);
    void OnFailedLogin(Object ref);
}
