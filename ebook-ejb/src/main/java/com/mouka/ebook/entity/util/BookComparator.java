package com.mouka.ebook.entity.util;

import com.mouka.ebook.entity.Book;
import com.mouka.ebook.service.BookService;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Abdelhak
 */
public class BookComparator implements Comparator<Book>{

    
    private BookService bookService;

    public BookComparator() {   
        Context context;
        try {
            context = new InitialContext();
            bookService = (BookService) context.lookup("java:module/BookService!com.mouka.ebook.service.BookService");
        } catch (NamingException ex) {
            Logger.getLogger(BookComparator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Override
    public int compare(final Book o1, final Book o2) {
        if(bookService == null){
            System.out.println("The BookService is null");
        }
        System.out.println("The Book ID " + o1.getId() + "===" + o2.getId());
        double b1Rate = bookService.getBookRating(o1.getId());
        double b2Rate = bookService.getBookRating(o2.getId());
        
        if(b1Rate == b2Rate){
            return 0;
        }else if(b1Rate > b2Rate){
            return 1;
        }else{
            return -1;
        }    
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
    
}
