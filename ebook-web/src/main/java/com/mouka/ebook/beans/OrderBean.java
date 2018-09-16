package com.mouka.ebook.beans;

import com.mouka.ebook.beans.entity.Address;
import com.mouka.ebook.beans.util.CheckoutPaymentError;
import com.mouka.ebook.beans.util.JsfUtils;
import com.mouka.ebook.entity.BankCard;
import com.mouka.ebook.entity.Customer;
import com.mouka.ebook.entity.CustomerOrder;
import com.mouka.ebook.entity.OrderItem;
import com.mouka.ebook.entity.Product;
import com.mouka.ebook.entity.util.OrderStatus;
import com.mouka.ebook.service.CustomerService;
import com.mouka.ebook.service.OrderService;
import com.mouka.ebook.service.ProductService;
import com.mouka.ebook.service.UserService;
import com.twocheckout.model.Authorization;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.New;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Abdelhak
 */

@Named
@SessionScoped
public class OrderBean implements  Serializable{
    
    @EJB
    private ProductService productService;
    
    @EJB
    private UserService userService;
    
    @EJB
    private CustomerService customerService;
    
    @EJB
    private OrderService orderService;
    
    @Inject
    @New
    private CustomerOrder orders;
    
    @Inject
    @New(value = Customer.class)
    private Customer actualCustomer;
    
    private Set<OrderItem> orderedItems = new HashSet<>();
    
    private int quantity = 1;
    private boolean renderLoginForm;
    private boolean renderShoppingCartSection;
    private boolean useTwoAddress;
    private boolean storePaymentDetails;
    
    private String tokenValue = "";
    
    private Address billingAddress = new Address();
    private Address shippingAddress = new Address();
    
    private Map<String, String> billing = new HashMap<>();
    private Map<String, String> shipping = new HashMap<>();
    
    private String shipmentMethod;
    private String shipmentCost;

    
    @Inject
    @New(BankCard.class)
    private BankCard bankCard;
    
    //We divide the date because we have to have a string here and a date in db.
    private String expDate;
    
    private BigDecimal totalCharge = new BigDecimal(0);
    
    public OrderBean() {
    }
    
    @PostConstruct
    public void init(){
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        if(thereIsALoggedUser()){
            //There is a logged user
            String customerEmail = externalContext.getRemoteUser();
            actualCustomer = customerService.findByEmail(customerEmail);
            orders.setCustomer(actualCustomer);//?????
            orders.setOrderStatus(OrderStatus.Pending);
        }else{
            //There is no logged in user
            //return "login.xhtml";
        }
    }
    
    public String addProductToOrderList(Long id){
        Product orderedProduct = productService.findById(id);
        OrderItem orderedItemDetails = new OrderItem();
        orderedItemDetails.setOrder(orders);
        orderedItemDetails.setOrderedProduct(orderedProduct);
        orderedItemDetails.setQuantity(getQuantity());
        
        getOrderedItems().add(orderedItemDetails);
        setRenderShoppingCartSection(true);
        
        return null;
    }
    
    public boolean thereIsALoggedUser(){
        boolean answer = false;
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        String username = externalContext.getRemoteUser();
        List<String> userRole = null;
        if(username != null && !username.isEmpty()){
            userRole = userService.getUserRoleByEmail(username);
            if(userRole.contains("Admin")){
                JsfUtils.addFailureMessage("We know that you are the admin "
                        + "and you control all, but you have to be a customer to shop:");
            }else if(userRole.contains("Customer")){
                answer = true;
            }else{
                //Undefined
            }
        }
        
        return answer;
    }
    
    public boolean orderedAlready(Long id){
        boolean ordered = false;
        OrderItem item = new OrderItem(id);
        for(OrderItem od : getOrderedItems()){
            if(od.getOrderedProduct().getId().equals(id)){
                ordered = true;
                break;
            }
        }
        
        return ordered;
    }
    
