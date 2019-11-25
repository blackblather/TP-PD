package common.observer;

public abstract class ControllerObserver implements IControllerObserver {
    @Override
    public void Update(Object o) { }

    @Override
    public void OnSuccessfulLogin(Object ref) { }

    @Override
    public void OnFailedLogin(Object ref) { }
}
