package common.observer;

public interface IObserver {
    void Update(Object o);
    void OnSuccessfulLogin(Object ref);
    void OnInvalidCredentials(Object ref);
}
