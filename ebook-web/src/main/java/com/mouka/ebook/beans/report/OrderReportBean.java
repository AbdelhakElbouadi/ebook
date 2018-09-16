package com.mouka.ebook.beans.report;

import com.mouka.ebook.beans.util.JsfUtils;
import com.mouka.ebook.beans.util.NavigationOption;
import com.mouka.ebook.entity.OrderReportData;
import com.mouka.ebook.entity.util.OrderStatus;
import com.mouka.ebook.service.OrderService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author Abdelhak
 */
@Named
@SessionScoped
public class OrderReportBean implements Serializable{
    
    @EJB
    private OrderService orderService;
    
    private OrderStatus searchedStatus;
    private List<NavigationOption> allOptions =  new ArrayList<>();
    private List<OrderStatus> allOrderStatus = new ArrayList<>();
    private NavigationOption chosenOption;
    private boolean renderMonthNav = false, renderCustomNav = false;
    private int chosenMonth;
    private Date fromDate, toDate;
    private BigDecimal weekGain, monthGain, customGain;
    private List<OrderReportData> reportData = new ArrayList<>();
    
    
    @PostConstruct
    public void init(){
        getAllOptions().add(NavigationOption.WEEK);
        getAllOptions().add(NavigationOption.MONTH);
        getAllOptions().add(NavigationOption.CUSTOM);
        
        allOrderStatus.add(OrderStatus.All);
        allOrderStatus.add(OrderStatus.Complete);
        allOrderStatus.add(OrderStatus.Pending);
        allOrderStatus.add(OrderStatus.Cancelled);
        weekGain = orderService.getGeneralGainForThisWeek();
    }
    
    public String refreshNavigationArea(){
        switch(chosenOption){
            case MONTH:
                setRenderMonthNav(true);
                setRenderCustomNav(false);
                reportData.clear();
                break;
            case CUSTOM:
                setRenderCustomNav(true);
                setRenderMonthNav(false);
                reportData.clear();
                break;
            default:
                setRenderMonthNav(false);
                setRenderCustomNav(false);
                showWeekResult();
                break;
        }
        
        return null;
    }
    
    
    public void adjustDateRange(){
        if(fromDate == null || toDate == null)
            return;
        
        if(!fromDate.before(toDate)){
            JsfUtils.addFailureMessage("The Starting Date(From) Should Be Before The Ending Date(To)");
        }else{
            showCustomResult();
        }
    }
    
    public void showResultByStatus(){
        switch(chosenOption){
            case MONTH:
                showMonthResult();
                break;
            case CUSTOM:
                showCustomResult();
                break;
            default:
                showWeekResult();
                break;
        }
    }
    
    public void showWeekResult(){
        if(searchedStatus == null){
            searchedStatus =  OrderStatus.All;
        }
        reportData = orderService.getOrderGainForThisWeek(searchedStatus);
    }
    
    public void showMonthResult(){
        Date first = null, last = null;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, chosenMonth);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        first = calendar.getTime();
        
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        last = calendar.getTime();
        
