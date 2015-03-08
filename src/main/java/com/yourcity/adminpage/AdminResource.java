package com.yourcity.adminpage;

/**
 * Created by Andrey on 21.02.2015.
 */

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Path("/admin")
@Produces(MediaType.TEXT_HTML)
public class AdminResource {

    @GET
    public InputStream showIndexPage() {
        File index = new File("src/main/webapp/admin-page.html");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(index);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fis;
    }
}
