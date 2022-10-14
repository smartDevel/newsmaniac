package eu.ways4.newsmaniac.ui.category.category;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import eu.ways4.newsmaniac.AppUtils;
import eu.ways4.newsmaniac.remote.ApiUtil;
import eu.ways4.newsmaniac.remote.api.ApiServiceInterface;
import eu.ways4.newsmaniac.remote.model.Article;
import eu.ways4.newsmaniac.remote.model.NewsResponse;
import eu.ways4.newsmaniac.utils.ProgressListener;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryViewModel extends ViewModel {

    private static final String TAG = CategoryViewModel.class.getSimpleName();

    private String catCountry = AppUtils.getCountry();

    private MutableLiveData<ArrayList<Article>> articlesLiveData;
    private ApiServiceInterface apiServiceInterface;
    CompositeDisposable disposable;
    ProgressListener listener;

    public CategoryViewModel() {
        articlesLiveData = new MutableLiveData<>();
        apiServiceInterface = ApiUtil.getNewsApiServiceInterface();
        disposable = new CompositeDisposable();
    }

    public void setListener(ProgressListener listener) {
        this.listener = listener;
    }

    //fetch topheadline by countries
/*    public LiveData<ArrayList<Article>> getTechnologyCategory(String category, String apiKey) {
        listener.shoLoading();
        apiServiceInterface.getTopHeadLinesByCategory(category, apiKey).enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    listener.showMovies();
                    ArrayList<Article> articles = response.body().getArticles();
                    articlesLiveData.setValue(articles);
                } else {
                    Log.e(TAG, "onResponse: Error fetching data" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                listener.showNoInternet();
                Log.e(TAG, "onFailure: error" + t.getMessage());
            }
        });
        return articlesLiveData;
    }*/

    public LiveData<ArrayList<Article>> getTechnologyCategory(String category, String apiKey) {
        listener.shoLoading();
        apiServiceInterface.getTopHeadLinesByCategoryCountry(category,catCountry, apiKey).enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    listener.showMovies();
                    ArrayList<Article> articles = response.body().getArticles();
                    articlesLiveData.setValue(articles);
                } else {
                    Log.e(TAG, "onResponse: Error fetching data" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                listener.showNoInternet();
                Log.e(TAG, "onFailure: error" + t.getMessage());
            }
        });
        return articlesLiveData;
    }

    @Override
    protected void onCleared() {
        disposable.dispose();
    }
}
