package com.mouka.ebook.service;

import com.mouka.ebook.entity.Author;
import com.mouka.ebook.entity.Author_;
import com.mouka.ebook.entity.Book;
import com.mouka.ebook.entity.BookReportData;
import com.mouka.ebook.entity.Book_;
import com.mouka.ebook.entity.Category;
import com.mouka.ebook.entity.CustomerOrder;
import com.mouka.ebook.entity.CustomerOrder_;
import com.mouka.ebook.entity.OrderItem;
import com.mouka.ebook.entity.OrderItem_;
import com.mouka.ebook.entity.Product;
import com.mouka.ebook.entity.Product_;
import com.mouka.ebook.entity.Rate;
import com.mouka.ebook.entity.Rate_;
import com.mouka.ebook.entity.util.OrderStatus;
import com.mouka.ebook.exceptions.PreExistingEntityException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;


/**
 *
 * @author Abdelhak
 */
@Stateless
public class BookService extends ServiceFacade<Book>{
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public BookService() {
        super(Book.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    
    public Book create(Book entity) throws PreExistingEntityException{
        if(entity.getId() != null && findById(entity.getId()) != null){
            throw new PreExistingEntityException("The entity with id " + entity.getId()
             + " exist already.");
        }
        entityManager.persist(entity.getImage());
        entityManager.flush();
        entityManager.persist(entity);
        entityManager.flush();
        
        return entity;
    }
    
    public Book findByIdWithDetails(Long id){
        Book book = null;
        book = entityManager.find(Book.class, id);
        book.getAuthors().size();
        
        return book;
    }
    
    public List<Book> getAllBook(){
        return super.findAll();
    }
    
    public List<Book> getBookByTitle(String title){
        List<Book> books = null;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> bookQuery = builder.createQuery(Book.class);
        Root<Book> book = bookQuery.from(Book.class);
        bookQuery.select(book);
        bookQuery.where(builder.like(book.get(Book_.title), title));
        
        TypedQuery<Book> query = entityManager.createQuery(bookQuery);
        books = query.getResultList();
        
        
        return books;
    }
    
    public List<Book> getBookByAuthor(String name){
        List<Book> books = null;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> bookQuery = builder.createQuery(Book.class);
        Root<Book> book = bookQuery.from(Book.class);
        Join<Book,Author> join = book.join(Book_.authors);
        
        bookQuery.select(book);
        bookQuery.where(builder.or(builder.like(join.get(Author_.fname),name),
                builder.like(join.get(Author_.lname),name)));
        TypedQuery<Book> query = entityManager.createQuery(bookQuery);
        books = query.getResultList();
        
        return books;
    }
    
    public List<Book> makeGeneralSearch(String query){
        
        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);
        try {
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException ex) {
            Logger.getLogger(BookService.class.getName()).log(Level.SEVERE, null, ex);
        }
        QueryBuilder qb = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(Book.class).get();
        org.apache.lucene.search.Query q = qb
                .keyword()
                .onFields("title", "description", "authors.lname","authors.fname","publisher.name")
                .matching(query)
                .createQuery();
        Query jpaQuery = fullTextEntityManager.createFullTextQuery(q, Book.class);
        List<Book> books = jpaQuery.getResultList();
        
        for(Book b : books){
            b.getAuthors().size();
            //Hibernate.initialize(b.getAuthors().size());
        }
        
        
        return books;
    }
    
    public double getBookRating(Long id){
        double rate = 0;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Rate> rateQuery = builder.createQuery(Rate.class);
        Root<Rate> root = rateQuery.from(Rate.class);
        rateQuery.select(root);
        rateQuery.where(builder.equal(root.get(Rate_.reviewedProduct), id));
        List<Rate> rates = entityManager.createQuery(rateQuery).getResultList();
        if(!rates.isEmpty()){
            for(Rate r : rates){
                rate += r.getRate();
            }
            rate = Math.floor(rate/rates.size());
        }
        
        return rate;
    }
    
    public List<Book> getNewBooks(){
        List<Book> books = null;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> bookQuery = builder.createQuery(Book.class);
        Root<Book> book = bookQuery.from(Book.class);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -10);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        
        Date tenDaysAgo= calendar.getTime();
        bookQuery.select(book);
        bookQuery.where(builder.greaterThan(book.get(Book_.lastUpdated), tenDaysAgo));
        
        TypedQuery<Book> query = entityManager.createQuery(bookQuery);
        books = query.getResultList();
        
        for(Book b : books){
            b.getAuthors().size();
            //Hibernate.initialize(b.getAuthors().size());
        }
        
        return books;
    }
    
    
    public Map<Book,Integer> getMostByedBooksByMonth(int month, int year){
        List<Book> books = null;
        List<Integer> orderItemQuantity = null;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        
        CriteriaQuery<Integer> orderItemQuery = builder.createQuery(Integer.class);
        CriteriaQuery<Book> bookQuery = builder.createQuery(Book.class);
        
        Root<OrderItem> orderItem = orderItemQuery.from(OrderItem.class);
        Root<Book> book = bookQuery.from(Book.class);
        
        Path<CustomerOrder> orderJoin = orderItem.join(OrderItem_.order);
        orderJoin.get(CustomerOrder_.dateCreated);
        
        Path<Product> productJoin = orderItem.join(OrderItem_.orderedProduct);
        productJoin.get(Product_.id);
        
        //Getting the first and last date of this month.
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date fdate = calendar.getTime();
        
        calendar.set(Calendar.DAY_OF_MONTH, getLastDayOfMonth(month, year));
        Date ldate = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        
        //Get the book<> first.
        bookQuery.select(book);
        bookQuery.where(builder.and(builder.between(orderJoin.get(CustomerOrder_.dateCreated), fdate, ldate),
                builder.equal(orderJoin.get(CustomerOrder_.orderStatus), "Complete")));
        bookQuery.groupBy(book.get(Book_.id));
        books = entityManager.createQuery(bookQuery).getResultList();
        
        //Get the quantity of the book<> second.
        orderItemQuery.select(builder.sum(orderItem.get(OrderItem_.quantity)));
        orderItemQuery.where(builder.and(builder.between(orderJoin.get(CustomerOrder_.dateCreated), fdate, ldate),
                builder.equal(orderJoin.get(CustomerOrder_.orderStatus), "Complete")));
        orderItemQuery.groupBy(productJoin.get(Product_.id));
        orderItemQuantity = entityManager.createQuery(orderItemQuery).getResultList();
        
        //Dump data into a map for return.
        Map<Book, Integer> bookQuantity = new HashMap<>();
        for(int i=0; i < books.size(); i++){
            bookQuantity.put(books.get(i), orderItemQuantity.get(i));
        }
        
        return bookQuantity;
    }
    
    
    public List<Product> getBestBookThisWeek(){
        List<Product> books = new ArrayList<>();
        Date firstDayOfWeek = null, lastDayOfWeek = null;
        //Get the Order Of This Week 
        firstDayOfWeek = getFirstDayOfThisWeek();
        lastDayOfWeek = getLastDayOfThisWeek();
        
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderItem> cQuery = builder.createQuery(OrderItem.class);
        Root<OrderItem> root = cQuery.from(OrderItem.class);
        Path<CustomerOrder> pathItemOrder = root.join(OrderItem_.order);
        cQuery.select(root);
        cQuery.where(builder.and(builder.equal(pathItemOrder.get(CustomerOrder_.orderStatus), OrderStatus.Complete),
                builder.between(pathItemOrder.get(CustomerOrder_.dateCreated), firstDayOfWeek, lastDayOfWeek)));
        
        TypedQuery<OrderItem> query = entityManager.createQuery(cQuery);
        List<OrderItem> items = query.getResultList();
        Map<Product, Integer> productQuantity = new HashMap<>();
        //Get The Product Count For Each OrderItem.
        for(OrderItem item : items){
            Product p = item.getOrderedProduct();
            int count = item.getQuantity();
            productQuantity.put(p, (productQuantity.get(p) == null ? new Integer(0) : productQuantity.get(p)) + count);
        }
        //Order The Map In Ascending Order.
        Set<Entry<Product, Integer>> entrySet = productQuantity.entrySet();
        List<Entry<Product, Integer>> list = new ArrayList<>(entrySet);
        Collections.sort(list, new Comparator<Entry<Product, Integer>>(){
            @Override
            public int compare(Entry<Product, Integer> o1, Entry<Product, Integer> o2) {
                if(o1.getValue() > o2.getValue()){
                    return 1;
                }else if(o1.getValue() < o2.getValue()){
                    return -1;
                }else{
                    return 0;
                }
            }
        });
        
        int best = (list.size() > 100 ? 100 : list.size());
        List<Entry<Product, Integer>> bestHundred = list.subList(0, best);
        //Get Only The Product.
        for(Entry<Product, Integer> entry : bestHundred){
            books.add(entry.getKey());
        }
        
        return books;
    }
    
    
    public List<Book> getBestBookThisWeekOrg(){
        List<Book> books = new ArrayList<>();
        Date firstDayOfWeek = null, lastDayOfWeek = null;
        //Get the Order Of This Week 
        firstDayOfWeek = getFirstDayOfThisWeek();
        lastDayOfWeek = getLastDayOfThisWeek();
        
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderItem> cQuery = builder.createQuery(OrderItem.class);
        Root<OrderItem> root = cQuery.from(OrderItem.class);
        Path<CustomerOrder> pathItemOrder = root.join(OrderItem_.order);
        cQuery.select(root);
        cQuery.where(builder.and(builder.equal(pathItemOrder.get(CustomerOrder_.orderStatus), OrderStatus.Complete),
                builder.between(pathItemOrder.get(CustomerOrder_.dateCreated), firstDayOfWeek, lastDayOfWeek)));
        
        TypedQuery<OrderItem> query = entityManager.createQuery(cQuery);
        List<OrderItem> items = query.getResultList();
        Map<Book, Integer> productQuantity = new HashMap<>();
        //Get The Product Count For Each OrderItem.
        for(OrderItem item : items){
            Product p = item.getOrderedProduct();
            Book b = findById(p.getId());
            int count = item.getQuantity();
            productQuantity.put(b, (productQuantity.get(b) == null ? new Integer(0) : productQuantity.get(b)) + count);
        }
        //Order The Map In Ascending Order.
        Set<Entry<Book, Integer>> entrySet = productQuantity.entrySet();
        List<Entry<Book, Integer>> list = new ArrayList<>(entrySet);
        Collections.sort(list, new Comparator<Entry<Book, Integer>>(){
            @Override
            public int compare(Entry<Book, Integer> o1, Entry<Book, Integer> o2) {
                if(o1.getValue() > o2.getValue()){
                    return 1;
                }else if(o1.getValue() < o2.getValue()){
                    return -1;
                }else{
                    return 0;
                }
            }
        });
        
        int best = (list.size() > 100 ? 100 : list.size());
        List<Entry<Book, Integer>> bestHundred = list.subList(0, best);
        //Get Only The Product.
        for(Entry<Book, Integer> entry : bestHundred){
            books.add(entry.getKey());
        }
        
        return books;
    }
    
