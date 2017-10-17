package com.stashinvest.stashchallenge.ui.fragment.popupdialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stashinvest.stashchallenge.App;
import com.stashinvest.stashchallenge.R;
import com.stashinvest.stashchallenge.api.model.ImageResponse;
import com.stashinvest.stashchallenge.api.model.ImageResult;
import com.stashinvest.stashchallenge.api.model.MetadataResponse;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PopUpDialogFragment extends DialogFragment implements PopUpDialogMvpView {

    public static final String KEY_ID = "id";
    public static final String KEY_URI = "uri";

    @BindView(R.id.image_view) ImageView imageView;
    @BindView(R.id.title_view) TextView titleTextView;
    @BindView(R.id.artist_view) TextView artistTextView;
    @BindView(R.id.similar_image_view1) ImageView similarImageView1;
    @BindView(R.id.similar_image_view2) ImageView similarImageView2;
    @BindView(R.id.similar_image_view3) ImageView similarImageView3;
    @BindView(R.id.dialogLayout) LinearLayout dialogLayout;
    @BindView(R.id.errorLayout) FrameLayout errorLayout;

    @Inject PopUpDialogPresenter presenter;

    private String id;
    private String uri;

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
        App.getInstance().getAppComponent().inject(this);
        presenter.attachView(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
    public void onSuccessGettingImageData(ImageResponse response) {

        setImage(imageView, uri);

        ImageResult similarImage1 = response.getImages().get(0);
        ImageResult similarImage2 = response.getImages().get(1);
        ImageResult similarImage3 = response.getImages().get(2);

        if (similarImage1 != null) {
            setImage(similarImageView1, similarImage1.getThumbUri());
        }

        if (similarImage1 != null) {
            setImage(similarImageView2, similarImage2.getThumbUri());
        }

        if (similarImage1 != null) {
            setImage(similarImageView3, similarImage3.getThumbUri());
        }
    }

    @Override
    public void onSuccessGettingMetaData(MetadataResponse response) {

        String imageTitle = response.getMetadata().get(0).getTitle();
        String artist = response.getMetadata().get(0).getArtist();

        titleTextView.setText(imageTitle);
        artistTextView.setText(artist);
    }

    private void setImage(final ImageView imageView, final String url) {
        if (!url.isEmpty()) {
            Picasso.with(getContext()).load(url).into(imageView);
        }
    }

    @Override
    public void showError(String error) {
        Log.e("##", error);
        dialogLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showResult() {
        dialogLayout.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
    }

}
