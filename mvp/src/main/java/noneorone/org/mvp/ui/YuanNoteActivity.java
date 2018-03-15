package noneorone.org.mvp.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import noneorone.org.mvp.R;

public class YuanNoteActivity extends AppCompatActivity {

    private YuanNotePresenter mNotePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mvp_activity_yuan_note);

        // set toolbar as supported action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        YuanNoteFragment fragment = (YuanNoteFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        if (fragment == null) {
            fragment = YuanNoteFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment, fragment);
            transaction.commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }

        mNotePresenter = new YuanNotePresenter(fragment);
    }


}
