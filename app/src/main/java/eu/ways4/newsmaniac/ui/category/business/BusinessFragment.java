package eu.ways4.newsmaniac.ui.category.business;

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
//import com.ways4.newsmaniac.databinding.FragmentBusinessBinding;

import eu.ways4.newsmaniac.R;
//import eu.ways4.newsmaniac.databinding.FragmentBusinessBinding;
import eu.ways4.newsmaniac.databinding.FragmentBusinessBinding;
import eu.ways4.newsmaniac.remote.ApiUtil;
import eu.ways4.newsmaniac.remote.model.Article;
import eu.ways4.newsmaniac.ui.category.category.CategoryViewModel;
import eu.ways4.newsmaniac.utils.AppUtils;
import eu.ways4.newsmaniac.utils.ProgressListener;

public class BusinessFragment extends Fragment implements ProgressListener {
    private static final String TAG = BusinessFragment.class.getSimpleName();

    FragmentBusinessBinding binding;
    BusinessAdapter adapter;
    CategoryViewModel viewModel;
    RecyclerView recyclerView;

    public BusinessFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBusinessBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        viewModel.setListener(this);
        adapter = new BusinessAdapter(requireActivity());
        recyclerView = binding.subCategoryRv.subCategoryRv;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(adapter);
        setUpBusinessArticle();
    }

    private void setUpBusinessArticle() {
        viewModel.getTechnologyCategory("business", ApiUtil.API_KEY).observe(requireActivity(), articles -> {
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
