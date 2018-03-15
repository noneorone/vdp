package noneorone.org.mvp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import noneorone.org.mvp.R;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A placeholder fragment containing a simple view.
 */
public class YuanNoteFragment extends Fragment implements YuanNoteContract.View, View.OnClickListener {

    private YuanNoteContract.Presenter mPresenter;

    private EditText etContent;
    private Button btnUpdate;
    private Button btnReset;

    public static YuanNoteFragment newInstance() {
        return new YuanNoteFragment();
    }

    public YuanNoteFragment() {
        // Requires empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mvp_fragment_yuan_note, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        etContent = view.findViewById(R.id.et_content);
        btnUpdate = view.findViewById(R.id.btn_update);
        btnReset = view.findViewById(R.id.btn_reset);
        btnUpdate.setOnClickListener(this);
        btnReset.setOnClickListener(this);
    }

    @Override
    public void setPresenter(YuanNoteContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void changeViewState() {

    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        if (vid == R.id.btn_reset) {
        } else if (vid == R.id.btn_update) {
        }
    }
}
