package org.jboss.aerogear.memolist.service.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
public class PostController {

    // currently we use system 'temp' directory to store files
    private static final String SERVER_UPLOAD_LOCATION_FOLDER = System.getProperty("java.io.tmpdir") + File.separator;

    @Inject
    private RedHatUserBean rhuBean;

    @Inject
    private MemeService memeService;

    
    @GET
    @Path("post")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Post> mostRecent() {
        List<Meme> memes = memeService.getMostRecent();
        List<Post> posts = new ArrayList<>(memes.size());
        
        memes.forEach(meme -> {
            Post post = new Post();
            post.setComment("No comment");
            post.setFileUrl("http://10.0.2.2:8080/memeolist/v1/api/memeimage/"+meme.getId());
            post.setId(meme.getId());
            post.setOwner(meme.getOwner());
            post.setPosted(meme.getPosted());
            posts.add(post);
        });
        
        return posts;
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
