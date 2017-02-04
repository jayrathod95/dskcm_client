package com.deskcomm.networking;

import com.deskcomm.exceptions.ResponseException;
import com.deskcomm.support.Keys;
import com.deskcomm.support.L;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.File;
import java.net.ConnectException;

/**
 * Created by Jay Rathod on 03-02-2017.
 */
public class ProfilePicUpload {

    private String uuid;
    private String sessionId;
    private File imageFile;


    public ProfilePicUpload(String uuid, String sessionId, File imageFile) {
        this.uuid = uuid;
        this.sessionId = sessionId;
        this.imageFile = imageFile;
    }

    public Response perform() throws ResponseException, ConnectException {
        Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
        FileDataBodyPart filePart = new FileDataBodyPart("xml", imageFile);
        FormDataMultiPart multiPart = new FormDataMultiPart();
        multiPart.field(Keys.SESSION_ID, sessionId).bodyPart(filePart);
        WebTarget webTarget = client.target(URLManager.TARGET_URL + "/user/" + uuid + "/profile_pic");
        Response response = webTarget.request().post(Entity.entity(multiPart, multiPart.getMediaType()));
        if (response.getStatus() >= 200 || response.getStatus() < 300) return response;
        else {
            L.println(response.getStatus());
            throw new ResponseException(Response.Status.fromStatusCode(response.getStatus()));
        }
    }

}
