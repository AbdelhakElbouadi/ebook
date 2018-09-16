package com.mouka.ebook.service;

import com.mouka.ebook.entity.BankAccount;
import com.mouka.ebook.entity.BankCard;
import com.mouka.ebook.entity.BankCard_;
import com.mouka.ebook.entity.CustomerOrder;
import com.mouka.ebook.entity.OrderItem;
import com.mouka.ebook.entity.OrderReportData;
import com.mouka.ebook.entity.util.OrderStatus;
import static com.mouka.ebook.entity.util.OrderStatus.All;
import com.mouka.ebook.exceptions.NoEnoughFundsException;
import com.twocheckout.Twocheckout;
import com.twocheckout.TwocheckoutCharge;
import com.twocheckout.TwocheckoutException;
import com.twocheckout.model.Authorization;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Abdelhak
 */
@Stateless
public class OrderService extends ServiceFacade<CustomerOrder>{
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public OrderService() {
        super(CustomerOrder.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    
    public BankCard validateBankCard(BankCard bankCard){
        //Using the Checkout Card or Any Other Payment Gateway.
        boolean isValidate = false;
        BankCard actualCard = null;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BankCard> criteriaQuery = builder.createQuery(BankCard.class);
        Root<BankCard> root = criteriaQuery.from(BankCard.class);
        criteriaQuery.select(root);
        criteriaQuery.where(
                builder.and(
                        builder.equal(root.get(BankCard_.cardNumber),
                                bankCard.getCardNumber()),
                        builder.equal(root.get(BankCard_.cardVerificationValue),
                                bankCard.getCardVerificationValue())));
        Query query = entityManager.createQuery(criteriaQuery);
        
        List<BankCard> listOfBankCards = query.getResultList();
        if(!listOfBankCards.isEmpty()){
            //It is the first of the month that is considered
            if(bankCard.getExpDate().after(firstDateOfTheCurrentMonth())){
                actualCard = listOfBankCards.get(0);
                isValidate = true;
            }
        }
        
        return actualCard;
    }
    
    public Authorization authorizePayment(Map<String, String> shippingAddress, Map<String, String> billingAddress, String token, Set<OrderItem> orderedItems){
        String message = "";
        Authorization result = null;
        Twocheckout.privatekey = "A3031008-A2E9-47C2-BC6D-5495712FAEF8";
        Twocheckout.mode = "sandbox";
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("sellerId", "901388955");
            params.put("merchantOrderId", "test123");
            params.put("token", token);
            params.put("currency", "USD");
            params.put("total", String.valueOf(getShoppingTotalPrice(orderedItems)));
            params.put("billingAddr", billingAddress);
            params.put("shippingAddr", shippingAddress);
            
            result = TwocheckoutCharge.authorize(params);   
            message = result.getResponseMsg();
            if(message.startsWith("Successfully authorized")){//There is a successfull shopping process.
                for(OrderItem od : orderedItems){
                    od.getOrderedProduct().setInStock(od.getOrderedProduct().getInStock() - od.getQuantity());
                }
            }else{//There is a failure.
                
            }
        } catch (TwocheckoutException ex) {
            Logger.getLogger(OrderService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
     private BigDecimal getShoppingTotalPrice(Set<OrderItem> orderedItems){
        BigDecimal totalPrice = new BigDecimal("0.00");
        totalPrice.setScale(2, RoundingMode.CEILING);
        for(OrderItem od : orderedItems){
            totalPrice = totalPrice.add(
                    new BigDecimal(od.getOrderedProduct().getPrice()*od.getQuantity()));
        }
        
        return totalPrice;
    }
    
    private Date firstDateOfTheCurrentMonth(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date currentDate = calendar.getTime();
        
        return currentDate;
    }
    
    public String chargePayment(){
        return null;
    }
    
    public boolean withdraw(BankAccount account, BigDecimal amount)
            throws NoEnoughFundsException{
        boolean result = false;
        if(account.getAvailableBalance().compareTo(amount) >= 0){
            account.setAvailableBalance(account.getAvailableBalance().subtract(amount));
            entityManager.merge(account);
            entityManager.flush();
            
            result = true;
        }else{
            throw new NoEnoughFundsException("There is no enough "
                    + "funds in this account to withdraw from");
        }
        
        return result;
    }
    
    public boolean deposit(BankAccount account, BigDecimal amount){
        boolean result = false;
        account.getAvailableBalance().add(amount);
        //Update the account with new data
        entityManager.merge(account);
        result = true;
        
        return result;
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
    
    public BigDecimal getGeneralGainForThisWeek(){
        BigDecimal gain = new BigDecimal(0);
        Date first = getFirstDayOfThisWeek(), last = getLastDayOfThisWeek();
        
        String ql = "SELECT SUM(oi.quantity * p.price) "
                + "FROM OrderItem oi, IN(oi.orderedProduct) p, IN(oi.order) o " 
                + "WHERE (o.dateCreated BETWEEN :first AND :last)";
        Query query = entityManager.createQuery(ql);
        query.setParameter("first", first);
        query.setParameter("last",last);
        
        if(query.getSingleResult() != null){
            gain = new BigDecimal((double)query.getSingleResult());
        }

        return gain;
    }
    
    
    public List<OrderReportData> getOrderGainForThisWeek(OrderStatus status){
        List<OrderReportData> reports = null;
        Date first = getFirstDayOfThisWeek(), last = getLastDayOfThisWeek();
        String ql = "";
        
        if(status == All){
            ql = "SELECT NEW OrderReportData(o.id, o.orderStatus, SUM(oi.quantity), SUM(oi.quantity * p.price))  "
                + "FROM OrderItem oi, IN(oi.orderedProduct) p, IN(oi.order) o " 
                + "WHERE (o.dateCreated BETWEEN :first AND :last)"
                + "GROUP BY o.id";
        }else{
            ql = "SELECT NEW OrderReportData(o.id, o.orderStatus, SUM(oi.quantity), SUM(oi.quantity * p.price))  "
                + "FROM OrderItem oi, IN(oi.orderedProduct) p, IN(oi.order) o " 
                + "WHERE o.orderStatus= :status AND (o.dateCreated BETWEEN :first AND :last) "
                + "GROUP BY o.id";
        }
        
        Query query = entityManager.createQuery(ql);
        query.setParameter("first", first);
        query.setParameter("last", last);
        if(status != All)
            query.setParameter("status", status);
        
        reports = query.getResultList();
        
        
        return reports;
    }
    
    
    public BigDecimal getGeneralGainForRange(Date from, Date to){
        BigDecimal gain = new BigDecimal(0.00);
        String ql = "SELECT SUM(oi.quantity * p.price) "
                + "FROM OrderItem oi, IN(oi.orderedProduct) p, IN(oi.order) o " 
                + "WHERE (o.dateCreated BETWEEN :first AND :last)";
        Query query = entityManager.createQuery(ql);
        query.setParameter("first", from);
        query.setParameter("last",to);
        
        if(query.getSingleResult() != null){
            gain = new BigDecimal((double)query.getSingleResult());
        }
        
        return gain;
    }
    
    public List<OrderReportData> getGainForRange(Date from, Date to, OrderStatus status){
        List<OrderReportData> reports = null;
        String ql = "";
        
        if(status == All){
            ql = "SELECT NEW OrderReportData(o.id, o.orderStatus, SUM(oi.quantity), SUM(oi.quantity * p.price))  "
                + "FROM OrderItem oi, IN(oi.orderedProduct) p, IN(oi.order) o " 
                + "WHERE (o.dateCreated BETWEEN :first AND :last)"
                + "GROUP BY o.id";
        }else{
            ql = "SELECT NEW OrderReportData(o.id, o.orderStatus, SUM(oi.quantity), SUM(oi.quantity * p.price))  "
                + "FROM OrderItem oi, IN(oi.orderedProduct) p, IN(oi.order) o " 
                + "WHERE o.orderStatus= :status AND (o.dateCreated BETWEEN :first AND :last) "
                + "GROUP BY o.id";
        }
        
        Query query = entityManager.createQuery(ql);
        query.setParameter("first", from);
        query.setParameter("last",to);
        if(status != All)
            query.setParameter("status", status);
        
        reports = query.getResultList();
        
        return reports;
    }  
    
}