package com.dmio.org.tut.activity.demo.moxie;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dmio.org.tut.R;
import com.dmio.org.tut.core.log.Logger;
import com.dmio.org.tut.core.utils.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoxieActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQ_CODE_INDENTIFY = 0x1001;

    @BindView(R.id.btn_func_email)
    Button btnFuncEmail;

    @BindView(R.id.btn_func_id_verify)
    Button btnFuncIdVerify;

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
        }
    }

}