    private Date getFirstDayOfThisWeek(){
        Date date = null;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        int today = calendar.get(Calendar.DAY_OF_WEEK);
        switch(today){
            case Calendar.SUNDAY:
                date = calendar.getTime();
                break;
            case Calendar.MONDAY:
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                date = calendar.getTime();
                break;
            case Calendar.TUESDAY:
                calendar.add(Calendar.DAY_OF_MONTH, -2);
                date = calendar.getTime();
                break;
            case Calendar.WEDNESDAY:
                calendar.add(Calendar.DAY_OF_MONTH, -3);
                date = calendar.getTime();
                break;
            case Calendar.THURSDAY:
                calendar.add(Calendar.DAY_OF_MONTH, -4);
                date = calendar.getTime();
                break;
            case Calendar.FRIDAY:
                calendar.add(Calendar.DAY_OF_MONTH, -5);
                date = calendar.getTime();
                break;
            case Calendar.SATURDAY:
                calendar.add(Calendar.DAY_OF_MONTH, -6);
                date = calendar.getTime();
                break;
        }
        
        return date;
    }
    
    private Date getLastDayOfThisWeek(){
        Date date = null;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        int today = calendar.get(Calendar.DAY_OF_WEEK);
        
        switch(today){
            case Calendar.SUNDAY:
                calendar.add(Calendar.DAY_OF_MONTH, 6);
                date = calendar.getTime();
                break;
            case Calendar.MONDAY:
                calendar.add(Calendar.DAY_OF_MONTH, 5);
                date = calendar.getTime();
                break;
            case Calendar.TUESDAY:
                calendar.add(Calendar.DAY_OF_MONTH, 4);
                date = calendar.getTime();
                break;
            case Calendar.WEDNESDAY:
                calendar.add(Calendar.DAY_OF_MONTH, 3);
                date = calendar.getTime();
                break;
            case Calendar.THURSDAY:
                calendar.add(Calendar.DAY_OF_MONTH, 2);
                date = calendar.getTime();
                break;
            case Calendar.FRIDAY:
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                date = calendar.getTime();
                break;
            case Calendar.SATURDAY:
                date = calendar.getTime();
                break;
        }
        
        return date;
    }
    
    
    public List<Product> getBestBookThisMonth(){
        List<Product> books = new ArrayList<>();
        Date firstDayOfMonth = null, lastDayOfMonth = null;
        
        //Get the first and last date of this month.
        Calendar calendar = Calendar.getInstance();
        int minMonth =  calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int maxMonth =  calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, minMonth);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        firstDayOfMonth = calendar.getTime();
        
