package com.stashinvest.stashchallenge.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.stashinvest.stashchallenge.App;
import com.stashinvest.stashchallenge.R;
import com.stashinvest.stashchallenge.api.GettyImageService;
import com.stashinvest.stashchallenge.api.model.ImageResponse;
import com.stashinvest.stashchallenge.api.model.ImageResult;
import com.stashinvest.stashchallenge.ui.adapter.ViewModelAdapter;
import com.stashinvest.stashchallenge.ui.factory.GettyImageFactory;
import com.stashinvest.stashchallenge.ui.viewmodel.BaseViewModel;
import com.stashinvest.stashchallenge.util.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class MainFragment extends Fragment implements Callback<ImageResponse> {
    @Inject
    ViewModelAdapter adapter;
    @Inject
    GettyImageService gettyImageService;
    @Inject
    GettyImageFactory gettyImageFactory;

    @BindView(R.id.search_phrase)
    EditText searchView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindDimen(R.dimen.image_space)
    int space;

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

        searchView.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search();
                return true;
            }
            return false;
        });

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(space, space, space, space));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
        progressBar.setVisibility(GONE);

        if (response.isSuccessful()) {
            List<ImageResult> images = response.body().getImages();
            updateImages(images);
        } else {
            //todo - show error
        }
    }

    @Override
    public void onFailure(Call<ImageResponse> call, Throwable t) {
        progressBar.setVisibility(GONE);

        //todo - show error
    }

    private void search() {
        progressBar.setVisibility(View.VISIBLE);
        Call<ImageResponse> call = gettyImageService.searchImages(searchView.getText().toString());
        call.enqueue(this);
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
    }
}
