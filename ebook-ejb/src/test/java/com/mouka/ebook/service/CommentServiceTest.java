package com.mouka.ebook.service;

import com.mouka.ebook.entity.Comment;
import java.io.Serializable;
import java.util.List;
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
public class CommentServiceTest implements Serializable{
    
    @EJB
    ReviewService reviewService;
    
    @Ignore
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
    public void test0_getAllCommentOnProduct(){
        List<Comment> comments = reviewService.getAllCommentOnProduct(1L);
        Assert.assertEquals(5, comments.size());
        //Assert.assertEquals(3, reviewService.getAllCommentOnProduct(3L).size());
        Assert.assertNull(reviewService.getAllCommentOnProduct(3L));
    }
    
    @Ignore
    @Test
    public void test1_getCustomerRateOnProduct(){
        Assert.assertEquals(2, reviewService.getCustomerRateOnProduct(3L, 1L));
        Assert.assertEquals(2, reviewService.getCustomerRateOnProduct(5L, 1L));
        //Assert.assertEquals(5, reviewService.getCustomerRateOnProduct(6L, 1L));
        //Assert.assertEquals(5, reviewService.getCustomerRateOnProduct(8L, 2L));
    }
}
