package com.mouka.ebook.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 *
 * @author nasser
 */
@Entity
public class Author extends Person{

    @Column(name = "phone_home")
    private String phoneHome;
    
    @Column(name ="phone_work")
    private String phoneWork;
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "authors")
    private List<Book> works;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "category_authors",
        joinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id"),   
        inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
    private Set<Category> categories =  new HashSet<>();
    
    public Author() {
        works = new ArrayList<>();
    }

    /**
     * @return the phoneHome
     */
    public String getPhoneHome() {
        return phoneHome;
    }

    /**
     * @param phoneHome the phoneHome to set
     */
    public void setPhoneHome(String phoneHome) {
        this.phoneHome = phoneHome;
    }

    /**
     * @return the phoneWork
     */
    public String getPhoneWork() {
        return phoneWork;
    }

    /**
     * @param phoneWork the phoneWork to set
     */
    public void setPhoneWork(String phoneWork) {
        this.phoneWork = phoneWork;
    }

    /**
     * @return the works
     */
    public List<Book> getWorks() {
        return works;
    }

//    /**
//     * @param works the works to set
//     */
//    public void setWorks(List<Book> works) {
//        this.works = works;
//    }
    
    public void addBook(Book book){
        this.getWorks().add(book);
        book.getAuthors().add(this);
    }
    
    
    public void removeBook(Book book){
        this.getWorks().remove(book);
        book.getAuthors().remove(this);
    }

    /**
     * @return the categories
     */
    public Set<Category> getCategories() {
        return categories;
    }

    /**
     * @param categories the categories to set
     */
    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
    
}
