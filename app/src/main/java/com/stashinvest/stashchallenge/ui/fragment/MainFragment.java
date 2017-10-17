package com.stashinvest.stashchallenge.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.stashinvest.stashchallenge.App;
import com.stashinvest.stashchallenge.R;
import com.stashinvest.stashchallenge.api.model.ImageResponse;
import com.stashinvest.stashchallenge.api.model.ImageResult;
import com.stashinvest.stashchallenge.ui.adapter.ViewModelAdapter;
import com.stashinvest.stashchallenge.ui.factory.GettyImageFactory;
import com.stashinvest.stashchallenge.ui.fragment.popupdialog.PopUpDialogFragment;
import com.stashinvest.stashchallenge.ui.search.SearchMvpView;
import com.stashinvest.stashchallenge.ui.search.SearchPresenter;
import com.stashinvest.stashchallenge.ui.viewmodel.BaseViewModel;
import com.stashinvest.stashchallenge.util.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.view.View.GONE;

public class MainFragment extends Fragment implements SearchMvpView {
    @Inject
    ViewModelAdapter adapter;
    @Inject
    GettyImageFactory gettyImageFactory;
    @Inject
    SearchPresenter searchPresenter;

    @BindView(R.id.search_phrase) EditText searchEditText;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindDimen(R.dimen.image_space) int space;

    Unbinder unbinder;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);

        searchPresenter.attachView(this);
        searchPresenter.search(RxTextView.textChanges(searchEditText));

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(space, space, space, space));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        searchPresenter.detachView();
    }

    private void updateImages(List<ImageResult> images) {
        List<BaseViewModel> viewModels = new ArrayList<>();
        int i = 0;
        for (ImageResult imageResult : images) {
            viewModels.add(gettyImageFactory.createGettyImageViewModel(i++, imageResult, this::onImageLongPress));
        }
        adapter.setViewModels(viewModels);
    }

    public void onImageLongPress(String id, String uri) {
        //todo - implement new feature
        PopUpDialogFragment dialogFragment = PopUpDialogFragment.newInstance(id, uri);
        dialogFragment.show(getActivity().getSupportFragmentManager(), PopUpDialogFragment.class.getSimpleName());
    }

    @Override
    public void showError() {
        //todo - show error
        Log.e("##", "showError()");
        Toast.makeText(getContext(), "Error loading images", Toast.LENGTH_LONG).show();
        progressBar.setVisibility(GONE);
    }

    @Override
    public void showResults(ImageResponse response) {
        Log.d("##", "showResults() -> images.size(): " + response.getImages().size());
        progressBar.setVisibility(GONE);
        List<ImageResult> images = response.getImages();
        updateImages(images);
    }

    @Override
    public void showLoading(boolean showLoading) {
        Log.d("##", "showLoading()");
        progressBar.setVisibility(showLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showEmpty() {
        Log.d("##", "showEmpty()");
        progressBar.setVisibility(GONE);
        adapter.clear();
    }

}
