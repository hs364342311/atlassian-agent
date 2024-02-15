package io.zhile.crack.atlassian;

import io.zhile.crack.atlassian.utils.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.zip.Deflater;
import java.util.zip.DeflaterInputStream;

/**
 * @Description LicenseEcoder 类（或接口）
 * @Author huangshun
 * @Date 2024/2/15
 */
public class LicenseEncoder {

    private final String privateKeyStr;
    private PrivateKey privateKey;

    public LicenseEncoder(String privateKeyStr) {
        this.privateKeyStr = privateKeyStr;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("DSA");
            privateKey  = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(privateKeyStr)));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    public String encode(String licenseText) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        byte[] licenseData = zipText(licenseText.getBytes(StandardCharsets.UTF_8));
        byte[] text = new byte[licenseData.length + 5];
        text[0] = 13;   // license prefix
        text[1] = 14;
        text[2] = 12;
        text[3] = 10;
        text[4] = 15;
        System.arraycopy(licenseData,0,text,5,licenseData.length);
        Signature signature = Signature.getInstance("SHA1withDSA");
        signature.initSign(privateKey);
        signature.update(text);
        return packLicense(text,signature.sign());
    }

    private static byte[] zipText(byte[] licenseText) throws IOException {
        int len;
        byte[] buff = new byte[64];
        ByteArrayInputStream in = new ByteArrayInputStream(licenseText);
        DeflaterInputStream deflater = new DeflaterInputStream(in, new Deflater());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            while ((len = deflater.read(buff)) > 0) {
                out.write(buff, 0, len);
            }
            return out.toByteArray();
        } finally {
            out.close();
            deflater.close();
            in.close();
        }

    }

    private static String packLicense(byte[] text, byte[] hash) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dOut = new DataOutputStream(out);
        dOut.writeInt(text.length);
        dOut.write(text);
        dOut.write(hash);
        String result = Base64.encode(out.toByteArray()).trim();
        return split(result+"X02"+Integer.toString(result.length(),31));
    }

    private static String split(String licenseData) {
        if (licenseData == null || licenseData.length() <= 0) {
            return licenseData;
        }
        char[] chars = licenseData.toCharArray();
        StringBuffer buf = new StringBuffer(chars.length + chars.length / 76);
        for (int i = 0; i < chars.length; i++) {
            buf.append(chars[i]);
            if (i > 0 && i % 76 == 0) {
                buf.append("\n");
            }
        }
        return buf.toString();
    }


}
