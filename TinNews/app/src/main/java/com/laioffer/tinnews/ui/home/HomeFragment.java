package com.laioffer.tinnews.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.laioffer.tinnews.R;
import com.laioffer.tinnews.databinding.FragmentHomeBinding;
import com.laioffer.tinnews.model.Article;
import com.laioffer.tinnews.repository.NewsRepository;
import com.laioffer.tinnews.repository.NewsViewModelFactory;
import com.mindorks.placeholderview.SwipeDecor;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements TinNewsCard.OnSwipeListener {

    private HomeViewModel viewModel;
    // View Binding
    private FragmentHomeBinding binding;

    @Override
    public void onLike(Article news) {
        viewModel.setFavoriteArticleInput(news);
    }

    @Override
    public void onDisLike(Article news) {
        if (binding.swipeView.getChildCount() < 3) {
            viewModel.setCountryInput("us");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.onCancel();
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // modify default swipeView configuration
        binding.swipeView
                .getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor().setPaddingTop(20).setRelativeScale(0.01f));
        binding.rejectBtn.setOnClickListener(v -> binding.swipeView.doSwipe(false));
        binding.acceptBtn.setOnClickListener(v -> binding.swipeView.doSwipe(true));

        // setup viewModel
        NewsRepository repository = new NewsRepository(getContext());
        // let owner to manage NewsViewModelFactory
        viewModel = new ViewModelProvider(this, new NewsViewModelFactory(repository)).get(HomeViewModel.class);
        viewModel.setCountryInput("us");
        viewModel
                // viewModel expose data source stream topic
                .getTopHeadlines()
                // observe 实现实时更新
                .observe(
                        getViewLifecycleOwner(),
                        // data callback
                        newsResponse -> {
                            // view 显示 data
                            if (newsResponse != null) {
                                for (Article article : newsResponse.articles) {
                                    TinNewsCard tinNewsCard = new TinNewsCard(article, this);
                                    binding.swipeView.addView(tinNewsCard);
                                }
                            }
                        });

        viewModel
                .onFavorite()
                .observe(
                        getViewLifecycleOwner(),
                        isSuccess -> {
                            if (isSuccess) {
                                // Toast 显示提示
                                Toast.makeText(getContext(), "Success", LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "You might have liked before", LENGTH_SHORT).show();
                            }
                        });
    }
}