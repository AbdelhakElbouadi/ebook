package com.mouka.ebook.beans;

import com.mouka.ebook.entity.Author;
import com.mouka.ebook.entity.Book;
import com.mouka.ebook.entity.Category;
import com.mouka.ebook.service.BookService;
import com.mouka.ebook.service.CategoryService;
import com.mouka.ebook.service.UserService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.commons.lang3.text.WordUtils;

/**
 *
 * @author Abdelhak
 */
@Named(value = "searchbook")
@ViewScoped
public class SearchBookBean implements Serializable{
    
    @EJB
    private BookService bookService;
    
    @EJB
    private CategoryService categoryService;
    
    @EJB
    private UserService userService;
    
    private boolean someSearched = false;
    private String searchRequest;
    private List<Book> searchedBooks = new ArrayList<>();
    private List<Book> newBooks = new ArrayList<>();
    private List<Book> buyedBooks = new ArrayList<>();
    private String searchMsg = "";
    private Category selectedCategory;
    private List<Category> categories;
    private Author selectedAuthor;
    private List<Author> authors;
    
    public SearchBookBean() {
    }
    
    @PostConstruct
    private void init(){
        //search();
    }
    
    public String search(){
        
        if(searchRequest != null && !searchRequest.isEmpty()){
            //Convert to lowercase because db hold data in lowercase.
            searchRequest = searchRequest.toLowerCase();
            searchedBooks = bookService.makeGeneralSearch(searchRequest);
            setSomeSearched(true);
        }
        
        return null;
    }
    
    public String capitalize(String title){
        String newTitle = "";
        newTitle  = WordUtils.capitalize(title);
        
        return newTitle;
    }
    
    public List<Category> getAllCategory(){
        categories = categoryService.findAll();
        
        return categories;
    }
    
    //We get the author depending on the selected category
    public List<Author> getAuthorsInCategory(){
        if(selectedCategory != null)
            authors = userService.getAuthorsByCategory(selectedCategory.getId());
        
        return authors;
    }
    
    public List<Book> getNewBooks(){
        newBooks = bookService.getNewBooks();

        return newBooks;
    }
    
    //Only for the actual month
    public List<Book> getBuyedBooks(){
        buyedBooks = bookService.getBestBookThisWeekOrg();
        
        return buyedBooks;
    }
    
    public List<Book> getMostRatedBooks(){
        List<Book> mostRatedBooks = null;
        
        return mostRatedBooks;
    }
    
    /**
     * @return the searchMsg
     */
    public String getSearchMsg() {
        return searchMsg;
    }
    
    /**
     * @param searchMsg the searchMsg to set
     */
    public void setSearchMsg(String searchMsg) {
        this.searchMsg = searchMsg;
    }
    
    
    /**
     * @return the someSearched
     */
    public boolean isSomeSearched() {
        return someSearched;
    }
    
    /**
     * @param someSearched the someSearched to set
     */
    public void setSomeSearched(boolean someSearched) {
        this.someSearched = someSearched;
    }
    
    /**
     * @return the searchRequest
     */
    public String getSearchRequest() {
        return searchRequest;
    }
    
    /**
     * @param searchRequest the searchRequest to set
     */
    public void setSearchRequest(String searchRequest) {
        this.searchRequest = searchRequest;
    }
    
    /**
     * @return the searchedBooks
     */
    public List<Book> getSearchedBooks() {
        return searchedBooks;
    }
    
    /**
     * @return the selectedCategory
     */
    public Category getSelectedCategory() {
        return selectedCategory;
    }
    
    /**
     * @param selectedCategory the selectedCategory to set
     */
    public void setSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
    }
    
    /**
     * @return the selectedAuthor
     */
    public Author getSelectedAuthor() {
        return selectedAuthor;
    }
    
    /**
     * @param selectedAuthor the selectedAuthor to set
     */
    public void setSelectedAuthor(Author selectedAuthor) {
        this.selectedAuthor = selectedAuthor;
    }
}