    public String processPayment(){
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        String nextPage = null;
        String responseType = params.get("status");
        if(responseType.equals("success")){
            tokenValue = params.get("token");
            nextPage = "order-placed.xhtml?faces-redirect=true";
        }else{//There is only two possible status
            String errorCode = params.get("errorCode");
            CheckoutPaymentError error = CheckoutPaymentError.UNAUTHORIZED;
            switch(Integer.parseInt(errorCode)){
                case 300:
                    error =  CheckoutPaymentError.UNAUTHORIZED;
                    break;
                case 400:
                    error = CheckoutPaymentError.BAD_REQ_PARAMETER_MISSING;
                    break;
                case 401:
                    error = CheckoutPaymentError.BAD_REQ_DATA_MISSING;
                    break;
                case 200:
                    error = CheckoutPaymentError.REQUEST_FAILED_RETRY;
                    break;
                case 500:
                    error = CheckoutPaymentError.REQUEST_FAILED;
                    break;
                default :
                    error = CheckoutPaymentError.REQUEST_FAILED_RETRY;
            }
            JsfUtils.addFailureMessage(error.getMessage());
        }
        
        return nextPage;
    }
    
    
    public String configureDeliveryStage(){
        //Billing Address
        billing.put("name", billingAddress.getFname() + " " + billingAddress.getLname());
        billing.put("addrLine1", billingAddress.getAddrLine1());
        if(billingAddress.getAddrLine2() != null && !billingAddress.getAddrLine2().isEmpty())
            billing.put("addrLine2", billingAddress.getAddrLine2() != null ? billingAddress.getAddrLine2() : "");
        billing.put("city", billingAddress.getCity());
        billing.put("state", billingAddress.getState());
        billing.put("country", billingAddress.getCountry());
        if(billingAddress.getZipCode() != null && !billingAddress.getZipCode().isEmpty()){
            billing.put("zipCode", billingAddress.getZipCode());
        }
        billing.put("email", billingAddress.getEmail());
        billing.put("phoneNumber", billingAddress.getPhone());
        
        if(useTwoAddress){
            //Shipping Address
            shipping.put("name", shippingAddress.getFname() + " " + shippingAddress.getLname());
            shipping.put("addrLine1", shippingAddress.getAddrLine1());
            if(shippingAddress.getAddrLine2() != null && !shippingAddress.getAddrLine2().isEmpty())
                shipping.put("addrLine2", shippingAddress.getAddrLine2() != null ? shippingAddress.getAddrLine2() : "");
            shipping.put("city", shippingAddress.getCity());
            shipping.put("state", shippingAddress.getState());
            shipping.put("country", shippingAddress.getCountry());
            if(shippingAddress.getZipCode() != null && !shippingAddress.getZipCode().isEmpty()){
                shipping.put("zipCode", shippingAddress.getZipCode());
            }
            shipping.put("email", shippingAddress.getEmail());
            shipping.put("phoneNumber", shippingAddress.getPhone());
        }else{
            //Use One Address Only For Billing And Shipping Too.
            shipping = billing;
        }
        
        return "payment.xhtml?faces-redirect=true";
    }
    
    public String reset(){
        billingAddress = new Address();
        System.out.println("Reset Everything to its original Place!!!!!");
        
        return null;
    }
    
