package com.noo.core.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ViewStubCompat;
import android.view.View;

import com.noo.core.R;

/**
 * Base Activity
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2017/4/18 18:00<br/>
 * @since 1.0
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private View mContainer;

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public View getContainer() {
        return mContainer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initView();
    }

    private void initView() {
        // set toolbar as supported actionbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNavClick();
            }
        });
        setSupportActionBar(mToolbar);

        // fill content layout into containter
        ViewStubCompat viewStubCompat = (ViewStubCompat) findViewById(R.id.container);
        viewStubCompat.setLayoutResource(getContentLayouResId());
        mContainer = viewStubCompat.inflate();
    }

    /**
     * On navigation click, so that sub class can override
     */
    protected void onNavClick() {
        onBackPressed();
    }

    /**
     * @return specific layout resource
     */
    public abstract @LayoutRes int getContentLayouResId();

}
