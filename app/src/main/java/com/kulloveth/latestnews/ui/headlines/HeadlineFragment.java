package com.kulloveth.latestnews.ui.headlines;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.google.android.gms.ads.AdRequest;
import com.google.android.material.snackbar.Snackbar;
//import com.google.firebase.analytics.FirebaseAnalytics;
import com.kulloveth.latestnews.BuildConfig;
import com.kulloveth.latestnews.R;
import com.kulloveth.latestnews.databinding.FragmentHeadlineBinding;
import com.kulloveth.latestnews.local.FavoriteEntity;
import com.kulloveth.latestnews.remote.ApiUtil;
import com.kulloveth.latestnews.remote.model.Article;
import com.kulloveth.latestnews.ui.RxSearchObservable;
import com.kulloveth.latestnews.ui.favorite.FavoriteVieModel;
import com.kulloveth.latestnews.ui.favorite.MyViewModelFactory;
import com.kulloveth.latestnews.ui.widget.WidgetService;
import com.kulloveth.latestnews.utils.AppUtils;
import com.kulloveth.latestnews.utils.ProgressListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class HeadlineFragment extends Fragment implements HeadlineAdapter.ItemCLickedListener, ProgressListener {


    private static final String TAG = HeadlineFragment.class.getSimpleName();
    HeadlineAdapter.ScrollDirection scrollDirection;
    //private static String hlCountry = com.kulloveth.latestnews.AppUtils.getCountry();
    //private static String hlCountry = HeadlineCountry.getSelCountry();

    private static SharedPreferences sharedP;
    private static String mVersionName = BuildConfig.VERSION_NAME;
    //private static String mAppName = BuildConfig.APPLICATION_ID;
    private static String mVersionCode = BuildConfig.VERSION_CODE + "";

    private static String hlCountry;
    HeadlineViewModel viewModel;
    FavoriteVieModel favoriteVieModel;
    FragmentHeadlineBinding binding;
    RecyclerView recyclerView;
    private HeadlineAdapter adapter;
    private SearchView searchView;
    private Toolbar toolbar;
    //private FirebaseAnalytics mFirebaseAnalytics;

    ArrayList<Article> articleArrayList = new ArrayList<>();


    public HeadlineFragment() {


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());


        if (sharedP == null) {
            hlCountry = "de";
        } else {
            hlCountry = sharedP.getString(getString(R.string.saved_SelCountry), "de");
        }
        setAPI_key();

    }

    private void setAPI_key() {

        if (!(sharedP == null)) {
            ApiUtil.API_KEY = sharedP.getString(getString(R.string.saved_apikey), "");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHeadlineBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setAPI_key();
        //private Context context = currActivity;


        toolbar = binding.appBar.toolbar;
        adapter = new HeadlineAdapter(requireActivity());
        recyclerView = binding.headlineRv;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(adapter);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        String hlToolbar = "";
        if (hlCountry == null) {
            hlToolbar = getString(R.string.headline_fragment_category);
        } else {
            switch (hlCountry) {
                case "us":
                    hlToolbar = getString(R.string.u_title);
                    break;
                case "sg":
                    hlToolbar = getString(R.string.singapore_title);
                    break;
                case "au":
                    hlToolbar = getString(R.string.australia_title);
                    break;
                case "nz":
                    hlToolbar = getString(R.string.newzeeland_title);
                    break;
                case "in":
                    hlToolbar = getString(R.string.india_title);
                    break;
                case "hk":
                    hlToolbar = getString(R.string.hongkong_title);
                    break;
                case "nl":
                    hlToolbar = getString(R.string.netherlands_title);
                    break;
                case "tw":
                    hlToolbar = getString(R.string.taiwan_title);
                    break;
                case "gb":
                    hlToolbar = getString(R.string.england_title);
                    break;
                case "fr":
                    hlToolbar = getString(R.string.france_title);
                    break;
                case "za":
                    hlToolbar = getString(R.string.southafrica_title);
                    break;
                default:
                    hlToolbar = getString(R.string.de_title);
                    break;
            }


        }
        AppUtils.setToolbarTitle(hlToolbar, ((AppCompatActivity) requireActivity()));
        viewModel = new ViewModelProvider(requireActivity()).get(HeadlineViewModel.class);
        viewModel.setProgressListener(this);
        favoriteVieModel = new ViewModelProvider(this, new MyViewModelFactory(requireActivity().getApplication())).get(FavoriteVieModel.class);
        setUpHeadLineArticle();
        adapter.setClickedListener(this::itemClicked);
        //AdRequest adRequest = new AdRequest.Builder().build();
        //binding.adView.loadAd(adRequest);


    }


    private void setUpHeadLineArticle() {
        if (hlCountry == null) {
            hlCountry = "de";
        }
        setAPI_key();
        viewModel.getTopHeadlineByCountry(hlCountry, ApiUtil.API_KEY).observe(requireActivity(), articles -> {
            for (Article article : articles) {
                Log.d(TAG, "onActivityCreated: headlines by country " + article.getTitle());
            }
            articleArrayList = articles;
            adapter.submitList(articles);
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    scrollDirection = HeadlineAdapter.ScrollDirection.DOWN;
                } else {
                    scrollDirection = HeadlineAdapter.ScrollDirection.TOP;
                }
            }
        });
        adapter.scrollDirection = scrollDirection;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.headline_detail_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchItem.getActionView();
        ImageView icon = searchView.findViewById(R.id.search_button);
        icon.setColorFilter(Color.YELLOW);
        setUpSearchObservable();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.update:
                WidgetService.actionUpdateWidget(requireActivity(), articleArrayList);
                Snackbar.make(requireView(), R.string.widget_update_msg, Snackbar.LENGTH_LONG).show();
                return true;

            case R.id.filter:
                showAlertDialog();
                return true;
            case R.id.config:
                showConfigDialog();
                return true;
            case R.id.about:
                showAboutDialog();
                return true;
            case R.id.exit:
                System.exit(0);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }


    private void setUpSearchObservable() {
        RxSearchObservable.fromView(searchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter(text -> {
                    if (text.isEmpty()) {
                        adapter.submitList(articleArrayList);
                        return true;
                    } else {
                        return true;
                    }
                })
                .distinctUntilChanged()
                .switchMap((Function<String, ObservableSource<ArrayList<Article>>>) query -> {
                    //query = "%" + query + "%";
                    return viewModel.searchHeadLine(query, ApiUtil.API_KEY);

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(articles -> adapter.submitList(articles), throwable -> {
                    Log.e(TAG, "setUpSearchObservable: error searching" + throwable.getMessage());
                });
    }

    private void showAboutDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireActivity());
        alertDialog.setTitle(getString(R.string.aboutDialog_title));
        // set the custom layout
        final View configLayout = getLayoutInflater().inflate(R.layout.aboutdialog_layout, null);
        alertDialog.setView(configLayout);

        final TextView tvVersionInfo = (TextView) configLayout.findViewById(R.id.tvAppVersioninfoTxt_About);
        final TextView tvAppName = (TextView) configLayout.findViewById(R.id.tvAppNameTxt_About);
        tvAppName.setText(getString(R.string.app_name));
        tvVersionInfo.setText(mVersionName + " " + mVersionCode);



        alertDialog.setCancelable(true);
        // add a cancel button
        alertDialog.setNegativeButton(getString(R.string.btnCancelConfigDialog), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send data from the AlertDialog to the Activity
                dialog.dismiss();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }

    private void showConfigDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireActivity());
        alertDialog.setTitle(getString(R.string.configDialog_title));
        Activity currActivity = getActivity();
        SharedPreferences sharedPref = currActivity.getPreferences(Context.MODE_PRIVATE);
        sharedP = sharedPref;

        SharedPreferences.Editor editor = sharedPref.edit();

        // set the custom layout
        final View configLayout = getLayoutInflater().inflate(R.layout.configdialog_layout, null);
        alertDialog.setView(configLayout);

        //show current_key
        TextView tvCurrKey = configLayout.findViewById(R.id.tvCurrApikey);
        String savedAPIKey = sharedP.getString(getString(R.string.saved_apikey), "");
        if (!((savedAPIKey == null) || (savedAPIKey.isEmpty()))) {
            tvCurrKey.setText(savedAPIKey);
        }

        // add a button
        alertDialog.setPositiveButton(getString(R.string.btnOKConfigDialog), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send data from the AlertDialog to the Activity
                EditText editText = configLayout.findViewById(R.id.etApikey);
                sendDialogDataToActivity(editText.getText().toString());
            }

            private void sendDialogDataToActivity(String toString) {

                editor.putString(getString(R.string.saved_apikey), toString);
                editor.apply();
                ApiUtil.API_KEY = toString;
                Snackbar.make(requireView(), getString(R.string.api_update_msg) + toString, Snackbar.LENGTH_LONG).show();
            }
        });
        //make dialog cancelable
        alertDialog.setCancelable(true);
        // add a cancel button
        alertDialog.setNegativeButton(getString(R.string.btnCancelConfigDialog), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send data from the AlertDialog to the Activity
                dialog.dismiss();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }

    private void showAlertDialog() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireActivity());
        //20220928_changed_countrylist
        //tw added
        alertDialog.setTitle(getString(R.string.countryDialog_title));

        Activity currActivity = getActivity();
        SharedPreferences sharedPref = currActivity.getPreferences(Context.MODE_PRIVATE);
        sharedP = sharedPref;

        SharedPreferences.Editor editor = sharedPref.edit();

        String[] items = {getString(R.string.de_title), getString(R.string.u_title),
                getString(R.string.singapore_title), getString(R.string.australia_title),
                getString(R.string.newzeeland_title), getString(R.string.india_title), getString(R.string.hongkong_title),
                getString(R.string.netherlands_title), getString(R.string.taiwan_title),
                getString(R.string.england_title), getString(R.string.france_title),
                getString(R.string.southafrica_title)};
        int checkedItem;
        if (hlCountry == null) {
            checkedItem = 0;
        } else {
            switch (hlCountry) {
                case "de":
                    checkedItem = 0;
                    break;
                case "us":
                    checkedItem = 1;
                    break;
                case "sg":
                    checkedItem = 2;
                    break;
                case "au":
                    checkedItem = 3;
                    break;
                case "nz":
                    checkedItem = 4;
                    break;
                case "in":
                    checkedItem = 5;
                    break;
                case "hk":
                    checkedItem = 6;
                    break;
                case "nl":
                    checkedItem = 7;
                    break;
                case "tw":
                    checkedItem = 8;
                    break;
                case "gb":
                    checkedItem = 9;
                    break;
                case "fr":
                    checkedItem = 10;
                    break;
                case "za":
                    checkedItem = 11;
                    break;
                default:
                    checkedItem = 0;
                    break;
            }

        }
        alertDialog.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            switch (which) {
                case 0:
                    viewModel.getTopHeadlineByCountry("de", ApiUtil.API_KEY);
                    AppUtils.setToolbarTitle(getString(R.string.de_title), ((AppCompatActivity) requireActivity()));
                    com.kulloveth.latestnews.AppUtils.setCountry("de");
                    editor.putString(getString(R.string.saved_SelCountry), "de");
                    editor.apply();
                    break;
                case 1:
                    viewModel.getTopHeadlineByCountry("us", ApiUtil.API_KEY);
                    AppUtils.setToolbarTitle(getString(R.string.u_title), ((AppCompatActivity) requireActivity()));
                    com.kulloveth.latestnews.AppUtils.setCountry("us");
                    editor.putString(getString(R.string.saved_SelCountry), "us");
                    editor.apply();
                    break;
                case 2:
                    //viewModel.getTopHeadlineByCountry("jp", ApiUtil.API_KEY);
                    //AppUtils.setToolbarTitle("Japan", ((AppCompatActivity) requireActivity()));
                    viewModel.getTopHeadlineByCountry("sg", ApiUtil.API_KEY);
                    AppUtils.setToolbarTitle(getString(R.string.singapore_title), ((AppCompatActivity) requireActivity()));
                    com.kulloveth.latestnews.AppUtils.setCountry("sg");
                    editor.putString(getString(R.string.saved_SelCountry), "sg");
                    editor.apply();
                    break;
                case 3:
                    viewModel.getTopHeadlineByCountry("au", ApiUtil.API_KEY);
                    AppUtils.setToolbarTitle(getString(R.string.australia_title), ((AppCompatActivity) requireActivity()));
                    com.kulloveth.latestnews.AppUtils.setCountry("au");
                    editor.putString(getString(R.string.saved_SelCountry), "au");
                    editor.apply();
                    break;
                case 4:
                    viewModel.getTopHeadlineByCountry("nz", ApiUtil.API_KEY);
                    AppUtils.setToolbarTitle(getString(R.string.newzeeland_title), ((AppCompatActivity) requireActivity()));
                    com.kulloveth.latestnews.AppUtils.setCountry("nz");
                    editor.putString(getString(R.string.saved_SelCountry), "nz");
                    editor.apply();
                    break;
                case 5:
                    viewModel.getTopHeadlineByCountry("in", ApiUtil.API_KEY);
                    AppUtils.setToolbarTitle(getString(R.string.india_title), ((AppCompatActivity) requireActivity()));
                    com.kulloveth.latestnews.AppUtils.setCountry("in");
                    editor.putString(getString(R.string.saved_SelCountry), "in");
                    editor.apply();
                    break;
                case 6:
                    viewModel.getTopHeadlineByCountry("hk", ApiUtil.API_KEY);
                    AppUtils.setToolbarTitle(getString(R.string.hongkong_title), ((AppCompatActivity) requireActivity()));
                    com.kulloveth.latestnews.AppUtils.setCountry("hk");
                    editor.putString(getString(R.string.saved_SelCountry), "hk");
                    editor.apply();
                    break;

                case 7:
//                    viewModel.getTopHeadlineByCountry("ng", ApiUtil.API_KEY);
//                    AppUtils.setToolbarTitle(getString(R.string.ng_title), ((AppCompatActivity) requireActivity()));
                    viewModel.getTopHeadlineByCountry("nl", ApiUtil.API_KEY);
                    AppUtils.setToolbarTitle(getString(R.string.netherlands_title), ((AppCompatActivity) requireActivity()));
                    com.kulloveth.latestnews.AppUtils.setCountry("nl");
                    editor.putString(getString(R.string.saved_SelCountry), "nl");
                    editor.apply();
                    break;

                //20220928_changed_countrylist
                case 8:
                    viewModel.getTopHeadlineByCountry("tw", ApiUtil.API_KEY);
                    AppUtils.setToolbarTitle(getString(R.string.taiwan_title), ((AppCompatActivity) requireActivity()));
                    com.kulloveth.latestnews.AppUtils.setCountry("tw");
                    editor.putString(getString(R.string.saved_SelCountry), "tw");
                    editor.apply();
                    break;
                case 9:
                    viewModel.getTopHeadlineByCountry("gb", ApiUtil.API_KEY);
                    AppUtils.setToolbarTitle(getString(R.string.england_title), ((AppCompatActivity) requireActivity()));
                    com.kulloveth.latestnews.AppUtils.setCountry("gb");
                    editor.putString(getString(R.string.saved_SelCountry), "gb");
                    editor.apply();
                    break;
                case 10:
                    viewModel.getTopHeadlineByCountry("fr", ApiUtil.API_KEY);
                    AppUtils.setToolbarTitle(getString(R.string.france_title), ((AppCompatActivity) requireActivity()));
                    com.kulloveth.latestnews.AppUtils.setCountry("fr");
                    editor.putString(getString(R.string.saved_SelCountry), "fr");
                    editor.apply();
                    break;
                case 11:
                    viewModel.getTopHeadlineByCountry("za", ApiUtil.API_KEY);
                    AppUtils.setToolbarTitle(getString(R.string.southafrica_title), ((AppCompatActivity) requireActivity()));
                    com.kulloveth.latestnews.AppUtils.setCountry("za");
                    editor.putString(getString(R.string.saved_SelCountry), "za");
                    editor.apply();
                    break;
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }

    @Override
    public void itemClicked(Article article, int position) {
        FavoriteEntity favoriteEntity = new FavoriteEntity(position, article.getTitle(), article.getDescription(), article.getUrlToImage(), article.getUrl());
        favoriteVieModel.insertFavorite(favoriteEntity);
        Snackbar.make(requireView(), R.string.liked_article_mesage, Snackbar.LENGTH_SHORT).show();
        Bundle bundle = new Bundle();
        bundle.putString("ITEM_NAME", article.getTitle());
        bundle.putString("CONTENT_TYPE_URL", article.getUrlToImage());
        //bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, article.getTitle());
        //bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, article.getUrlToImage());
        //mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
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
