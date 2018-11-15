package com.asana.git.graphql;

import android.view.View;

import com.apollo.apollographql.api.SearchRepoQuery;
import com.apollographql.apollo.api.Response;
import com.asana.git.graphql.search.GitHubGraphQlRepository;
import com.asana.git.graphql.search.SearchPresenter;
import com.asana.git.graphql.search.SearchViewContract;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

public class SearchPresenterTest {

    private SearchPresenter presenter;

    @Mock
    private GitHubGraphQlRepository repository;
    @Mock
    private SearchViewContract viewContract;

    @Before
    public void setUp() throws Exception {
        // for the "@Mock" annotations
        MockitoAnnotations.initMocks(this);

        // Make presenter a mock while using mock repository and viewContract created above
        presenter = spy(new SearchPresenter(viewContract, repository));

    }

    @Test
    public void searchGitHubRepos_noQuery() {
        String searchQuery = null;
        String afterCursor = null;

        // Trigger
        presenter.searchGitHubRepos(searchQuery, afterCursor);

        // Validation
        verify(repository, never()).searchQuery(searchQuery, afterCursor, presenter);
        verify(viewContract, never()).setProgressBarVisibility(1);

        verify(viewContract, times(1)).displayError("Please Enter a keyword");
    }

    @Test
    public void searchGitHubRepos() {
        String searchQuery = "keyword";
        String afterCursor = null;

        // Trigger
        presenter.searchGitHubRepos(searchQuery, afterCursor);

        // Validation
        verify(repository, times(1)).searchQuery(searchQuery, afterCursor, presenter);
        verify(viewContract, times(1)).setProgressBarVisibility(0);

        verify(viewContract, never()).displayError("Please Enter a keyword");

    }

    @SuppressWarnings("unchecked")
    @Test
    public void handleGitHubResponse_Success() {

        Response<SearchRepoQuery.Data> response = mock(Response.class);

        SearchRepoQuery.Data data = mock(SearchRepoQuery.Data.class);

        SearchRepoQuery.Search search = mock(SearchRepoQuery.Search.class);

        SearchRepoQuery.Edge edge = mock(SearchRepoQuery.Edge.class);

        SearchRepoQuery.PageInfo expectedPageInfo = mock(SearchRepoQuery.PageInfo.class);


        doReturn(false).when(response).hasErrors();

        doReturn(data).when(response).data();

        List<SearchRepoQuery.Edge> mockedList = Arrays.asList(edge,edge,edge);


        doReturn(search).when(data).search();

        when(data.search().edges()).thenReturn(mockedList);
        when(data.search().pageInfo()).thenReturn(expectedPageInfo);

        // Trigger
        presenter.handleGitHubResponse(response);

        // Validation
        Mockito.verify(viewContract, Mockito.times(1)).displaySearchResults(
                mockedList, expectedPageInfo);
        verify(viewContract, times(1)).setProgressBarVisibility(View.GONE);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void handleGitHubResponse_Failure() {

        Response<SearchRepoQuery.Data> response = mock(Response.class);
        doReturn(true).when(response).hasErrors();

        // Trigger
        presenter.handleGitHubResponse(response);

        // Validation
        verify(viewContract, times(1)).displayError();
        verify(viewContract, times(1)).setProgressBarVisibility(View.GONE);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void handleGitHubResponse_EmptyResponse() {
        Response<SearchRepoQuery.Data> response = mock(Response.class);

        doReturn(true).when(response).hasErrors();
        doReturn(null).when(response).data();

        // Trigger
        presenter.handleGitHubResponse(response);

        // Validation
        verify(viewContract, times(1)).displayError();
    }

    @Test
    public void handleGitHubError() {
        // Trigger
        presenter.handleGitHubError();

        // Validation
        verify(viewContract, times(1)).setProgressBarVisibility(View.GONE);
        verify(viewContract, times(1)).displayError();
    }
}
