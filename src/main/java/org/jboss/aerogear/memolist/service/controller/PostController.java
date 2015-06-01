package org.jboss.aerogear.memolist.service.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import org.apache.commons.io.IOUtils;
import org.jboss.aerogear.memolist.security.RedHatUserBean;
import org.jboss.aerogear.memolist.service.PostService;
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
    private PostService postService;

    @POST
    @Path("post")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("multipart/form-data")
    public Post post(MultipartFormDataInput input) {

        Map<String, List<InputPart>> dataMap = input.getFormDataMap();

        List<InputPart> fileParts = dataMap.get("file");
        List<InputPart> commentParts = dataMap.get("comment");
        String filename = null;
        String comment = "";

        for (InputPart part : fileParts) {
            try {
                filename = UUID.randomUUID() + ".jpg";
                InputStream is = part.getBody(InputStream.class, null);
                saveFile(is, SERVER_UPLOAD_LOCATION_FOLDER + filename);
            } catch (IOException e) {
                throw new WebApplicationException(e);
            }
        }

        for (InputPart part : commentParts) {
            try {
                
                InputStream is = part.getBody(InputStream.class, null);
                comment = IOUtils.toString(is);
            } catch (IOException e) {
                throw new WebApplicationException(e);
            }
        }
        
        Post post = new Post();
        post.setComment(comment);
        post.setPosted(new Date());
        post.setOwner(rhuBean.getRedHatUser());
        post = postService.save(post);
        return post;
    }

    // save uploaded file to a defined location on the server
    public static void saveFile(InputStream uploadedInputStream,
            String serverLocation) {
        try {
            OutputStream outpuStream = new FileOutputStream(new File(serverLocation));
            byte[] bytes = new byte[1024];

            int read;
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                outpuStream.write(bytes, 0, read);
            }

            outpuStream.flush();
            outpuStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
