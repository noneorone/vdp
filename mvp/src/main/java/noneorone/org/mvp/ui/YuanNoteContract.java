package noneorone.org.mvp.ui;

import noneorone.org.mvp.architech.BasePresenter;
import noneorone.org.mvp.architech.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 * Created by wangmeng on 2018/3/14.
 */
public interface YuanNoteContract {

    interface View extends BaseView<Presenter> {
        void changeViewState();
    }

    interface Presenter extends BasePresenter {
        void addNote();

        void deleteNote();

        void updateNote();
    }

}
