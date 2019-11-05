package common.observer;

import common.model.Response;

public interface IObserver {
    void Update(Response resp);
}
