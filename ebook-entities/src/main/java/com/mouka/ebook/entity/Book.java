package com.mouka.ebook.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;




/**
 *
 * @author mmeh4
 */
@Entity
@Indexed
@AnalyzerDef(name = "bookAnalyzer",
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
        filters = {
            @TokenFilterDef(factory = LowerCaseFilterFactory.class),
            @TokenFilterDef(factory = SnowballPorterFilterFactory.class)
        }
)
@Table(name = "book")
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
@XmlRootElement
public class Book extends Product {
    
    @NotNull
    @Size(min = 1, max = 50)
    @Field(index = org.hibernate.search.annotations.Index.YES, analyze = Analyze.YES,
            store = org.hibernate.search.annotations.Store.NO)
    @Analyzer(definition = "bookAnalyzer")
    private String title;
    
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;
    
    @NotNull
    @Size(min = 1, max = 25)
    @Field(index = org.hibernate.search.annotations.Index.YES, analyze = Analyze.YES, 
            store = org.hibernate.search.annotations.Store.NO)
    @Analyzer(definition = "bookAnalyzer")
    private String isbn;

    @Lob
    private byte[] image;
    
    @ManyToOne(fetch = FetchType.EAGER, 
            cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "publisher_id")
    @IndexedEmbedded
    private Publisher publisher;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "book_authors",
            joinColumns = {@JoinColumn(name = "book_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id", referencedColumnName = "id")})
    @IndexedEmbedded
    private Set<Author> authors = new HashSet<>();

    public Book() {
    }
    
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(Category category) {
        this.category = category;
    }
    
    /**
     * @return the isbn
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * @param isbn the isbn to set
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }


    /**
     * @return the publisher
     */
    public Publisher getPublisher() {
        return publisher;
    }

    /**
     * @param publisher the publisher to set
     */
    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    /**
     * @return the image
     */
    public byte[] getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(byte[] image) {
        this.image = image;
    }

    /**
     * @return the authors
     */
    public Set<Author> getAuthors() {
        return authors;
    }

//    /**
//     * @param authors the authors to set
//     */
//    public void setAuthors(List<Author> authors) {
//        this.authors = authors;
//    }
    
    
    public void addAuthor(Author author){
        this.getAuthors().add(author);
        author.getWorks().add(this);
    }
    
    public void removeAuthor(Author author){
        this.getAuthors().remove(author);
        author.getWorks().remove(this);
    }


}