package com.zub.covid_19;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.zub.covid_19.adapter.NewsAdapter;
import com.zub.covid_19.api.newsData.NewsData;
import com.zub.covid_19.util.SpacesItemDecoration;
import com.zub.covid_19.vm.NewsDataViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private ArrayList<String> mNewsImage = new ArrayList<>();
    private ArrayList<String> mNewsTitle = new ArrayList<>();
    private ArrayList<String> mNewsURL = new ArrayList<>();

    private ShimmerFrameLayout mNewsShimmer;

    private RecyclerView mNewsRecyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mNewsShimmer = view.findViewById(R.id.shimmer_layout);
        mNewsRecyclerView = view.findViewById(R.id.news_recycleview);

        NewsDataViewModel newsDataViewModel;

        newsDataViewModel = ViewModelProviders.of(this).get(NewsDataViewModel.class);
        newsDataViewModel.init();

        newsDataViewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    showLoading();
                } else {
                    hideLoading();
                }
            }
        });

        newsDataViewModel.getNewsData().observe(this, new Observer<NewsData>() {
            @Override
            public void onChanged(NewsData newsData) {
                List<NewsData.Articles> articles = newsData.getArticles();
                for (NewsData.Articles theArticle : articles) {
                    mNewsTitle.add(theArticle.getTitle());
                    mNewsImage.add(theArticle.getUrltoimage());
                    mNewsURL.add(theArticle.getUrl());
                }

                NewsAdapter newsAdapter = new NewsAdapter(mNewsImage, mNewsTitle, mNewsURL, getContext());
                mNewsRecyclerView.setAdapter(newsAdapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
                mNewsRecyclerView.setLayoutManager(linearLayoutManager);
                mNewsRecyclerView.addItemDecoration(new SpacesItemDecoration(20));
                SnapHelper snapHelper = new PagerSnapHelper();
                if (mNewsRecyclerView.getOnFlingListener() == null)
                    snapHelper.attachToRecyclerView(mNewsRecyclerView);
            }
        });

        return view;

    }

    private void hideLoading() {
        mNewsShimmer.stopShimmer();
        mNewsShimmer.setVisibility(View.GONE);
        mNewsRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        mNewsShimmer.setVisibility(View.VISIBLE);
        mNewsRecyclerView.setVisibility(View.GONE);
    }

}