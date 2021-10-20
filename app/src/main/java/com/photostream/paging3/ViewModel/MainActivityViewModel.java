package com.photostream.paging3.ViewModel;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import com.photostream.paging3.Model.Movie;
import com.photostream.paging3.Paging.MoviePagingSource;

/*
    ViewModel for MainActivity
 */
public class MainActivityViewModel extends ViewModel {

    public MainActivityViewModel() {
        init();
    }

    // Init ViewModel Data
    private void init() {
        // Define Paging Source
        MoviePagingSource moviePagingSource = new MoviePagingSource();

        // Create new Pager
        Pager<Integer, Movie> pager = new Pager(
                // Create new paging config
                new PagingConfig(20, // pageSize - Count of items in one page
                        20, // prefetchDistance - Number of items to prefetch
                        false, // enablePlaceholders - Enable placeholders for data which is not yet loaded
                        20, // initialLoadSize - Count of items to be loaded initially
                        20 * 499),// maxSize - Count of total items to be shown in recyclerview
                () -> moviePagingSource); // set paging source

    }

    public LiveData<PagingData<Movie>> getSearchResults(Lifecycle lifecycle){
        MoviePagingSource moviePagingSource = new MoviePagingSource();
        Pager<Integer, Movie> pager = new Pager(
                // Create new paging config
                new PagingConfig(20, // pageSize - Count of items in one page
                        20, // prefetchDistance - Number of items to prefetch
                        false, // enablePlaceholders - Enable placeholders for data which is not yet loaded
                        20, // initialLoadSize - Count of items to be loaded initially
                        20 * 499),// maxSize - Count of total items to be shown in recyclerview
                () -> moviePagingSource); // set paging source

        return PagingLiveData.cachedIn(PagingLiveData.getLiveData(pager), lifecycle);
    }
}
