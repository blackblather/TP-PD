package common.controller;

import common.observable.Observable;
import org.json.JSONObject;

public abstract class Controller extends Observable implements IController {
    protected boolean IsValidJSONFormat(JSONObject jsonObject){
        return true;
    }
}
