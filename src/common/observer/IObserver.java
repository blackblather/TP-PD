package common.observer;

public interface IObserver {
    void Update(Object o);
    void OnSuccessfulLogin(Object ref);
    void OnInvalidCredentials(Object ref);
    void OnRegisterSuccess(Object ref);
    void OnRegisterError(Object ref);
    void OnExceptionOccurred(Object ref, Integer errorCode, String message);
}
