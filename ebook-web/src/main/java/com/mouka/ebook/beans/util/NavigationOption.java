package com.mouka.ebook.beans.util;

/**
 *
 * @author Abdelhak
 */
public enum NavigationOption{
        WEEK("This Week Only  "), 
        MONTH("Per Month  "),
        CUSTOM("Custom Date  ");
        
        private String name;

        private NavigationOption(String name) {
            this.name = name;
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
          
    }