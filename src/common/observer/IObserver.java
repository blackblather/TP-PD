package common.observer;

public interface IObserver {
    void Update(Object o);
    void OnLoginSuccess(Object ref, String token);
    void OnInvalidCredentials(Object ref);
    void OnRegisterSuccess(Object ref, String token);
    void OnPasswordsNotMatching(Object ref);
    void OnUsernameNotUnique(Object ref);
    void OnExceptionOccurred(Object ref, String exceptionName);
}