        monthGain = orderService.getGeneralGainForRange(first, last);
        reportData = orderService.getGainForRange(first, last, searchedStatus);
    }
    
    public void showCustomResult(){
        customGain = orderService.getGeneralGainForRange(fromDate, toDate);
        reportData = orderService.getGainForRange(fromDate, toDate, searchedStatus);
    }
    
    public double getTotal(){
        if(chosenOption != null){
            switch(chosenOption){
                case WEEK:
                    return weekGain.setScale(2, RoundingMode.CEILING).doubleValue();
                case MONTH:
                    if(monthGain != null)
                        return monthGain.setScale(2, RoundingMode.CEILING).doubleValue();
                    else
                        return 0.00;
                case CUSTOM:
                    if(customGain != null)
                        return customGain.setScale(2, RoundingMode.CEILING).doubleValue();
                    else 
                        return 0.00;
                default:
                    return 0.00;
            }
        }
        
        return 0.00;
    }
    
    public String renderStyle(OrderStatus status){
        String style = "";
        switch(status){
            case Complete:
                style = "alert alert-success";
                break;
            case Pending:
                style = "alert alert-default";
                break;
            case Cancelled:
                style = "alert alert-danger";
                break;
            default:
                style = "alert alert-default";
        }
        
        return style;
    }
    
    public Map<String, Integer> getMonthsOfYear(){
        Map<String, Integer> months = new LinkedHashMap<>();
        months.put("JANURAY",0);
        months.put("FEBRUARY",1);
        months.put("MARCH",2);
        months.put("APRIL",3);
        months.put("MAY",4);
        months.put("JUNE",5);
        months.put("JULY",6);
        months.put("AUGUST",7);
        months.put("SEPTEMBER",8);
        months.put("OCTOBER",9);
        months.put("NOVEMBER",10);
        months.put("DECEMBER",11);
        
        return months;
    }
    
    /**
     * @return the searchedStatus
     */
    public OrderStatus getSearchedStatus() {
        return searchedStatus;
    }

    /**
     * @param searchedStatus the searchedStatus to set
     */
    public void setSearchedStatus(OrderStatus searchedStatus) {
        this.searchedStatus = searchedStatus;
    }

    /**
     * @return the allOptions
     */
    public List<NavigationOption> getAllOptions() {
        return allOptions;
    }

    /**
     * @param allOptions the allOptions to set
     */
    public void setAllOptions(List<NavigationOption> allOptions) {
        this.allOptions = allOptions;
    }

    /**
     * @return the allOrderStatus
     */
    public List<OrderStatus> getAllOrderStatus() {
        return allOrderStatus;
    }

    /**
     * @param allOrderStatus the allOrderStatus to set
     */
    public void setAllOrderStatus(List<OrderStatus> allOrderStatus) {
        this.allOrderStatus = allOrderStatus;
    }

    /**
     * @return the chosenOption
     */
    public NavigationOption getChosenOption() {
        return chosenOption;
    }

    /**
     * @param chosenOption the chosenOption to set
     */
    public void setChosenOption(NavigationOption chosenOption) {
        this.chosenOption = chosenOption;
    }

    /**
     * @return the renderMonthNav
     */
    public boolean isRenderMonthNav() {
        return renderMonthNav;
    }

    /**
     * @param renderMonthNav the renderMonthNav to set
     */
    public void setRenderMonthNav(boolean renderMonthNav) {
        this.renderMonthNav = renderMonthNav;
    }

    /**
     * @return the renderCustomNav
     */
    public boolean isRenderCustomNav() {
        return renderCustomNav;
    }

    /**
     * @param renderCustomNav the renderCustomNav to set
     */
    public void setRenderCustomNav(boolean renderCustomNav) {
        this.renderCustomNav = renderCustomNav;
    }

    /**
     * @return the chosenMonth
     */
    public int getChosenMonth() {
        return chosenMonth;
    }

    /**
     * @param chosenMonth the chosenMonth to set
     */
    public void setChosenMonth(int chosenMonth) {
        this.chosenMonth = chosenMonth;
    }

    /**
     * @return the fromDate
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return the toDate
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    /**
     * @return the weekGain
     */
    public BigDecimal getWeekGain() {
        return weekGain;
    }

    /**
     * @param weekGain the weekGain to set
     */
    public void setWeekGain(BigDecimal weekGain) {
        this.weekGain = weekGain;
    }

    /**
     * @return the monthGain
     */
    public BigDecimal getMonthGain() {
        return monthGain;
    }

    /**
     * @param monthGain the monthGain to set
     */
    public void setMonthGain(BigDecimal monthGain) {
        this.monthGain = monthGain;
    }

    /**
     * @return the customGain
     */
    public BigDecimal getCustomGain() {
        return customGain;
    }

    /**
     * @param customGain the customGain to set
     */
    public void setCustomGain(BigDecimal customGain) {
        this.customGain = customGain;
    }

    /**
     * @return the reportData
     */
    public List<OrderReportData> getReportData() {
        return reportData;
    }

    /**
     * @param reportData the reportData to set
     */
    public void setReportData(List<OrderReportData> reportData) {
        this.reportData = reportData;
    }  
    
}
