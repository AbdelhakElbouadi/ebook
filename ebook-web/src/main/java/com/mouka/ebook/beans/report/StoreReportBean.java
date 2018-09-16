package com.mouka.ebook.beans.report;

import com.mouka.ebook.entity.Product;
import com.mouka.ebook.exceptions.NonExistingEntityException;
import com.mouka.ebook.service.ProductService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class StoreReportBean implements Serializable {
    
    @EJB
    private ProductService productService;
    
    private Product toUpdatePro;
    private String searchRequest;
    private boolean renderUpdateForm = false;
    private List<Product> reportData = new ArrayList<>();
    private int initialInStock;
    
    @PostConstruct
    public void init(){
    }
    
    public String updateThisProduct(Long id){
        setToUpdatePro(productService.findById(id));
        initialInStock =  toUpdatePro.getInStock();
        toUpdatePro.setInStock(0);
        setRenderUpdateForm(true);
        
        return null;
    }
    
    public String saveProduct(){
        try {
            toUpdatePro.setLastUpdated(new Date());
            int newStockCharge = toUpdatePro.getInStock() + initialInStock;
            toUpdatePro.setInStock(newStockCharge);
            Product updated = productService.edit(getToUpdatePro());
            reportData.remove(toUpdatePro);
            reportData.add(updated);
            setRenderUpdateForm(false);
        } catch (NonExistingEntityException ex) {
            Logger.getLogger(StoreReportBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public void cancelUpdate(){
        setRenderUpdateForm(false);
    }
    
    public String search(){
        if(getSearchRequest() != null && !searchRequest.isEmpty()){
            setSearchRequest(getSearchRequest().toLowerCase());
            reportData = productService.makeGeneralSearch(getSearchRequest());
        }
        
        return null;
    }
    
    /**
     * @return the toUpdatePro
     */
    public Product getToUpdatePro() {
        return toUpdatePro;
    }
    
    /**
     * @param toUpdatePro the toUpdatePro to set
     */
    public void setToUpdatePro(Product toUpdatePro) {
        this.toUpdatePro = toUpdatePro;
    }
    
    /**
     * @return the searchRequest
     */
    public String getSearchRequest() {
        return searchRequest;
    }
    
    /**
     * @param searchRequest the searchRequest to set
     */
    public void setSearchRequest(String searchRequest) {
        this.searchRequest = searchRequest;
    }
    
    /**
     * @return the renderUpdateForm
     */
    public boolean isRenderUpdateForm() {
        return renderUpdateForm;
    }
    
    /**
     * @param renderUpdateForm the renderUpdateForm to set
     */
    public void setRenderUpdateForm(boolean renderUpdateForm) {
        this.renderUpdateForm = renderUpdateForm;
    }
    
    /**
     * @return the reportData
     */
    public List<Product> getReportData() {
        return reportData;
    }
    
    /**
     * @param reportData the reportData to set
     */
    public void setReportData(List<Product> reportData) {
        this.reportData = reportData;
    }
    
    /**
     * @return the initialInStock
     */
    public int getInitialInStock() {
        return initialInStock;
    }
    
    /**
     * @param initialInStock the initialInStock to set
     */
    public void setInitialInStock(int initialInStock) {
        this.initialInStock = initialInStock;
    }
    
}
