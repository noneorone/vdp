package com.dmio.org.tut.security.ca;

import android.content.Context;
import android.text.TextUtils;

import com.itrus.raapi.implement.CertInfo;
import com.itrus.raapi.implement.ClientForAndroid;
import com.itrus.raapi.implement.Helper;

import org.bouncycastle.util.encoders.Base64;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 功能说明：CA工具类
 * 作者：wangmeng on 2016/10/29 11:22
 * 邮箱：noneorone@yeah.net
 */

public class CAHelper {

    private static boolean init = false;

    /**
     * SHA256加密
     *
     * @param source
     * @return
     */
    public static String encryptSHA256(String source) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = source.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(bt);
            strDes = Helper.bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    /**
     * 每一个操作都要校验一下用户PIN码
     *
     * @param context
     * @param uid
     */
    private static void checkUserPIN(Context context, String uid) {
        // 验证pin码 每次必须设置
        int userPIN = ClientForAndroid.getInstance(context).verifyUserPIN(uid);
        if (userPIN != 0) {
            // 设置管理员密码（可以设置一次）
            ClientForAndroid.getInstance(context).setAdminPIN("", "ADMIN");
            // 使用管理员设置pin码（设置一次就可）
            ClientForAndroid.getInstance(context).initUserPIN("ADMIN", uid);
        }
        ClientForAndroid.getInstance(context).verifyUserPIN(uid);
    }

    /**
     * 初始化CA
     *
     * @param context
     * @param uid
     * @return
     */
    public static void init(Context context, String uid) {
        String caDBDir = ClientForAndroid.getInstance(context).getSystemDBDir();
        if (!uid.equals(caDBDir)) {
            String caPath = context.getApplicationContext().getFilesDir().getAbsolutePath() + "/" + uid;
            File file = new File(caPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            //设置数据库
            ClientForAndroid.getInstance(context).setSystemDBDir(caPath);
        }
        // 设置liscense
        ClientForAndroid.getInstance(context).setLicense(Helper.getAppName(context));
        checkUserPIN(context, uid);
        init = true;
    }

    /**
     * 生成CSR
     *
     * @param context
     * @param uid
     * @return
     */
    public static synchronized String genCSR(Context context, String uid) {
        if (!init) {
            init(context, uid);
        }
        checkUserPIN(context, uid);
        String strCSR = ClientForAndroid.getInstance(context).genCSR(uid, "", "", "", 2048, "RSA");
        return strCSR;
    }

    /**
     * 获取所有本地证书索引
     *
     * @param context
     * @param uid
     * @return
     */
    public static String[] getCertIndexs(Context context, String uid) {
        if (!init) {
            init(context, uid);
        }
        checkUserPIN(context, uid);
        return ClientForAndroid.getInstance(context).filterCert("", "", "", 0, 0);
    }

    /**
     * 本地是否保存有证书
     *
     * @param context
     * @param uid
     * @return
     */
    public static synchronized boolean hasCertLocal(Context context, String uid) {
        String[] certIndexs = getCertIndexs(context, uid);
        if (certIndexs == null || certIndexs.length < 1) {
            return false;
        }
        return true;
    }

    /**
     * 导入证书
     *
     * @param context
     * @param uid
     * @param cert
     * @return
     */
    public static boolean importCert(Context context, String uid, String cert) {
        if (!init) {
            init(context, uid);
        }
        checkUserPIN(context, uid);
        int ret = ClientForAndroid.getInstance(context).importCert(cert);
        if (ret == 0) {
            return true;
        }
        return false;
    }

    /**
     * 用证书签名
     *
     * @param context
     * @param uid
     * @param unSignData
     * @return
     */
    public static String sign(Context context, String uid, String unSignData) {
        if (hasCertLocal(context, uid)) {
            String[] certIndexs = getCertIndexs(context, uid);
            String signData = ClientForAndroid.getInstance(context).signMessageBYTE(Base64.decode(unSignData), certIndexs[0], "SHA1", 0);
            return signData;
        }
        return null;
    }

    /**
     * 获取最后错误信息
     *
     * @param context
     * @return
     */
    public static String getLastErrInfo(Context context, String uid) {
        if (init) {
            checkUserPIN(context, uid);
            return ClientForAndroid.getInstance(context).getLastErrInfo();
        }

        return "";
    }

    /**
     * 获取证书信息
     *
     * @param context
     * @param uid
     * @return
     */
    public static CertInfo getCertInfo(Context context, String uid) {
        CertInfo certInfo = null;
        if (hasCertLocal(context, uid)) {
            String[] certIndexs = getCertIndexs(context, uid);
            if (certIndexs != null && certIndexs.length > 0) {
                certInfo = ClientForAndroid.getInstance(context).getCertAttribute(certIndexs[0]);
            }
        }
        return certInfo;
    }

    /**
     * 获取证书序列号
     *
     * @param context
     * @param uid
     * @return
     */
    public static String getCertSerialNumber(Context context, String uid) {
        CertInfo certInfo = getCertInfo(context, uid);
        if (certInfo != null) {
            return certInfo.SerialNumber;
        }
        return null;
    }

    /**
     * 检测是否存在到期的证书
     *
     * @param context
     * @return
     */
    public static boolean existsExpiredCerts(Context context, String uid) {
        CertInfo certInfo = getCertInfo(context, uid);
        try {
            if (certInfo != null) {
                String validTo = certInfo.ValidTo;
                if (!TextUtils.isEmpty(validTo)) {
                    String template = "EEE MMM dd kk:mm:ss yyyy";
                    SimpleDateFormat sdf = new SimpleDateFormat(template, Locale.ENGLISH);
                    Date validToDate = sdf.parse(validTo);
                    Date nowDate = Calendar.getInstance().getTime();
                    if (nowDate.compareTo(validToDate) > 0) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除证书集
     *
     * @param context
     * @return
     */
    public static int deleteAllCerts(Context context, String uid) {
        CertInfo certInfo = getCertInfo(context, uid);
        if (certInfo != null) {
            String serialNumber = certInfo.SerialNumber;
            if (!TextUtils.isEmpty(serialNumber)) {
                return ClientForAndroid.getInstance(context).deleteCertBySerialNumber(serialNumber);
            }
        }

        return 0;
    }

    /**
     * 重置
     */
    public static void reset() {
        CAHelper.init = false;
    }

}
