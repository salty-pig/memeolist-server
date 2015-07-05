package org.jboss.aerogear.memolist.service.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import org.apache.commons.io.IOUtils;
import org.jboss.aerogear.memolist.security.RedHatUserBean;
import org.jboss.aerogear.memolist.service.MemeService;
import org.jboss.aerogear.memolist.util.ImageOverlay;
import org.jboss.aerogear.memolist.vo.Meme;
import org.jboss.aerogear.memolist.vo.Post;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 *
 * @author summers
 */
@Stateless
@Path("/api")
public class MemeController {

    // currently we use system 'temp' directory to store files
    private static final String SERVER_UPLOAD_LOCATION_FOLDER = System.getProperty("java.io.tmpdir") + File.separator;

    @Inject
    private RedHatUserBean rhuBean;

    @Inject
    private MemeService memeService;

    @POST
    @PUT
    @Path("meme")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("multipart/form-data")
    public Meme saveMeme(MultipartFormDataInput input) {

        Map<String, List<InputPart>> dataMap = input.getFormDataMap();

        List<InputPart> fileParts = dataMap.get("file");
        List<InputPart> topCommentParts = dataMap.get("topComment");
        List<InputPart> bottomCommentParts = dataMap.get("bottomComment");
        String topComment = "";
        String bottomComment = "";
        byte[] image = null;
        for (InputPart part : fileParts) {
            try {
                InputStream is = part.getBody(InputStream.class, null);
                image = readFile(is);
            } catch (IOException e) {
                throw new WebApplicationException(e);
            }
        }

        for (InputPart part : topCommentParts) {
            try {
                
                InputStream is = part.getBody(InputStream.class, null);
                topComment = IOUtils.toString(is);
            } catch (IOException e) {
                throw new WebApplicationException(e);
            }
        }
        
        for (InputPart part : bottomCommentParts) {
            try {
                
                InputStream is = part.getBody(InputStream.class, null);
                bottomComment = IOUtils.toString(is);
            } catch (IOException e) {
                throw new WebApplicationException(e);
            }
        }
        
        try {
            BufferedImage overlayImage = ImageOverlay.overlay(ImageIO.read(new ByteArrayInputStream(image)), topComment, bottomComment);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( overlayImage, "png", baos );
            baos.flush();
            image = baos.toByteArray();
            baos.close();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(MemeController.class.getName()).log(Level.SEVERE, null, ex);
            throw new WebApplicationException(ex);
        }
        
        Meme post = new Meme();
        post.setTopComment(topComment);
        post.setBottomComment(bottomComment);
        post.setPosted(new Date());
        post.setImage(image);
        post.setOwner(rhuBean.getRedHatUser());
        post = memeService.save(post);
        return post;
    }

    @GET
    @Path("meme")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Meme> mostRecent() {
        return memeService.getMostRecent();
    }
    
    // save uploaded file to a defined location on the server
    public static byte[] readFile(InputStream uploadedInputStream) {
        try {
            ByteArrayOutputStream outpuStream = new ByteArrayOutputStream(1024);
            byte[] bytes = new byte[1024];

            int read;
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                outpuStream.write(bytes, 0, read);
            }

            outpuStream.flush();
            outpuStream.close();
            
            return outpuStream.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
