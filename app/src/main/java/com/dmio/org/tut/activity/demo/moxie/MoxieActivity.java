package com.dmio.org.tut.activity.demo.moxie;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.dmio.org.tut.R;
import com.noo.core.log.Logger;
import com.dmio.org.tut.widget.flowlayout.FlowLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoxieActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQ_CODE_INDENTIFY = 0x1001;

    @BindView(R.id.fl_func)
    FlowLayout flFunc;

    @BindView(R.id.btn_func_email)
    TextView btnFuncEmail;

    @BindView(R.id.btn_func_id_verify)
    TextView btnFuncIdVerify;


    @BindView(R.id.btn_func_renhangzhengxin)
    TextView btnFuncRenhangzhengxin;

    @BindView(R.id.btn_func_shebao)
    TextView btnFuncShebao;

    @BindView(R.id.btn_func_gongjijin)
    TextView btnFuncGongjijin;

    @BindView(R.id.btn_func_geshui)
    TextView btnFuncGeshui;

    @BindView(R.id.btn_func_yunyingshang)
    TextView btnFuncYunyingshang;

    @BindView(R.id.btn_func_xuexinwang)
    TextView btnFuncXuexinwang;

    @BindView(R.id.btn_func_taobao)
    TextView btnFuncTaobao;

    @BindView(R.id.btn_func_jingdong)
    TextView btnFuncJingdong;

    @BindView(R.id.btn_func_xinyongka)
    TextView btnFuncXinyongka;

    @BindView(R.id.tv_result)
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moxie);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_INDENTIFY:
                if (resultCode == RESULT_OK && data != null) {
                    Bundle b = data.getExtras();              //data为B中回传的Intent
                    String json = b.getString("result");    //result即为回传的值(JSON格式)
                    Logger.d("result=" + json);
                    tvResult.setText(json);

                    Result result = Result.getValue(json);
                    if (result != null) {
                        Result.Type type = Result.Type.getByCode(result.getCode());
                        if (type != null) {
                            StringBuilder text = new StringBuilder(type.getValue());
                            String message = result.getMessage();
                            if (!TextUtils.isEmpty(message)) {
                                text.append("(");
                                text.append(message);
                                text.append(")");
                            }
                            Snackbar.make(tvResult, text, Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        Snackbar.make(tvResult, Result.Type.C11.getValue(), Snackbar.LENGTH_LONG).show();
                    }

                }
                break;
        }
    }


    private void initView() {
        btnFuncEmail.setOnClickListener(this);
        btnFuncIdVerify.setOnClickListener(this);

        btnFuncRenhangzhengxin.setOnClickListener(this);
        btnFuncShebao.setOnClickListener(this);
        btnFuncGongjijin.setOnClickListener(this);
        btnFuncGeshui.setOnClickListener(this);
        btnFuncYunyingshang.setOnClickListener(this);
        btnFuncXuexinwang.setOnClickListener(this);
        btnFuncTaobao.setOnClickListener(this);
        btnFuncJingdong.setOnClickListener(this);
        btnFuncXinyongka.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_func_email:
                MoxieHelper.normalIdentify(MoxieActivity.this, Function.EMAIL, REQ_CODE_INDENTIFY);
                break;
            case R.id.btn_func_id_verify:
                MoxieHelper.identityVerification(MoxieActivity.this, IDVerify.ID_CARD);
                break;

            case R.id.btn_func_renhangzhengxin:
                MoxieHelper.normalIdentify(MoxieActivity.this, Function.ZHENGXIN, REQ_CODE_INDENTIFY);
                break;
            case R.id.btn_func_shebao:
                MoxieHelper.normalIdentify(MoxieActivity.this, Function.SECURITY, REQ_CODE_INDENTIFY);
                break;
            case R.id.btn_func_gongjijin:
                MoxieHelper.normalIdentify(MoxieActivity.this, Function.FUND, REQ_CODE_INDENTIFY);
                break;
            case R.id.btn_func_geshui:
                MoxieHelper.normalIdentify(MoxieActivity.this, Function.TAX, REQ_CODE_INDENTIFY);
                break;
            case R.id.btn_func_yunyingshang:
                MoxieHelper.normalIdentify(MoxieActivity.this, Function.CARRIER, REQ_CODE_INDENTIFY);
                break;
            case R.id.btn_func_xuexinwang:
                MoxieHelper.normalIdentify(MoxieActivity.this, Function.CHSI, REQ_CODE_INDENTIFY);
                break;
            case R.id.btn_func_taobao:
                MoxieHelper.normalIdentify(MoxieActivity.this, Function.TAOBAO, REQ_CODE_INDENTIFY);
                break;
            case R.id.btn_func_jingdong:
                MoxieHelper.normalIdentify(MoxieActivity.this, Function.JINGDONG, REQ_CODE_INDENTIFY);
                break;
            case R.id.btn_func_xinyongka:
                MoxieHelper.normalIdentify(MoxieActivity.this, Function.ONLINE_BANK, REQ_CODE_INDENTIFY);
                break;
        }

        // 改变当前选中项样式
        int count = flFunc.getChildCount();
        for (int index = 0; index < count; index++) {
            View view = flFunc.getChildAt(index);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                if (view.getId() == v.getId()) {
                    tv.setTextColor(ContextCompat.getColor(this, R.color.c1));
                } else {
                    tv.setTextColor(ContextCompat.getColor(this, R.color.c8));
                }
            }
        }

    }

}
