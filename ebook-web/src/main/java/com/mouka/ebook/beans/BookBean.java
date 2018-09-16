package com.mouka.ebook.beans;

import com.mouka.ebook.beans.util.JsfUtils;
import com.mouka.ebook.entity.Author;
import com.mouka.ebook.entity.Book;
import com.mouka.ebook.entity.Category;
import com.mouka.ebook.entity.CustomerOrder;
import com.mouka.ebook.entity.Image;
import com.mouka.ebook.entity.Product;
import com.mouka.ebook.entity.Publisher;
import com.mouka.ebook.exceptions.NonExistingEntityException;
import com.mouka.ebook.exceptions.PreExistingEntityException;
import com.mouka.ebook.service.AuthorService;
import com.mouka.ebook.service.BookService;
import com.mouka.ebook.service.CategoryService;
import com.mouka.ebook.service.PublisherService;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.inject.New;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.WordUtils;

/**
 *
 * @author Abdelhak
 */
@Named(value = "bookBean")
@ViewScoped
public class BookBean implements Serializable{
    
    @Inject
    private Conversation conversation;
    
    @EJB
    private BookService bookService;
    
    @EJB
    private PublisherService publisherService;
    
    @EJB
    private CategoryService categoryService;
    
    @EJB
    private AuthorService authorService;
    
    @Inject
    @New(value = Book.class)
    private Book book;
    
    @Inject
    @New(value = Publisher.class)
    private Publisher publisher;
    
    @Inject
    @New(value = CustomerOrder.class)
    private CustomerOrder order;
    
    private Long bookDetailsId;
    
    private boolean someSearched = false;
    private boolean editBook;
    private boolean deleteBook;
    
    private String searchRequest;
    
    private List<Book> searchedBooks = new ArrayList<>();
    
    private Part loadedFilePArt;
    
    @PostConstruct
    public void init(){
        book.getAuthors().add(null);
    }
    
