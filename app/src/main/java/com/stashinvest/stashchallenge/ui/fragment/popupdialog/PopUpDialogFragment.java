package com.stashinvest.stashchallenge.ui.fragment.popupdialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stashinvest.stashchallenge.App;
import com.stashinvest.stashchallenge.R;
import com.stashinvest.stashchallenge.api.model.ImageResponse;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PopUpDialogFragment extends DialogFragment implements PopUpDialogMvpView {

    public static final String KEY_ID = "id";
    public static final String KEY_URI = "uri";

    @BindView(R.id.image_view) ImageView imageView;
    @BindView(R.id.title_view) TextView titleView;
    @BindView(R.id.similar_image_view1) ImageView simliarImageView1;
    @BindView(R.id.similar_image_view2) ImageView simliarImageView2;
    @BindView(R.id.similar_image_view3) ImageView simliarImageView3;

    @Inject PopUpDialogPresenter presenter;

    public static PopUpDialogFragment newInstance(String id, String uri) {
        PopUpDialogFragment fragment = new PopUpDialogFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ID, id);
        args.putString(KEY_URI, uri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_popup, container);
        ButterKnife.bind(this, view);
        presenter.attachView(this);
        App.getInstance().getAppComponent().inject(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String id;
        String uri;

        Bundle args = getArguments();
        if (args != null) {
            id = args.getString(KEY_ID);
            uri = args.getString(KEY_URI);
            presenter.getImageData(id);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void shodError(String error) {

    }

    @Override
    public void onSuccessGettingImageData(ImageResponse response) {

    }
}
