package com.mouka.ebook.beans.report;

import com.mouka.ebook.beans.util.JsfUtils;
import com.mouka.ebook.beans.util.NavigationOption;
import static com.mouka.ebook.beans.util.NavigationOption.CUSTOM;
import static com.mouka.ebook.beans.util.NavigationOption.MONTH;
import static com.mouka.ebook.beans.util.NavigationOption.WEEK;
import com.mouka.ebook.entity.BookReportData;
import com.mouka.ebook.entity.Category;
import com.mouka.ebook.service.BookService;
import com.mouka.ebook.service.CategoryService;
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
public class BookReportBean implements Serializable{
    
    @EJB
    private CategoryService categoryService;
    
    @EJB
    private BookService bookService;
    
    private Category searchedCategory;
    private List<NavigationOption> allOptions =  new ArrayList<>();
    private NavigationOption chosenOption;
    private boolean renderMonthNav = false, renderCustomNav = false;
    private int chosenMonth;
    private Date fromDate, toDate;
    private BigDecimal weekGain, monthGain, customGain;
    private List<BookReportData> reportData = new ArrayList<>();
    
    @PostConstruct
    public void init(){
        allOptions.add(NavigationOption.WEEK);
        allOptions.add(NavigationOption.MONTH);
        allOptions.add(NavigationOption.CUSTOM);
        weekGain = bookService.getGeneralGainForThisWeek();
        weekGain = weekGain.setScale(2, RoundingMode.CEILING);
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
    
    public List<Category> getAllCategory(){
        List<Category> all = new ArrayList<>();
        all = categoryService.findAll();
        
        return all;
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
    
    public void showResultByCategory(){
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
    
    public void adjustDateRange(){
        if(fromDate == null || toDate == null)
            return;
        
        if(!fromDate.before(toDate)){
            JsfUtils.addFailureMessage("The Starting Date(From) Should Be Before The Ending Date(To)");
        }else{
            showCustomResult();
        }
    }
    
    public void showWeekResult(){
        reportData = bookService.getBookGainForThisWeek(searchedCategory);
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
        
        monthGain = bookService.getGeneralGainForRange(first, last);
        reportData = bookService.getGainForRange(first, last, searchedCategory);
    }
    
    public void showCustomResult(){
        customGain = bookService.getGeneralGainForRange(fromDate, toDate);
        reportData = bookService.getGainForRange(fromDate, toDate, searchedCategory);
    }
    
    public int getShareFromGeneralBenefit(double amount){
        BigDecimal am = new BigDecimal(amount);
        BigDecimal am1 = am.multiply(new BigDecimal(100));
        int quotient = 0;
        
        if(chosenOption != null){
            switch(chosenOption){
                case WEEK:
                    quotient = (int) (am1.doubleValue()/weekGain.doubleValue());
                    break;
                case MONTH:
                    if(monthGain != null)
                        quotient = (int) (am1.doubleValue()/monthGain.doubleValue());
                    break;
                case CUSTOM:
                    if(customGain != null)
                        quotient = (int) (am1.doubleValue()/customGain.doubleValue());
                    break;
                default:
                    quotient = 0;
            }
        }
        
        return quotient;
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
    
    /**
     * @return the searchedCategory
     */
    public Category getSearchedCategory() {
        return searchedCategory;
    }
    
    /**
     * @param searchedCategory the searchedCategory to set
     */
    public void setSearchedCategory(Category searchedCategory) {
        this.searchedCategory = searchedCategory;
    }
    
    /**
     * @return the allOptions
     */
    public List<NavigationOption> getAllOptions() {
        return allOptions;
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
     * @return the renderCustmNav
     */
    public boolean isRenderCustomNav() {
        return renderCustomNav;
    }
    
    /**
     * @param renderCustmNav the renderCustmNav to set
     */
    public void setRenderCustomNav(boolean renderCustmNav) {
        this.renderCustomNav = renderCustmNav;
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
    public List<BookReportData> getReportData() {
        return reportData;
    }
    
    /**
     * @param reportData the reportData to set
     */
    public void setReportData(List<BookReportData> reportData) {
        this.reportData = reportData;
    }
    
}
