package controller;

import model.Photo;

import java.util.HashMap;

public interface PhotoService {
    HashMap<String, Photo> getAllPhotos();
    Photo getPhotoById(String id);
    Photo getPhotoByName(String name);

    boolean deletePhotoById(String id);
}
