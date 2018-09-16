package com.mouka.ebook.entity;

import com.mouka.ebook.entity.constraint.Email;
import java.util.HashSet;
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
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.DiscriminatorOptions;
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
@Table(name = "Users")
@DiscriminatorOptions(force = true)
public class Person implements Identifiable{

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
    
    @XmlTransient
    private String password;
    
    @ManyToMany(fetch = FetchType.EAGER, 
            cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinTable(name="user_group",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="group_id", referencedColumnName = "id"))
    private Set<Groups> groups =  new HashSet<>();

    public Person() {
    }

    public Person(String fname, String lname, String email, String password) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
    }


    /**
     * @return the groups
     */
    public Set<Groups> getGroups() {
        return groups;
    }
    
    public void addGroup(Groups group){
        this.getGroups().add(group);
    }
    
    public void removeGroup(Groups group){
        this.getGroups().remove(group);
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
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.id);
        hash = 83 * hash + Objects.hashCode(this.email);
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
        final Person other = (Person) obj;
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }  

}
