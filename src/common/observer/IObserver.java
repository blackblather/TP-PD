package common.observer;

public interface IObserver {
    void Update(Object o);
    void OnLoginSuccess(Object ref);
    void OnInvalidCredentials(Object ref);
    void OnRegisterSuccess(Object ref);
    void OnPasswordsNotMatching(Object ref);
    void OnUsernameNotUnique(Object ref);
    void OnExceptionOccurred(Object ref);
}