        calendar.set(Calendar.DAY_OF_MONTH, maxMonth);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        lastDayOfMonth = calendar.getTime();
        
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderItem> cQuery = builder.createQuery(OrderItem.class);
        Root<OrderItem> root = cQuery.from(OrderItem.class);
        Path<CustomerOrder> pathItemOrder = root.join(OrderItem_.order);
        cQuery.select(root);
        cQuery.where(builder.and(builder.equal(pathItemOrder.get(CustomerOrder_.orderStatus), OrderStatus.Complete),
                builder.between(pathItemOrder.get(CustomerOrder_.dateCreated), firstDayOfMonth, lastDayOfMonth)));
        
        TypedQuery<OrderItem> query = entityManager.createQuery(cQuery);
        List<OrderItem> items = query.getResultList();
        Map<Product, Integer> productQuantity = new HashMap<>();
        //Get The Product Count For Each OrderItem.
        for(OrderItem item : items){
            Product p = item.getOrderedProduct();
            int count = item.getQuantity();
            productQuantity.put(p, (productQuantity.get(p) == null ? new Integer(0) : productQuantity.get(p)) + count);
        }
        //Order The Map In Ascending Order.
        Set<Entry<Product, Integer>> entrySet = productQuantity.entrySet();
        List<Entry<Product, Integer>> list = new ArrayList<>(entrySet);
        Collections.sort(list, new Comparator<Entry<Product, Integer>>(){
            @Override
            public int compare(Entry<Product, Integer> o1, Entry<Product, Integer> o2) {
                if(o1.getValue() > o2.getValue()){
                    return 1;
                }else if(o1.getValue() < o2.getValue()){
                    return -1;
                }else{
                    return 0;
                }
            }
        });
        
