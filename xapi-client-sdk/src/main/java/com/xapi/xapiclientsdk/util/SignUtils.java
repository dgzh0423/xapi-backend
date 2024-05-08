package com.xapi.xapiclientsdk.util;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * 签名工具类
 * @author 15304
 */
public class SignUtils {

    public static String generateSign(String body, String secretKey){
        //使用SHA256算法
        Digester md5 = new Digester(DigestAlgorithm.SHA256);
        //构建签名内容
        String content = body + "." + secretKey;
        //生成签名（16进制）
        return md5.digestHex(content);
    }

}
