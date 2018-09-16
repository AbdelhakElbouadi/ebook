package com.mouka.ebook.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
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
@Indexed
@Table(name = "publisher")
@XmlRootElement
public class Publisher implements Identifiable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Field(index = Index.YES,analyze = Analyze.YES,store = Store.NO)
    @Analyzer(definition = "bookAnalyzer")
    private String name;
    
    @Column(name = "publisher_url")
    private String publisherUrl;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "publisher", orphanRemoval = true)
    private List<Book> books = new ArrayList<Book>();

    public Publisher() {
    }

    @Override
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the books
     */
    public List<Book> getBooks() {
        return books;
    }
    
    public void addBook(Book book){
        this.getBooks().add(book);
        book.setPublisher(this);
    }
    
    public void removeBook(Book book){
        this.getBooks().remove(book);
        book.setPublisher(null);
    }

    /**
     * @return the publisherUrl
     */
    public String getPublisherUrl() {
        return publisherUrl;
    }

    /**
     * @param publisherUrl the publisherUrl to set
     */
    public void setPublisherUrl(String publisherUrl) {
        this.publisherUrl = publisherUrl;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + Objects.hashCode(this.name);
        hash = 59 * hash + Objects.hashCode(this.publisherUrl);
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
        final Publisher other = (Publisher) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.publisherUrl, other.publisherUrl)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
}