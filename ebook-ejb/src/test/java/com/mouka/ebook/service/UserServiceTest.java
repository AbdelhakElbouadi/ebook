package com.mouka.ebook.service;

import com.mouka.ebook.entity.Administrator;
import com.mouka.ebook.entity.Author;
import com.mouka.ebook.entity.Customer;

import com.mouka.ebook.entity.Person;
import java.util.List;
import javax.ejb.EJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Abdelhak
 */
@RunWith(Arquillian.class)
public class UserServiceTest {

    @EJB
    UserService userService;
    
    @Deployment
    public static JavaArchive deploy(){
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class);
        jar.addPackages(true, "com.mouka.ebook");
        jar.addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml");
        jar.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.println(jar.toString(true));
        
        return jar;
    }
    
    @Ignore
    @Test
    public void test1_findByEmail(){
        Person p = userService.findByEmail("shao@mail.net");
        assertEquals(4L, p.getId().longValue());
        
        Person p1 = userService.findByEmail("shalalall");
        assertNull(p1);
    }
    
    @Ignore
    @Test
    public void test2_isItAUser(){
        Person p = userService.findById(1L);
        Person p1 = userService.findById(45L);
        Customer c = new Customer();
        Administrator m = new Administrator();
        
        assertTrue(userService.verifyUser(p));
        assertFalse(userService.verifyUser(p1));
        assertFalse(userService.verifyUser(c));
        assertFalse(userService.verifyUser(m)); 
    }
    
    @Ignore
    @Test
    public void test3_getUserRole(){
        Person p = userService.findById(1L);
        Person p1 = userService.findById(7L);
        Person p2 = userService.findById(11L);
        Person p3 = userService.findById(10L);
        
        assertEquals("Customer", userService.getUserRole(p));
        assertEquals("Customer", userService.getUserRole(p1));
        assertEquals("Admin", userService.getUserRole(p2));
        assertEquals("Admin", userService.getUserRole(p3));
    }
    
    
    @Test
    public void test4_getAuthorsByCategory(){
        List<Author> authors = userService.getAuthorsByCategory(2L);
        
        assertEquals(1, authors.size());
    }
}