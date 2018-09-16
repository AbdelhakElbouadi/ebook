package com.mouka.ebook.entity;

import com.mouka.ebook.entity.constraint.Email;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

/**
 *
 * @author Abdelhak
 */
@Entity
@Table(name = "author")
@Indexed
public class Author implements Identifiable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Field(index = Index.YES,analyze = Analyze.YES,store = Store.NO)
    @Analyzer(definition = "bookAnalyzer")
    private String fname;
    
    @Field(index = Index.YES,analyze = Analyze.YES,store = Store.NO)
    @Analyzer(definition = "bookAnalyzer")
    private String lname;
    
    @Email
    @Column(name="email", unique = true)
    private String email;
    
    @Column(name = "phone_home")
    private String phoneHome;
    
    @Column(name ="phone_work")
    private String phoneWork;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "authors")
    private List<Book> works = new ArrayList<>();
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "category_authors",
        joinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id"),   
        inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
    private Set<Category> categories =  new HashSet<>();
    
    public Author() {
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the fname
     */
    public String getFname() {
        return fname;
    }

    /**
     * @param fname the fname to set
     */
    public void setFname(String fname) {
        this.fname = fname;
    }

    /**
     * @return the lname
     */
    public String getLname() {
        return lname;
    }

    /**
     * @param lname the lname to set
     */
    public void setLname(String lname) {
        this.lname = lname;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
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
 
    
    public void addCategory(Category category){
        this.getCategories().add(category);
        category.getAuthors().add(this);
    }
    
    public void removeCategory(Category category){
        this.getCategories().remove(category);
        category.getAuthors().remove(this);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.id);
        hash = 37 * hash + Objects.hashCode(this.lname);
        hash = 37 * hash + Objects.hashCode(this.email);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Author other = (Author) obj;
        if (!Objects.equals(this.lname, other.lname)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
}