        int best = (list.size() > 100 ? 100 : list.size());
        List<Entry<Product, Integer>> bestHundred = list.subList(0, best);
        //Get Only The Product.
        for(Entry<Product, Integer> entry : bestHundred){
            books.add(entry.getKey());
        }

        return books;
    }
    
    public List<Product> getBestBookThisYear(){
        List<Product> books = new ArrayList<>();
        Date firstDayOfYear = null, lastDayOfYear = null;
        
        //Get the first and last date of this month.
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        firstDayOfYear = calendar.getTime();
        
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        lastDayOfYear = calendar.getTime();
        
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderItem> cQuery = builder.createQuery(OrderItem.class);
        Root<OrderItem> root = cQuery.from(OrderItem.class);
        Path<CustomerOrder> pathItemOrder = root.join(OrderItem_.order);
        cQuery.select(root);
        cQuery.where(builder.and(builder.equal(pathItemOrder.get(CustomerOrder_.orderStatus), OrderStatus.Complete),
                builder.between(pathItemOrder.get(CustomerOrder_.dateCreated), firstDayOfYear, lastDayOfYear)));
        
        TypedQuery<OrderItem> query = entityManager.createQuery(cQuery);
        List<OrderItem> items = query.getResultList();
        Map<Product, Integer> productQuantity = new HashMap<>();
        //Get The Product Count For Each OrderItem.
        for(OrderItem item : items){
            Product p = item.getOrderedProduct();
            int count = item.getQuantity();
            productQuantity.put(p, (productQuantity.get(p) == null ? new Integer(0) : productQuantity.get(p)) + count);
        }
        //Order The Map In Ascending Order.
        Set<Entry<Product, Integer>> entrySet = productQuantity.entrySet();
        List<Entry<Product, Integer>> list = new ArrayList<>(entrySet);
        Collections.sort(list, new Comparator<Entry<Product, Integer>>(){
            @Override
            public int compare(Entry<Product, Integer> o1, Entry<Product, Integer> o2) {
                if(o1.getValue() > o2.getValue()){
                    return 1;
                }else if(o1.getValue() < o2.getValue()){
                    return -1;
                }else{
                    return 0;
                }
            }
        });
        
        int best = (list.size() > 100 ? 100 : list.size());
        List<Entry<Product, Integer>> bestHundred = list.subList(0, best);
        //Get Only The Product.
        for(Entry<Product, Integer> entry : bestHundred){
            books.add(entry.getKey());
        }

        return books;
    }
    
    public BigDecimal getGeneralGainForThisWeek(){
        BigDecimal gain = new BigDecimal(0);
        Date first = getFirstDayOfThisWeek(), last = getLastDayOfThisWeek();
        System.out.println("FirstDayWeek ===>" + first.toString() + " LastDayWeek ===>" + last.toString());
        
        String ql = "SELECT SUM(oi.quantity * p.price) "
                + "FROM OrderItem oi,IN(oi.orderedProduct) p, IN(oi.order) o " 
                + "WHERE o.orderStatus= :status AND (o.dateCreated BETWEEN :first AND :last)";
        Query query = entityManager.createQuery(ql);
        query.setParameter("status", OrderStatus.Complete);
        query.setParameter("first", first);
        query.setParameter("last",last);
        
        if(query.getSingleResult() != null){
            gain = new BigDecimal((double)query.getSingleResult());
        }

        return gain;
    }
    
    public List<BookReportData> getBookGainForThisWeek(Category cat){
        List<BookReportData> reports = null;
        Date first = getFirstDayOfThisWeek(), last = getLastDayOfThisWeek();
        String ql = "";
        
        if(cat ==null){
            ql = "SELECT NEW BookReportData(p.id, SUM(oi.quantity), SUM(oi.quantity * p.price))  "
                + "FROM OrderItem oi, IN(oi.orderedProduct) p, IN(oi.order) o " 
                + "WHERE o.orderStatus= :status AND (o.dateCreated BETWEEN :first AND :last) "
                + "GROUP BY p.id";
        }else{
            ql = "SELECT NEW BookReportData(p.id, SUM(oi.quantity), SUM(oi.quantity * p.price))  "
                + "FROM OrderItem oi, IN(oi.orderedProduct) p INNER JOIN Book b on p.id=b.id ,IN(oi.order) o " 
                + "WHERE o.orderStatus= :status AND (o.dateCreated BETWEEN :first AND :last) "
                + "AND  b.category = :category "
                + "GROUP BY p.id";
        }
        
        Query query = entityManager.createQuery(ql);
        query.setParameter("status", OrderStatus.Complete);
        query.setParameter("first", first);
        query.setParameter("last", last);
        if(cat != null)
            query.setParameter("category", cat);
        
        reports = query.getResultList();
        
        
        return reports;
    }
    
    public BigDecimal getGeneralGainForRange(Date from, Date to){
        BigDecimal gain = new BigDecimal(0.00);
        String ql = "SELECT SUM(oi.quantity * p.price) FROM OrderItem oi,IN(oi.orderedProduct) p, IN(oi.order) o " +
                    "WHERE o.orderStatus= :status AND (o.dateCreated BETWEEN :first AND :last)";
        Query query = entityManager.createQuery(ql);
        query.setParameter("status", OrderStatus.Complete);
        query.setParameter("first", from);
        query.setParameter("last",to);
        
        if(query.getSingleResult() != null){
            gain = new BigDecimal((double)query.getSingleResult());
        }
        
        return gain;
    }
    
    public List<BookReportData> getGainForRange(Date from, Date to, Category cat){
        List<BookReportData> reports = null;
        String ql = "";
        
        if(cat == null){
            ql = "SELECT NEW BookReportData(p.id, SUM(oi.quantity), SUM(oi.quantity * p.price))  "
                + "FROM OrderItem oi, IN(oi.orderedProduct) p, IN(oi.order) o " 
                + "WHERE o.orderStatus= :status AND (o.dateCreated BETWEEN :first AND :last) "
                + "GROUP BY p.id";
        }else{
            ql = "SELECT NEW BookReportData(p.id, SUM(oi.quantity), SUM(oi.quantity * p.price))  "
                + "FROM OrderItem oi, IN(oi.orderedProduct) p INNER JOIN Book b on p.id=b.id, IN(oi.order) o " 
                + "WHERE o.orderStatus= :status AND (o.dateCreated BETWEEN :first AND :last) "
                + "AND  b.category = :category "
                + "GROUP BY p.id";
        }
        
        Query query = entityManager.createQuery(ql);
        query.setParameter("status", OrderStatus.Complete);
        query.setParameter("first", from);
        query.setParameter("last",to);
        if(cat != null)
            query.setParameter("category", cat);
        
        reports = query.getResultList();
        
        return reports;
    }
    
    
    public int getLastDayOfMonth(int month, int year){
        //Because month number start from 0
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        
        int lastDayOfTheMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        return lastDayOfTheMonth;
    }
    
}