    public String create(){
        try {
            saveImageFromFile();
            book = getBookService().create(book);
            JsfUtils.addSuccessMessage("New book created and added successfully!!");
        } catch (PreExistingEntityException ex) {
            Logger.getLogger(BookBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtils.addFailureMessage("Can't create new book");
        }
        
        return "/admin/book/View?bookId="+book.getId() + "&faces-redirect=true";
    }
    
    public String edit() {
        Book edited = null;
        try {
            edited = getBookService().edit(book);
            System.out.println("The New Edited Book ===>" + book.getTitle());
            JsfUtils.addSuccessMessage("Book updated successfully!!");
            setEditBook(false);
        } catch (NonExistingEntityException ex) {
            Logger.getLogger(BookBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtils.addFailureMessage("Can't update this book");
        }
        
        return "/admin/book/View?bookId=" + book.getId() + "&faces-redirect=true";
    }
    
    public String remove() {
        Book removed = null;
        
        try {
            removed = getBookService().remove(book.getId());
            JsfUtils.addSuccessMessage("Book with id " + book.getId() + " removed successfully!");
            setDeleteBook(false);
        } catch (NonExistingEntityException ex) {
            Logger.getLogger(BookBean.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtils.addSuccessMessage("Can't remove book with id " + book.getId() + ".");
        }
        
        return "/admin/index?faces-redirect=true";
    }
    
    private void saveImageFromFile(){
        Image image = new Image();
        
        try {
            String filename = loadedFilePArt.getSubmittedFileName();
            
            InputStream input = loadedFilePArt.getInputStream();
            byte[] imageData = IOUtils.toByteArray(input);
            image.setName(filename);
            image.setMimeType(loadedFilePArt.getContentType());
            image.setImageData(imageData);
            
            book.setImage(image);        
        } catch (IOException ex) {
            Logger.getLogger(BookBean.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
    
    public void viewBook(){
        book = findById(bookDetailsId);
    }
    
    public String refreshBookView(Long bookId){
        book = findById(bookId);
        setEditBook(true);
        setDeleteBook(true);
        
        return null;
    }
    
    public List<Book> getBestBookThisWeek(){
        List<Book> books = new ArrayList<>();
        
        List<Product> products = bookService.getBestBookThisWeek();
        for(Product p : products){
            books.add(bookService.findByIdWithDetails(p.getId()));
        }
        
        return books;
    }
    
    public List<Book> getBestBookThisMonth(){
        List<Book> books = new ArrayList<>();
        
        List<Product> products = bookService.getBestBookThisMonth();
        for(Product p : products){
            books.add(bookService.findByIdWithDetails(p.getId()));
        }
        
        return books;
    }
    
    public List<Book> getBestBookThisYear(){
        List<Book> books = new ArrayList<>();
        
        List<Product> products = bookService.getBestBookThisYear();
        for(Product p : products){
            books.add(bookService.findByIdWithDetails(p.getId()));
        }
        
        return books;
    }
    
    public String getBookTitle(Long id){
        Book book = bookService.findById(id);
        
        return WordUtils.capitalize(book.getTitle());
    }
    
    
    /**
     * @return the bookDetailsId
     */
    public Long getBookDetailsId() {
        return bookDetailsId;
    }

    /**
     * @param bookDetailsId the bookDetailsId to set
     */
    public void setBookDetailsId(Long bookDetailsId) {
        this.bookDetailsId = bookDetailsId;
    }
    
    public Book findById(Long id){
        Book found = getBookService().findById(id);
        
        return found;
    }
    
    public List<Book> findAll(){
        List<Book> books = getBookService().findAll();
        
        return books;
    }
    
    /**
     * @return the bookService
     */
    public BookService getBookService() {
        return bookService;
    }
    
    /**
     * @param bookService the bookService to set
     */
    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }
    
    /**
     * @return the publisherService
     */
    public PublisherService getPublisherService() {
        return publisherService;
    }
    
    /**
     * @param publisherService the publisherService to set
     */
    public void setPublisherService(PublisherService publisherService) {
        this.publisherService = publisherService;
    }
    
    /**
     * @return the book
     */
    public Book getBook() {
        return book;
    }
    
    /**
     * @param book the book to set
     */
    public void setBook(Book book) {
        this.book = book;
    }

    
    public List<Book> getAllBook(){
        List<Book> books = getBookService().findAll();

        return books;
    }
    
    public List<Publisher> getAllPublisher(){
        List<Publisher> publishers = publisherService.findAll();
        
        return  publishers;
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
     * @return the editBook
     */
    public boolean isEditBook() {
        return editBook;
    }

    /**
     * @param editBook the editBook to set
     */
    public void setEditBook(boolean editBook) {
        this.editBook = editBook;
    }

    /**
     * @return the deleteBook
     */
    public boolean isDeleteBook() {
        return deleteBook;
    }

    /**
     * @param deleteBook the deleteBook to set
     */
    public void setDeleteBook(boolean deleteBook) {
        this.deleteBook = deleteBook;
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
     * @param searchedBooks the searchedBooks to set
     */
    public void setSearchedBooks(List<Book> searchedBooks) {
        this.searchedBooks = searchedBooks;
    }

    /**
     * @return the loadedFilePArt
     */
    public Part getLoadedFilePArt() {
        return loadedFilePArt;
    }

    /**
     * @param loadedFilePArt the loadedFilePArt to set
     */
    public void setLoadedFilePArt(Part loadedFilePArt) {
        this.loadedFilePArt = loadedFilePArt;
    }
    
    public List<Category> getAllCategory(){
        List<Category> all = new ArrayList<>();
        all = categoryService.findAll();
        
        return all;
    }
    
    public List<Author> getAllAuthor(){
        List<Author> all = new ArrayList();
        all = authorService.findAll();
        
        return all;
    }
    
    public String addAuthor(){
        //Move from the view to the back up is a necessary
        if(conversation.getId() == null){
            conversation.begin();
        }
        book.getAuthors().add(null);
        
        return null;
    }
    
    public List<Author> autoCompleteAuthor(String query){
        List<Author> allAuthors = getAllAuthor();
        Collections.sort(allAuthors, new Comparator<Author>(){
            @Override
            public int compare(Author o1, Author o2) {
                return o1.getLname().compareToIgnoreCase(o2.getLname());
            }
        
        });
        List<Author> results = new ArrayList<>();
        
        for(Author a: allAuthors){
            if(a.getLname().toLowerCase().startsWith(query.toLowerCase()) 
                    || a.getFname().toLowerCase().startsWith(query.toLowerCase())){
                results.add(a);
            }
        }
        
        return results;
    }
    
    public String cancelChange(){
        FacesContext context = FacesContext.getCurrentInstance();
        Iterator<FacesMessage> it = context.getMessages();
        while(it.hasNext()){
            it.next();
            it.remove();
        }
        
        setEditBook(false);
        setDeleteBook(false);
        
        return null;
    }
    
}