package com.photostream.paging3.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.photostream.paging3.Adapter.MoviesLoadStateAdapter;
import com.photostream.paging3.Adapter.MoviesAdapter;
import com.photostream.paging3.Util.GridSpace;
import com.photostream.paging3.Util.MovieComparator;
import com.photostream.paging3.ViewModel.MainActivityViewModel;
import com.photostream.paging3.databinding.ActivityMainBinding;

/*
This activity shows list of movies using paging 3 library and RecyclerView
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create View binding object
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        // Create new MoviesAdapter object and provide
        MoviesAdapter moviesAdapter = new MoviesAdapter(new MovieComparator());

        // Create ViewModel
        MainActivityViewModel mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        mainActivityViewModel.getSearchResults(getLifecycle()).observe(this, moviesPagingData ->moviesAdapter.submitData(getLifecycle(), moviesPagingData));

        // Create GridlayoutManger with span of count of 2
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        // Finally set LayoutManger to recyclerview
        binding.recyclerViewMovies.setLayoutManager(gridLayoutManager);

        // Add ItemDecoration to add space between recyclerview items
        binding.recyclerViewMovies.addItemDecoration(new GridSpace(2, 20, true));

        // set adapter
        binding.recyclerViewMovies.setAdapter(
                // concat movies adapter with header and footer loading view
                // This will show end user a progress bar while pages are being requested from server
                moviesAdapter.withLoadStateFooter(
                        // Pass footer load state adapter.
                        // When we will scroll down and next page request will be sent
                        // while we get response form server Progress bar will show to end user
                        // If request success Progress bar will hide and next page of movies
                        // will be shown to end user or if request will fail error message and
                        // retry button will be shown to resend the request
                        new MoviesLoadStateAdapter(v -> {
                            moviesAdapter.retry();
                        })));

        // set Grid span
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                // If progress will be shown then span size will be 1 otherwise it will be 2
                return moviesAdapter.getItemViewType(position) == MoviesAdapter.LOADING_ITEM ? 1 : 2;
            }
        });

        moviesAdapter.addLoadStateListener(loadStates -> {
            binding.swipeRefresh.setRefreshing(loadStates.getRefresh() instanceof LoadState.Loading);
            return null;
        });
        binding.swipeRefresh.setOnRefreshListener(() -> {
            binding.swipeRefresh.setRefreshing(false);
//            moviesAdapter.refresh();
        });
    }
}