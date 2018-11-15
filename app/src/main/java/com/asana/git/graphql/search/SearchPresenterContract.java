package com.asana.git.graphql.search;
/**
 * SearchPresenterContract interface for the presenter
 *
 *
 * @author Gowsik K C
 * @version 1.0 ,10/27/2018
 * @since 1.0
 */
public interface SearchPresenterContract {

    /**
     * searches the git hub repository with keyword and after cursor
     *
     * @param searchQuery search keyword string
     * @param afterCursor after cursor string
     */
    void searchGitHubRepos(String searchQuery, String afterCursor);

}
