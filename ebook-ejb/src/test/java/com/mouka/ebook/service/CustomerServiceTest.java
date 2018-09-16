package com.mouka.ebook.service;

import com.mouka.ebook.entity.Customer;
import javax.ejb.EJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Abdelhak
 */
@RunWith(Arquillian.class)
public class CustomerServiceTest {

    @EJB
    CustomerService customerService;
    
    @Deployment(testable = false)
    public static JavaArchive createDeploymentArchive(){
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
        Customer customer = customerService.findById(4L);
        Assert.assertEquals("shao@mail.net", customer.getEmail());
        Customer founded = customerService.findByEmail("shao@mail.net");
        Assert.assertEquals(4L, founded.getId().longValue());
        Assert.assertNull(customerService.findByEmail("shanana"));
    }
    
    @Ignore
    @Test
    public void test2_authenticateCustomer(){
        //Customer customer = customerService.findById(4L);
        Customer resulted = customerService.authenticateCustomer("shao@mail.net", "nouna");
        Customer noResult = customerService.authenticateCustomer("shao@mail.net", "daraaa");
        Customer fictional = customerService.authenticateCustomer("saba", "sabalmin");
        Assert.assertEquals(4L, resulted.getId().longValue());
        Assert.assertNull(noResult);
        Assert.assertNull(fictional);
    }
    
    @Ignore
    @Test
    public void test3_ignoreTest(){
        Assert.fail("#########Testing the failure with two feet is deadley");
    }
    
}