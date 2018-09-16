package com.mouka.ebook.service;

import com.mouka.ebook.entity.Author;
import com.mouka.ebook.entity.Groups;
import com.mouka.ebook.entity.Person;
import com.mouka.ebook.entity.Person_;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Abdelhak
 */
@Stateless
public class UserService extends ServiceFacade<Person>{

    @PersistenceContext
    private EntityManager entityManager;

    public UserService() {
        super(Person.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    
    public boolean verifyUser(Person person){
        boolean yesItIs = false;
        if(person == null)
            return yesItIs;//It is not a user it is null
        String email = person.getEmail();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> query = builder.createQuery(Person.class);
        Root<Person> root = query.from(Person.class);
        query.select(root);
        query.where(builder.equal(root.get(Person_.email), email));
        
        List<Person> listOfUsers = entityManager.createQuery(query).getResultList();
        if(!listOfUsers.isEmpty()){
            yesItIs = true;
        }
        
        return yesItIs;
    }
    
    public Person verifyUserByEmail(String email){
        boolean answer = false;
        Person p = null;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> query = builder.createQuery(Person.class);
        Root<Person> root = query.from(Person.class);
        query.select(root);
        query.where(builder.equal(root.get(Person_.email), email));
        
        List<Person> listOfUsers = entityManager.createQuery(query).getResultList();
        if(!listOfUsers.isEmpty()){
            answer = true;
            p = listOfUsers.get(0);
        }
        
        return p;
    }
    
    public Person authenticateUser(String email,String password){
        Person p = null;
        
        p = findByEmail(email);
        if(p != null){
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Person> query = builder.createQuery(Person.class);
            Root<Person> root = query.from(Person.class);
            query.select(root);
            query.where(builder.and(
                    builder.equal(root.get(Person_.password), password),
                    builder.equal(root.get(Person_.email), email)
            ));
            
            List<Person> listOfUsers= entityManager.createQuery(query).getResultList();
            if(!listOfUsers.isEmpty()){
                p = listOfUsers.get(0);  
            }
        }
        
        return p;
    }
    
    public List<String> getUserRoleByEmail(String email){
        Person user = findByEmail(email);
        
        return getUserRole(user);
    }
    
    public List<String> getUserRole(Person person){
        List<String> roles = new ArrayList<>();
        if(verifyUser(person)){
            Set<Groups> groups = person.getGroups();
            for(Groups g : groups){
                roles.add(g.getName());
            }
        }
        
        return roles;
    }
    
    public String getUserPasswordByEmail(String email){
        String password = "";
        
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cQuery = builder.createQuery(String.class);
        Root<Person> root = cQuery.from(Person.class);
        cQuery.select(root.get(Person_.password));
        cQuery.where(builder.equal(root.get(Person_.email), email));
        
        TypedQuery<String> query = entityManager.createQuery(cQuery);
        password = query.getSingleResult();
        
        return password;
    }

    public Person findByEmail(String email){
        Person person  = null;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> query = builder.createQuery(Person.class);
        Root<Person> root = query.from(Person.class);
        query.select(root);
        query.where(builder.equal(root.get(Person_.email), email));
        List<Person> listOfUsers = entityManager.createQuery(query).getResultList();
        if(!listOfUsers.isEmpty()){
            person = listOfUsers.get(0);
        }
        
        return person;
    }
    
    public List<Author> getAuthorsByCategory(Long id){
        List<Author> authors = null;
        //JPQL
        Query q = entityManager.createQuery("SELECT a FROM Author a, IN(a.categories) c  "
                + "WHERE c.id=:id");
        q.setParameter("id", id);
        authors = q.getResultList();
        
        return authors;
    }

}