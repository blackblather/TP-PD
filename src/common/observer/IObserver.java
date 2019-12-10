package common.observer;

public interface IObserver {
    void Update(Object o);
    void OnLoginSuccess(Object ref);
    void OnLoginError(Object ref);
    void OnRegisterSuccess(Object ref);
    void OnRegisterError(Object ref);
    void OnExceptionOccurred(Object ref, Integer errorCode, String message);
}
