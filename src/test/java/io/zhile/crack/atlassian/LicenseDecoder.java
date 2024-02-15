package io.zhile.crack.atlassian;

import io.zhile.crack.atlassian.utils.Base64;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Properties;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**
 * @Description LicenseDecoder 类（或接口）
 * @Author huangshun
 * @Date 2024/2/15
 */
public class LicenseDecoder {

    private final String publicKeyStr;
    private PublicKey publicKey;

    public LicenseDecoder(String publicKeyStr) {
        this.publicKeyStr = publicKeyStr;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("DSA");
            this.publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(Base64.decode(publicKeyStr)));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    public Properties decode(String licenseString) {
        String encodedLicenseTextAndHash = this.getLicenseContent(removeWhiteSpaces(licenseString));
        byte[] licenseText = this.checkAndGetLicenseText(encodedLicenseTextAndHash);
        Reader reader = this.unzipText(licenseText);

        return this.loadLicenseConfiguration(reader);
    }

    private static String removeWhiteSpaces(String licenseData) {
        if (licenseData != null && licenseData.length() != 0) {
            char[] chars = licenseData.toCharArray();
            StringBuffer buf = new StringBuffer(chars.length);

            for (int i = 0; i < chars.length; ++i) {
                if (!Character.isWhitespace(chars[i])) {
                    buf.append(chars[i]);
                }
            }

            return buf.toString();
        } else {
            return licenseData;
        }
    }

    private String getLicenseContent(String licenseString) {
        String lengthStr = licenseString.substring(licenseString.lastIndexOf(88) + 3);
        int encodedLicenseLength = Integer.valueOf(lengthStr, 31);
        return licenseString.substring(0, encodedLicenseLength);
    }

    private byte[] checkAndGetLicenseText(String licenseContent) {
        try {
            byte[] decodeBytes = Base64.decode(licenseContent);
            ByteArrayInputStream in = new ByteArrayInputStream(decodeBytes);
            DataInputStream dIn = new DataInputStream(in);
            int textLength = dIn.readInt();
            byte[] licenseText = new byte[textLength];
            dIn.read(licenseText);
            byte[] hash = new byte[dIn.available()];
            dIn.read(hash);
            String encodeLicenseText = Base64.encode(licenseText);
            String encodeHash = Base64.encode(hash);
            if (!this.verify(encodeLicenseText, encodeHash)) {
                throw new RuntimeException("Failed  to verify the license");
            }
            return licenseText;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Boolean verify(String encodeLicenseText, String encodeHash) {
        try {
            Signature signature = Signature.getInstance("SHA1withDSA");
            signature.initVerify(this.publicKey);
            signature.update(Base64.decode(encodeLicenseText));
            return signature.verify(Base64.decode(encodeHash));
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Reader unzipText(byte[] licenseText) {
        byte[] LICENSE_PREFIX = new byte[]{13, 14, 12, 10, 15};
        ByteArrayInputStream in = new ByteArrayInputStream(licenseText);
        in.skip(LICENSE_PREFIX.length);
        InflaterInputStream zipIn = new InflaterInputStream(in, new Inflater());
        return new InputStreamReader(zipIn, StandardCharsets.UTF_8);
    }

    private Properties loadLicenseConfiguration(Reader reader) {
        Properties properties  = new Properties();
        try {
            new DefaultPropertiesPersister().load(properties,reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
