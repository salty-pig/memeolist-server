package org.jboss.aerogear.memolist.service.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.jboss.aerogear.memolist.security.RedHatUserBean;
import org.jboss.aerogear.memolist.service.MemeService;
import org.jboss.aerogear.memolist.vo.Meme;
import org.jboss.aerogear.memolist.vo.Post;

/**
 *
 * @author summers
 */
@Stateless
@Path("/api")
public class PostController {

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
