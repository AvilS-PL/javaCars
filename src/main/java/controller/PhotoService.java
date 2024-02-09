package controller;

import model.Photo;
import spark.Response;

import java.io.OutputStream;
import java.util.HashMap;

public interface PhotoService {
    HashMap<String, Photo> getAllPhotos();
    Photo getPhotoById(String id);
    Photo getPhotoByName(String name);

    boolean deletePhotoById(String id);
    boolean editPhotoById(String id, String name);
    boolean getDataPhoto(String id, Response res);
}
