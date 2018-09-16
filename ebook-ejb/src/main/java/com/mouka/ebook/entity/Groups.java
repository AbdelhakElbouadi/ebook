package com.mouka.ebook.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Abdelhak
 */
@Entity
@Table(name = "groups")
public class Groups implements Identifiable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;
    
    @Column(unique = true)
    private String name;
    private String description;

    public Groups() {
    }

    public Groups(String name, String description) {
        this.name = name;
        this.description = description;
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
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final Groups other = (Groups) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }    

}
