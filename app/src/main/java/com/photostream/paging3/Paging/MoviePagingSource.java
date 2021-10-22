package com.photostream.paging3.Paging;

import androidx.annotation.NonNull;
import androidx.paging.ListenableFuturePagingSource;
import androidx.paging.PagingState;

import com.google.common.base.Function;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.photostream.paging3.API.APIClient;
import com.photostream.paging3.Model.Movie;
import com.photostream.paging3.Model.MovieResponse;

import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.HttpException;

public class MoviePagingSource extends ListenableFuturePagingSource<Long , Movie> {
    private static long page;
    private static final long UNSPLASH_STARTING_PAGE_INDEX = 1;
    @NonNull
    @Override
    public ListenableFuture<LoadResult<Long, Movie>> loadFuture(@NonNull LoadParams<Long> loadParams) {
        Executor executor = Executors.newSingleThreadExecutor();
        page = loadParams.getKey()==null ? UNSPLASH_STARTING_PAGE_INDEX : loadParams.getKey();
        ListenableFuture<MovieResponse>  movieResponse =  APIClient.getAPIInterface().getMoviesByPage(page);

        ListenableFuture<LoadResult<Long ,Movie>> loadResultListenableFuture = Futures.transform(movieResponse, new Function<MovieResponse, LoadResult<Long, Movie>>() {
            @NullableDecl
            @Override
            public LoadResult<Long, Movie> apply(@NullableDecl MovieResponse input) {
                return new LoadResult.Page<>(input.getResults(), page==UNSPLASH_STARTING_PAGE_INDEX ? null:page-1, input.getResults().isEmpty()? null:page+1);
            }
        }, executor);
        ListenableFuture<LoadResult<Long ,Movie>> loadResultListenableFuture1 = Futures.catching(loadResultListenableFuture, HttpException.class, LoadResult.Error::new, executor);
        return Futures.catching(loadResultListenableFuture1, IOException.class, LoadResult.Error::new, executor);

    }

    @androidx.annotation.Nullable
    @Override
    public Long getRefreshKey(@NonNull PagingState<Long, Movie> pagingState) {
        Integer anchorPosition = pagingState.getAnchorPosition();
        if (anchorPosition == null) {
            return null;
        }

        LoadResult.Page<Long, Movie> anchorPage = pagingState.closestPageToPosition(anchorPosition);
        if (anchorPage == null) {
            return null;
        }

        Long prevKey = anchorPage.getPrevKey();
        if (prevKey != null) {
            return prevKey + 1;
        }

        Long nextKey = anchorPage.getNextKey();
        if (nextKey != null) {
            return nextKey - 1;
        }

        return null;
    }
}