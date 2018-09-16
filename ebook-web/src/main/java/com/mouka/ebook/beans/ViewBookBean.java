package com.mouka.ebook.beans;

import com.mouka.ebook.entity.Book;
import com.mouka.ebook.entity.Comment;
import com.mouka.ebook.entity.Customer;
import com.mouka.ebook.entity.Rate;
import com.mouka.ebook.service.BookService;
import com.mouka.ebook.service.ReviewService;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.New;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.text.WordUtils;

/**
 *
 * @author Abdelhak
 */
@Named
@ViewScoped
public class ViewBookBean implements Serializable{
    
    @EJB
    private BookService bookService;
    
    @EJB
    private ReviewService reviewService;
    
    @Inject
    @New(value = Book.class)
    private Book book;
    
    @Inject
    @New(value = Comment.class)
    private Comment comment;
    
    @Inject
    @New(value = Rate.class)
    private Rate rate;
    
    private Long bookToViewId;
    
    private Integer bookRating;
    private String bookImagePath = "";
    private byte[] imageArray;
    
    public ViewBookBean(){        
    }
    
    @PostConstruct
    public void init(){
        FacesContext context = FacesContext.getCurrentInstance();
        bookToViewId = Long.parseLong(context.getExternalContext().getRequestParameterMap().get("bookId"));
        viewBook();
        bookImagePath = getBookFrontImage(book);
    }
    
    public void viewBook(){
        setBook(getBookService().findByIdWithDetails(getBookToViewId()));
    }
    
    public String getBookFrontImage(Book book){
        String path = "";
        if(book.getImage() != null){
            path = "/image/" + book.getImage().getId();
        }else {
            path = "/resources/default/images/bookico.jpg";
        }
        
        return path;
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

    /**
     * @return the comment
     */
    public Comment getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(Comment comment) {
        this.comment = comment;
    }

    /**
     * @return the rate
     */
    public Rate getRate() {
        return rate;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate(Rate rate) {
        this.rate = rate;
    }

    /**
     * @return the bookToViewId
     */
    public Long getBookToViewId() {
        return bookToViewId;
    }

    /**
     * @param bookToViewId the bookToViewId to set
     */
    public void setBookToViewId(Long bookToViewId) {
        this.bookToViewId = bookToViewId;
    }

    /**
     * @return the bookRating
     */
    public Integer getBookRating() {
        setBookRating((int)bookService.getBookRating(book.getId()));
        return bookRating;
    }

    /**
     * @param bookRating the bookRating to set
     */
    public void setBookRating(Integer bookRating) {
        this.bookRating = bookRating;
    }

    /**
     * @return the bookImagePath
     */
    public String getBookImagePath() {
        return bookImagePath;
    }

    /**
     * @param bookImagePath the bookImagePath to set
     */
    public void setBookImagePath(String bookImagePath) {
        this.bookImagePath = bookImagePath;
    }

    /**
     * @return the imageArray
     */
    public byte[] getImageArray() {
        return imageArray;
    }

    /**
     * @param imageArray the imageArray to set
     */
    public void setImageArray(byte[] imageArray) {
        this.imageArray = imageArray;
    }
    
    public String capitalize(String title){
        String newTitle = "";
        newTitle  = WordUtils.capitalize(title);
        
        return newTitle;
    }
    
    public List<Comment> getAllCommentOnBook(Long id){
        List<Comment> comments = null;
        
        comments = reviewService.getAllCommentOnProduct(id);
        
        return comments;
    }
    
    public int getCustomerRateOnProduct(Long customerId, Long productId){
        int rate = -1;
        
        rate = reviewService.getCustomerRateOnProduct(customerId, productId);
        return rate;
    }
    
    public String addCustomerReview(Customer actualCustomer){
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        rate.setDateCreated(date);
        rate.setReviewedProduct(book);
        rate.setReviewer(actualCustomer);
        reviewService.addCustomerRate(rate);
        
        comment.setDateCreated(date);
        comment.setReviewedProduct(book);
        comment.setReviewer(actualCustomer);
        reviewService.addCustomerComment(comment);
        
        return null;
    }

}