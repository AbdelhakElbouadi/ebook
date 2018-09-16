package com.mouka.ebook.beans.util;

import com.mouka.ebook.entity.Image;
import com.mouka.ebook.service.ImageService;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Abdelhak
 */
@WebServlet("/image/*")
public class ImageServlet extends HttpServlet{
    
    @EJB
    private ImageService imageService;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException{
        Long imageId = Long.parseLong(req.getPathInfo().substring(1));
        Image image = imageService.findImageById(imageId);
        res.setContentType(image.getMimeType());
        res.setContentLength(image.getImageData().length);
        res.getOutputStream().write(image.getImageData());
    }
    
}