    public String chargePayment(){
        
        Authorization authorization =  orderService.authorizePayment(shipping, billing, tokenValue, orderedItems);
        String message = authorization.getResponseMsg();
        String nextPage = null;
        /*
        * For a Successfull authorization==> Successfully authorized the provided credit card
        * For a Failed authorization==>
        * Payment Authorization Failed: Please verify your information and try again, or try another payment method.
        */
        if(message.startsWith("Payment Authorization Failed")){
            JsfUtils.addFailureMessage("Sorry, Payment Authorization Failed: Please verify your information"
                    + " and try again, or try another payment method.");
            nextPage = null;
        }else{
            JsfUtils.addSuccessMessage("Thank You, You Will Receive Your Orders Shortly.");
            nextPage = null;
        }
        
        return nextPage;
    }

    
    private void adjustDateForCard(BankCard card, String dateInString){
        String dateArray[] = dateInString.split("/");
        
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(dateArray[1]));
        calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[0])-1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        
        Date expDate = calendar.getTime();
        card.setExpDate(expDate);
    }
    
    public void processValueChange(Long itemProductId, int itemQuantity){
        //Update the quantity of the item.
        for(OrderItem item : orderedItems){
            if(item.getOrderedProduct().getId().equals(itemProductId)){
                item.setQuantity(itemQuantity);
            }
        }
    }
    
    
    public String deleteFromShoppingCart(Long id){
        for(OrderItem oi : orderedItems){
            if(oi.getOrderedProduct().getId().equals(id)){
                orderedItems.remove(oi);
                break;
            }
        }
        
        return null;
    }
    
    
    public String calculateShipmentCost(){
        String result = "";
        StringBuffer sb = new StringBuffer();
        Calendar calendar  = Calendar.getInstance();
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM, dd");
        
        switch(shipmentMethod){
            case "FreeNoRush"://Add 10 Days
                sb.append("Your Delivery Will Be Free. You Will Get It ");
                calendar.add(Calendar.DAY_OF_MONTH, 10);
                date = calendar.getTime();
                sb.append(sdf.format(date));
                result = sb.toString();
                break;
            case "Standard"://Add 5 Days
                sb.append("Your Delivery Will Be Free. You Will Get It ");
                calendar.add(Calendar.DAY_OF_MONTH, 5);
                date = calendar.getTime();
                sb.append(sdf.format(date));
                result = sb.toString();
                break;
            case "Priority"://Add 2 Days()
                sb.append("Dilvery Charged With 5$. You Will Get It ");
                calendar.add(Calendar.DAY_OF_MONTH, 2);
                date = calendar.getTime();
                sb.append(sdf.format(date));
                result = sb.toString();
                break;
            case "Paid"://Add One Business Day
                sb.append("Delivery Charged With 10$. You Will Get It Tomorrow ");
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                date = calendar.getTime();
                sb.append(sdf.format(date));
                result = sb.toString();
                break;
            default://Add 10 Days
                sb.append("Your Delivery Will Be Free. You Will Get It ");
                calendar.add(Calendar.DAY_OF_MONTH, 10);
                date = calendar.getTime();
                sb.append(sdf.format(date));
                result = sb.toString();
        }
        
        shipmentCost = result;
        
        return result;
    }
    
    
    public void useOneAddressOnly(){
        System.out.println("==============>Use One Address Only");
        //useTwoAddress = true;
    }
    
    /**
     * @return the storePaymentDetails
     */
    public boolean isStorePaymentDetails() {
        return storePaymentDetails;
    }
    
    /**
     * @param storePaymentDetails the storePaymentDetails to set
     */
    public void setStorePaymentDetails(boolean storePaymentDetails) {
        this.storePaymentDetails = storePaymentDetails;
    }
    
    
    public String submit(){
        return null;
    }
    /**
     * @return the orderedItems
     */
    public Set<OrderItem> getOrderedItems() {
        return orderedItems;
    }
    
    /**
     * @param orderedItems the orderedItems to set
     */
    public void setOrderedItems(Set<OrderItem> orderedItems) {
        this.orderedItems = orderedItems;
    }
    
    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }
    
    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    /**
     * @return the renderLoginForm
     */
    public boolean isRenderLoginForm() {
        return renderLoginForm;
    }
    
    /**
     * @param renderLoginForm the renderLoginForm to set
     */
    public void setRenderLoginForm(boolean renderLoginForm) {
        this.renderLoginForm = renderLoginForm;
    }
    
    /**
     * @return the renderShoppingCartSection
     */
    public boolean isRenderShoppingCartSection() {
        return renderShoppingCartSection;
    }
    
    /**
     * @param renderShoppingCartSection the renderShoppingCartSection to set
     */
    public void setRenderShoppingCartSection(boolean renderShoppingCartSection) {
        this.renderShoppingCartSection = renderShoppingCartSection;
    }
    
    /**
     * @return the useTwoAddress
     */
    public boolean isUseTwoAddress() {
        return useTwoAddress;
    }
    
    /**
     * @param useTwoAddress the useTwoAddress to set
     */
    public void setUseTwoAddress(boolean useTwoAddress) {
        this.useTwoAddress = useTwoAddress;
    }
    
    /**
     * @return the tokenValue
     */
    public String getTokenValue() {
        return tokenValue;
    }
    
    /**
     * @param tokenValue the tokenValue to set
     */
    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }
    
    /**
     * @return the billingAddress
     */
    public Address getBillingAddress() {
        return billingAddress;
    }
    
    /**
     * @param billingAddress the billingAddress to set
     */
    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }
    
    /**
     * @return the shippingAddress
     */
    public Address getShippingAddress() {
        return shippingAddress;
    }
    
    /**
     * @param shippingAddress the shippingAddress to set
     */
    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
    
    /**
     * @return the billing
     */
    public Map<String, String> getBilling() {
        return billing;
    }
    
    /**
     * @param billing the billing to set
     */
    public void setBilling(Map<String, String> billing) {
        this.billing = billing;
    }
    
    /**
     * @return the shipping
     */
    public Map<String, String> getShipping() {
        return shipping;
    }
    
    /**
     * @param shipping the shipping to set
     */
    public void setShipping(Map<String, String> shipping) {
        this.shipping = shipping;
    }
    
    /**
     * @return the shippmentMethod
     */
    public String getShipmentMethod() {
        return shipmentMethod;
    }
    
    /**
     * @param shippmentMethod the shippmentMethod to set
     */
    public void setShipmentMethod(String shipmentMethod) {
        this.shipmentMethod = shipmentMethod;
    }
    
    /**
     * @return the shipmentCost
     */
    public String getShipmentCost() {
        return shipmentCost;
    }
    
    /**
     * @param shipmentCost the shipmentCost to set
     */
    public void setShipmentCost(String shipmentCost) {
        this.shipmentCost = shipmentCost;
    }
    
    /**
     * @return the bankCard
     */
    public BankCard getBankCard() {
        return bankCard;
    }
    
    /**
     * @param bankCard the bankCard to set
     */
    public void setBankCard(BankCard bankCard) {
        this.bankCard = bankCard;
    }
    
    /**
     * @return the expDate
     */
    public String getExpDate() {
        return expDate;
    }
    
    /**
     * @param expDate the expDate to set
     */
    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }
    
    /**
     * @return the totalCharge
     */
    public BigDecimal getTotalCharge() {
        return totalCharge;
    }
    
    /**
     * @param totalCharge the totalCharge to set
     */
    public void setTotalCharge(BigDecimal totalCharge) {
        this.totalCharge = totalCharge;
    }
    
}