package com.kulloveth.latestnews.ui.category.science;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.kulloveth.latestnews.R;
import com.kulloveth.latestnews.databinding.FragmentScienceBinding;
import com.kulloveth.latestnews.remote.ApiUtil;
import com.kulloveth.latestnews.remote.model.Article;
import com.kulloveth.latestnews.ui.category.category.CategoryViewModel;
import com.kulloveth.latestnews.ui.category.science.ScienceAdapter;

import com.kulloveth.latestnews.utils.AppUtils;
import com.kulloveth.latestnews.utils.ProgressListener;

public class ScienceFragment extends Fragment implements ProgressListener {

    private static final String TAG = ScienceFragment.class.getSimpleName();

    FragmentScienceBinding binding;
    ScienceAdapter adapter;
    CategoryViewModel viewModel;
    RecyclerView recyclerView;

    public ScienceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentScienceBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        viewModel.setListener(this);
        adapter = new ScienceAdapter(requireActivity());
        recyclerView = binding.subCategoryRv.subCategoryRv;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(adapter);
        setUpScienceArticle();
    }

    private void setUpScienceArticle() {
        viewModel.getTechnologyCategory("science", ApiUtil.API_KEY).observe(requireActivity(), articles -> {
            for (Article article : articles) {
                Log.d(TAG, "onActivityCreated: headlines by country " + article.getTitle());
            }
            adapter.submitList(articles);
        });
    }

    @Override
    public void shoLoading() {
        if (AppUtils.isConnected(requireActivity())) {
            binding.progressbar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            Snackbar.make(requireView(), R.string.no_internet_message, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showMovies() {
        binding.progressbar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showNoInternet() {
        Snackbar.make(requireView(), R.string.no_internet_message, Snackbar.LENGTH_SHORT).show();
    }
}
