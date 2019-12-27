package common.CWT;

import common.CWT.exceptions.InvalidTokenException;
import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Tokenizer {
    private final String authorizationType = "PD ";
    private final String separator = ".";

    private final String MACHashFunction = "HmacSHA256";
    private Mac HMAC_SHA256;

    public Tokenizer(String key) throws InvalidKeyException, NoSuchAlgorithmException {
        //Instantiates HMAC_SHA256 object
        HMAC_SHA256 = Mac.getInstance(MACHashFunction);
        //Sets private key
        HMAC_SHA256.init(new SecretKeySpec(key.getBytes(), MACHashFunction));
    }

    public Token GetToken(Payload payload) throws InvalidKeyException {
        //Gets payload encoded in base64
        String payloadBase64 = Base64.getEncoder().encodeToString(payload.toJSONObject().toString().getBytes());
        //Gets "signature" byte array
        byte[] signatureArray = HMAC_SHA256.doFinal(payloadBase64.getBytes());
        //Gets "signature" string
        String signatureBase64 = Base64.getEncoder().encodeToString(signatureArray);
        //Returns token
        return new Token(authorizationType + payloadBase64 + separator + signatureBase64);
    }

    public Payload GetPayload(Token token) throws JSONException, InvalidTokenException {
        String tokenStr = token.toString();

        //Checks if token starts with correct authorization type
        if(tokenStr.startsWith(authorizationType)){
            //Removes authorization type from tokenStr
            tokenStr = tokenStr.substring(authorizationType.length());
            //Splits tokenStr using separator
            String[] payloadBase64AndSignatureParts = tokenStr.split("\\" + separator);

            if(payloadBase64AndSignatureParts.length == 2) {

                //Get token parts
                String payloadBase64FromToken = payloadBase64AndSignatureParts[0];
                String signatureBase64FromToken = payloadBase64AndSignatureParts[1];

                //Gets "signature" byte array
                byte[] signatureArray = HMAC_SHA256.doFinal(payloadBase64FromToken.getBytes());
                //Gets "signature" string
                String signatureBase64 = Base64.getEncoder().encodeToString(signatureArray);

                //Compare signatures
                if(signatureBase64FromToken.equals(signatureBase64)){
                    //Decode payload
                    byte[] decodedPayloadBytes = Base64.getDecoder().decode(payloadBase64FromToken);
                    String decodedPayload = new String(decodedPayloadBytes);
                    //Gets payload JSONObject
                    JSONObject jsonPayload = new JSONObject(decodedPayload);
                    //Returns payload object from JSONObject
                    return new Payload(jsonPayload);
                }
            }
        }
        throw new InvalidTokenException();
    }
}
