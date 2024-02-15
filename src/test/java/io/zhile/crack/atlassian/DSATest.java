package io.zhile.crack.atlassian;

import io.zhile.crack.atlassian.keygen.Encoder;
import io.zhile.crack.atlassian.license.LicenseProperty;
import io.zhile.crack.atlassian.license.products.*;
import io.zhile.crack.atlassian.utils.Base64;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Properties;

/**
 * @Description DSATest 类（或接口）
 * @Author huangshun
 * @Date 2024/2/15
 */
public class DSATest {
    private final static String PRIVATEKEY_STR = "MIIBSwIBADCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoEFgIURblAyO+gKsfY6GOYYXan6CeTKtY=";
    private final static String PUBLICKEY_STR = "MIIBuDCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoDgYUAAoGBAOOAL/wqXmlKQTBxtfPXdbEq/N7mlCgjG5VdQAyPd/3pOVL7RR/FMZijN3v2MZdqF8qhNHpj7m0FhjIzCRcUCj5Ei3LhLL9Iz/mJswyqcy6Gj7WNvBkSV4x1pbHx+XfWWGCoMaOEp1dK8i2fWh7XNfETOyKklzLPdiUnGJ/MGeXC";

    @Test
    public void keyPairGenerator() throws NoSuchAlgorithmException, InvalidKeySpecException {
        //  生成DSA密钥对
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        // 获取私钥 和 公钥
        DSAPrivateKey privateKey = (DSAPrivateKey) keyPair.getPrivate();
        DSAPublicKey publicKey = (DSAPublicKey) keyPair.getPublic();
        DSAKeyPair dsaKeyPair = new DSAKeyPair(privateKey, publicKey);
        String privateKeyStr= dsaKeyPair.getPrivateKeyStr();
        String publicKeyStr = dsaKeyPair.getPublicKeyStr();
        System.out.println("privateKey：" + privateKeyStr);
        System.out.println("publicKey：" + publicKeyStr);
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        PrivateKey privateKey1 = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(privateKeyStr)));
        PublicKey publicKeyKey2 = keyFactory.generatePublic(new X509EncodedKeySpec(Base64.decode(publicKeyStr)));
        System.out.println("ok");
    }

    @Test
    public void loadKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(PRIVATEKEY_STR)));
        PublicKey publicKeyKey = keyFactory.generatePublic(new X509EncodedKeySpec(Base64.decode(PUBLICKEY_STR)));
        System.out.println("ok");
    }

    @Test
    public void testSign() throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        String data = Base64.encode("DEMO".getBytes(StandardCharsets.UTF_8));
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(PRIVATEKEY_STR)));
        Signature signature = Signature.getInstance("SHA1withDSA");
        signature.initSign(privateKey);
        signature.update(Base64.decode(data));
        String sign = Base64.encode(signature.sign());
        System.out.println("sign ->> "+sign);

    }
    @Test
    public void generateLicense(){
        LicenseEncoder licenseEncoder = new LicenseEncoder(PRIVATEKEY_STR);
        String product = "conf";
        String serverID = "BTQL-KWBK-NRSQ-V352";
        String contactEMail = "test@qq.com";
        String organisation = "huangshun";
        String contactName = contactEMail;
        boolean dataCenter = true;

        LicenseProperty property;
        switch (product) {
            case "conf":
                property = new Confluence(contactName, contactEMail, serverID, organisation, dataCenter);
                break;
            case "jira":
                property = new JIRASoftware(contactName, contactEMail, serverID, organisation, dataCenter);
                break;
            case "questions":
                property = new Questions(contactName, contactEMail, serverID, organisation, dataCenter);
                break;
            case "tc":
                property = new TeamCalendars(contactName, contactEMail, serverID, organisation, dataCenter);
                break;
            case "bamboo":
                property = new Bamboo(contactName, contactEMail, serverID, organisation);
                break;
            case "bitbucket":
                property = new Bitbucket(contactName, contactEMail, serverID, organisation, dataCenter);
                break;
            case "fisheye":
                property = new FishEye(contactName, contactEMail, serverID, organisation);
                break;
            case "crucible":
                property = new Crucible(contactName, contactEMail, serverID, organisation);
                break;
            case "crowd":
                property = new Crowd(contactName, contactEMail, serverID, organisation, dataCenter);
                break;
            case "jc":
                property = new JIRACore(contactName, contactEMail, serverID, organisation);
                break;
            case "portfolio":
                property = new Portfolio(contactName, contactEMail, serverID, organisation, dataCenter);
                break;
            case "jsd":
                property = new JIRAServiceDesk(contactName, contactEMail, serverID, organisation, dataCenter);
                break;
            case "training":
                property = new Training(contactName, contactEMail, serverID, organisation);
                break;
            case "capture":
                property = new Capture(contactName, contactEMail, serverID, organisation);
                break;
            default:

                return;
        }

        try {
            String licenseCode = licenseEncoder.encode(property.toString());

            System.out.println("Your license code(Don't copy this line!!!): \n");
            System.out.println(licenseCode);
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void decodeLiences(){
        String licenseString = "AAABOA0ODAoPeJxtkF1rwjAUhu/zKwq7jrR1XVUIbLYdiLVO27nrNBxtoKaaD5n/fjEtKEM4N3nPx\n" +
                "/vmeaka431C7QWRrVk0mY1jLykrL/TDV5RIoJp3IqUayE3BfoiDCGUX2hrXIXvaKkApKCb5ySktZ\n" +
                "yAUePXVawwVB9UYgdbyQAVX/c5dZp3YjyjT/AJESwMo6YS272xFeUs0KP1+Po9Yd+wnS02lBjmYO\n" +
                "inv3arrCQp6BJKsV6tsmyw+cmRvCA2CCgbZ74nL6/CNyRT7sS007C5Ski/SMitwHsR+PJ2Mp0H8F\n" +
                "geoBHkBadvzapPj5c98iYttucG7cRT27vYiTUDcMrn4w8Xndl9GsoYq+E9zALYDqW50QlSa+o7T3\n" +
                "XVmhTnWINf7b2UnCbb5soI8iT0wdDgeEP4B1AihazAtAhQVRIVZQjjXI2edvSD5LSCjGJgmnwIVA\n" +
                "IoW8x/ADK8T1oKZEdBev+WIRmWQX02fj";
        LicenseDecoder licenseDecoder = new LicenseDecoder(PUBLICKEY_STR);
        Properties properties = licenseDecoder.decode(licenseString);
        System.out.println(properties);
    }


}
