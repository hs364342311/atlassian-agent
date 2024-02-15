package io.zhile.crack.atlassian;

import io.zhile.crack.atlassian.utils.Base64;

import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;

/**
 * @Description DSAKeyPair 类（或接口）
 * @Author huangshun
 * @Date 2024/2/15
 */
public class DSAKeyPair {

    private DSAPrivateKey privateKey;
    private DSAPublicKey publicKey;

    public DSAKeyPair(DSAPrivateKey privateKey, DSAPublicKey publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public DSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    public DSAPublicKey getPublicKey() {
        return publicKey;
    }

    public String getPrivateKeyStr() {
        if (this.privateKey != null) {
            return Base64.encode(this.privateKey.getEncoded());
        }
        return null;
    }

    public String getPublicKeyStr() {
        if (this.publicKey != null) {
            return Base64.encode(this.publicKey.getEncoded());
        }
        return null;
    }
}
