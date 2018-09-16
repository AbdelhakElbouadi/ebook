package com.mouka.ebook.service;

import com.mouka.ebook.entity.Book;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Abdelhak
 */
@RunWith(Arquillian.class)
public class BookServiceTest {
    
    @EJB
    BookService bookService;
    
    @Deployment
    public static JavaArchive deploy(){
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class);
        jar.addPackages(true, "com.mouka.ebook");
        jar.addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml");
        jar.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.println(jar.toString(true));
        
        return jar;
    }
    
    @Test
    public void test1_getNewBooks(){
        List<Book> books = bookService.getNewBooks();
        
        //assertEquals(4,books.size());
    }
    
    @Test
    public void test2_getLastDayOfMonth(){
        
        assertEquals(31, bookService.getLastDayOfMonth(5, 2018));
        
        assertEquals(28, bookService.getLastDayOfMonth(2, 2018));
        
        assertEquals(30, bookService.getLastDayOfMonth(4, 2018));
    }
    
    @Test
    public void test3_getMostBuyed(){
        Map<Book,Integer> buyed = bookService.getMostByedBooksByMonth(10, 2018);
        
        assertEquals(2, buyed.size());
        assertEquals(3, buyed.get(0).intValue());
        assertEquals(5, buyed.get(1).intValue());
    }
    
    @Test
    public void test4_getGainForThisWeek(){
        BigDecimal gain = bookService.getGeneralGainForThisWeek();
        
        assertEquals(new BigDecimal(3914.69), gain);
    }
}
