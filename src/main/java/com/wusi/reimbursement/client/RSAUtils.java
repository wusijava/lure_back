package com.wusi.reimbursement.client;

import com.wusi.reimbursement.exception.BaseException;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * RSA的签名及验签
 *
 * @author wusi
 * @date 2019-11-23 14:43:53
 */
public class RSAUtils {

    private static Logger log = LoggerFactory.getLogger(RSAUtils.class);

    /**
     * 加密类型
     */
    private static final String SIGN_TYPE_RSA = "RSA";

    /**
     * 加密类型
     */
    private static final String SIGN_TYPE_RSA2 = "RSA2";

    /**
     * 签名算法
     */
    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    /**
     * 签名算法
     */
    private static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";

    /**
     * 默认字节长度
     */
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * RSA/RSA2 生成签名
     *
     * @param map        包含 sign_type、charset
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String rsaSign(Map<String, String> map, String privateKey) throws Exception {
        PrivateKey priKey = null;
        java.security.Signature signature = null;
        String signType = map.get("sign_type");
        String charset = map.get("charset");
        map.remove("sign_type");
        String content = getSignContent(map);
        if (SIGN_TYPE_RSA.equals(signType)) {
            priKey = getPrivateKeyFromPKCS8(SIGN_TYPE_RSA, new ByteArrayInputStream(privateKey.getBytes()));
            signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
        } else if (SIGN_TYPE_RSA2.equals(signType)) {
            priKey = getPrivateKeyFromPKCS8(SIGN_TYPE_RSA, new ByteArrayInputStream(privateKey.getBytes()));
            signature = java.security.Signature.getInstance(SIGN_SHA256RSA_ALGORITHMS);
        } else {
            log.error("不支持的签名类型:{}", signType);
            throw new BaseException("不支持的签名类型:signType=" + signType);
        }
        signature.initSign(priKey);
        if (ObjectUtils.isEmpty(charset)) {
            signature.update(content.getBytes());
        } else {
            signature.update(content.getBytes(charset));
        }
        byte[] signed = signature.sign();
        return new String(Base64.encodeBase64(signed));

    }

    /**
     * 验签方法
     *
     * @param map       包含 sign_type、charset,sign
     * @param publicKey 公钥
     * @param forceCheck 是否强制校验签名
     * @return
     * @throws Exception
     */
    public static boolean rsaCheck(Map<String, String> map, String publicKey,Boolean forceCheck) throws Exception {
        String sign = map.get("sign");
        if (!forceCheck &&  sign == null){
            return true;
        }else if (forceCheck && sign == null){
            return false;
        }
        return rsaCheck(map,publicKey);
    }

    /**
     * 验签方法
     *
     * @param map       包含 sign_type、charset,sign
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    public static boolean rsaCheck(Map<String, String> map, String publicKey) throws Exception {
        java.security.Signature signature = null;
        String signType = map.get("sign_type");
        String charset = map.get("charset");
        String sign = map.get("sign");
        map.remove("sign_type");
        map.remove("sign");
        String content = getSignContent(map);
        PublicKey pubKey = null;
        if (SIGN_TYPE_RSA.equals(signType)) {
            pubKey = getPublicKeyFromX509(SIGN_TYPE_RSA, new ByteArrayInputStream(publicKey.getBytes()));
            signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
        } else if (SIGN_TYPE_RSA2.equals(signType)) {
            pubKey = getPublicKeyFromX509(SIGN_TYPE_RSA, new ByteArrayInputStream(publicKey.getBytes()));
            signature = java.security.Signature.getInstance(SIGN_SHA256RSA_ALGORITHMS);
        } else {
            log.error("不支持的签名类型:{}", signType);
            throw new Exception("不支持的签名类型 : signType=" + signType);
        }
        signature.initVerify(pubKey);
        if (ObjectUtils.isEmpty(charset)) {
            signature.update(content.getBytes());
        } else {
            signature.update(content.getBytes(charset));
        }
        return signature.verify(Base64.decodeBase64(sign.getBytes()));
    }

    /**
     * 初始化私钥
     *
     * @param algorithm
     * @param ins
     */
    static PrivateKey getPrivateKeyFromPKCS8(String algorithm, InputStream ins) throws Exception {
        if (ins == null || ObjectUtils.isEmpty(algorithm)) {
            return null;
        }

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        byte[] encodedKey = readText(ins).getBytes();

        encodedKey = Base64.decodeBase64(encodedKey);

        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
    }

    /**
     * 初始化公钥
     *
     * @param algorithm
     * @param ins
     */
    static PublicKey getPublicKeyFromX509(String algorithm, InputStream ins) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        StringWriter writer = new StringWriter();
        io(new InputStreamReader(ins), writer, -1);

        byte[] encodedKey = writer.toString().getBytes();

        encodedKey = Base64.decodeBase64(encodedKey);

        return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
    }

    /**
     * 把参数合成成字符串
     *
     * @param sortedParams
     * @return
     */
    static String getSignContent(Map<String, String> sortedParams) {
        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList(sortedParams.keySet());
        Collections.sort(keys);
        int index = 0;
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = sortedParams.get(key);
            if (ObjectUtils.isNotEmpty(key) && ObjectUtils.isNotEmpty(value)) {
                content.append((index == 0 ? "" : "&") + key + "=" + value);
                index++;
            }
        }
        return content.toString();
    }

    static String readText(InputStream ins) throws IOException {
        Reader reader = new InputStreamReader(ins);
        StringWriter writer = new StringWriter();

        io(reader, writer, -1);
        return writer.toString();
    }

    static void io(Reader in, Writer out, int bufferSize) throws IOException {
        if (bufferSize == -1) {
            bufferSize = DEFAULT_BUFFER_SIZE >> 1;
        }

        char[] buffer = new char[bufferSize];
        int amount;

        while ((amount = in.read(buffer)) >= 0) {
            out.write(buffer, 0, amount);
        }
    }

}
