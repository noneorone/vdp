package noneorone.org.mvp.ui;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * the presenter
 * Created by wangmeng on 2018/3/14.
 */
public class YuanNotePresenter implements YuanNoteContract.Presenter {

    private final YuanNoteContract.View mView;

    public YuanNotePresenter(YuanNoteContract.View view) {
        mView = checkNotNull(view);
        mView.setPresenter(this);
    }

    @Override
    public void query() {

    }

    @Override
    public void addNote() {
        mView.changeViewState();
    }

    @Override
    public void deleteNote() {

    }

    @Override
    public void updateNote() {

    }
}
