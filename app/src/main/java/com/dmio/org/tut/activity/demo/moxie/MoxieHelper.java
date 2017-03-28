package com.dmio.org.tut.activity.demo.moxie;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.dmio.org.tut.R;
import com.dmio.org.tut.application.ExApplication;
import com.moxie.client.model.MxParam;

/**
 * Moxie工具类<br/>
 * wangmeng on 2017/3/28 9:16
 * wangmeng@pycredit.cn
 */

public final class MoxieHelper {


    private MoxieHelper() {
    }

    /**
     * 获取通用参数配置
     *
     * @return {@link MxParam}
     */
    private static MxParam getCommonParam() {
        MxParam mxParam = new MxParam();

        // 设置客户ID和API键
        mxParam.setUserId(Config.USER_ID);
        mxParam.setApiKey(Config.API_KEY);

        // 以下是非必须参数设置
        mxParam.setThemeColor(Config.THEME_COLOR);
        mxParam.setAgreementUrl(Config.AGREEMENT_URL);
        mxParam.setBannerBgColor(Config.BANNER_BG_COLOR);
        mxParam.setBannerTxtColor(Config.BANNER_TXT_COLOR);
        ExApplication instance = ExApplication.getInstance();
        String text = instance.getString(R.string.moxie_agreement_entry_text);
        mxParam.setAgreementEntryText(text);

        return mxParam;
    }

    /**
     * 普通验证
     *
     * @param activity   触发源活动项
     * @param function   功能类型{@link Function}
     * @param resultCode 结果返回码
     */
    public static void normalIdentify(FragmentActivity activity, Function function, int resultCode) {
        if (activity != null && function != null) {
            boolean actived = !activity.isFinishing();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                actived = actived && !activity.isDestroyed();
            }
            if (actived) {
                Bundle bundle = new Bundle();
                MxParam mxParam = getCommonParam();
                mxParam.setFunction(function.getValue());
                bundle.putParcelable("param", mxParam);
                Intent intent = new Intent(activity, com.moxie.client.MainActivity.class);
                intent.putExtras(bundle);
                activity.startActivityForResult(intent, resultCode);
            }
        }
    }

    /**
     * 身份验证
     *
     * @param idVerify
     */
    public static void identityVerification(FragmentActivity activity, IDVerify idVerify) {
        if (activity != null && idVerify != null) {
            boolean actived = !activity.isFinishing();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                actived = actived && !activity.isDestroyed();
            }
            if (actived) {
                String openUrl = String.format(Config.VERTIFICATION_URL, idVerify.getType(), Config.TOKEN);
                Bundle bundle = new Bundle();
                bundle.putString("openUrl", openUrl);
                bundle.putString("title", idVerify.getTitle());
                Intent intent = new Intent(activity, WebViewActivity.class);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        }
    }


}
