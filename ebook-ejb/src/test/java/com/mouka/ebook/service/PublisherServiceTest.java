package com.mouka.ebook.service;

import com.mouka.ebook.entity.Publisher;
import javax.ejb.EJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Abdelhak
 */
//@RunWith(Arquillian.class)
public class PublisherServiceTest {

    @EJB
    PublisherService publisherService;
    
    @Ignore
    @Deployment(testable = false)
    public static JavaArchive createDeployment(){
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class);
        jar.addAsManifestResource("META-INF/persistence.xml", "persistence.xml");
        jar.addPackages(true, "com.mouka.ebook");
        jar.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        //System.out.println(jar.toString(true));
        
        return jar;
    }
    
    @Ignore
    @Test
    public void testFindById(){
        Assert.assertNotNull(publisherService);
        Publisher publisher = publisherService.findById(2L);
        Assert.assertEquals("nadeem", publisher.getName());
        Assert.assertEquals("http://nadeem.net", publisher.getPublisherUrl());
    }
    @Ignore
    @Test
    public void testFailure(){
        Assert.fail("I never want you to fail just haven't enough time for the battle");
    }
}