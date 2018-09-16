package com.mouka.ebook.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
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
 * @author Abdelhak
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
    
    @Column(name = "title", nullable = false)
    @Size(min = 1, max = 50)
    @Field(index = org.hibernate.search.annotations.Index.YES, analyze = Analyze.YES,
            store = org.hibernate.search.annotations.Store.NO)
    @Analyzer(definition = "bookAnalyzer")
    private String title;
    
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @IndexedEmbedded
    private Category category;
    
    @Column(name = "isbn", nullable = false)
    @Size(min = 1, max = 25)
    @Field(index = org.hibernate.search.annotations.Index.YES, analyze = Analyze.YES,
            store = org.hibernate.search.annotations.Store.NO)
    @Analyzer(definition = "bookAnalyzer")
    private String isbn;
    
    @OneToOne(fetch = FetchType.EAGER ,cascade = {CascadeType.MERGE,CascadeType.REFRESH}, orphanRemoval = true)
    @JoinColumn(name = "img_id", referencedColumnName = "id")
    private Image image;
    
    @ManyToOne(fetch = FetchType.EAGER,
            cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "publisher_id")
    @IndexedEmbedded
    private Publisher publisher;
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "book_authors",
            joinColumns = {@JoinColumn(name = "book_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id", referencedColumnName = "id")})
    @IndexedEmbedded
    private List<Author> authors = new ArrayList<>();
    
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
     * @return the images
     */
    public Image getImage() {
        return image;
    }
    
    /**
     * @param image the image to set
     */
    public void setImage(Image image) {
        this.image = image;
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
     * @return the authors
     */
    public List<Author> getAuthors() {
        return authors;
    }
    
    
    public void addAuthor(Author author){
        this.getAuthors().add(author);
        author.getWorks().add(this);
    }
    
    public void removeAuthor(Author author){
        this.getAuthors().remove(author);
        author.getWorks().remove(this);
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.title);
        hash = 53 * hash + Objects.hashCode(this.isbn);
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
        final Book other = (Book) obj;
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.isbn, other.isbn)) {
            return false;
        }
        return true;
    }
    
}