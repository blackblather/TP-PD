package common.CWT;

public class Token {
    private String value;

    public Token(){ }

    public Token(String value){
        SetValue(value);
    }

    public void SetValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
