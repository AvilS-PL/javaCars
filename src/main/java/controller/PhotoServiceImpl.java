package controller;

import model.Photo;
import spark.Response;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;
import java.io.File;

public class PhotoServiceImpl implements PhotoService{

    private HashMap<String, Photo> photos = new HashMap<>();
    @Override
    public HashMap<String, Photo> getAllPhotos() {
        File f = new File("images/");
        File[] files = f.listFiles();
        if (files != null) {
            photos.clear();
            int k = 1;
            for (int i = 0; i < files.length; i++) {
                String temp = files[i].getName();
                if (temp.split("\\.")[temp.split("\\.").length - 1].equals("jpg") || temp.split("\\.")[temp.split("\\.").length - 1].equals("png")) {
                    photos.put(String.valueOf(k), new Photo(String.valueOf(k), temp, "images/" + temp));
                    k++;
                }
            }
            return  photos;
        } else {
            return null;
        }
    }

    @Override
    public Photo getPhotoById(String id) {
        return photos.get(id);
    }

    @Override
    public Photo getPhotoByName(String name) {
        for (String key: photos.keySet()) {
            if (photos.get(key).getName().equals(name)) return photos.get(key);
        }
        return null;
    }

    @Override
    public boolean deletePhotoById(String id) {
        if (photos.containsKey(id)) {
            File f = new File(photos.get(id).getPath());
            if (f.delete()) {
                photos.remove(id);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean editPhotoById(String id, String name) {
        if (photos.containsKey(id)) {
            File f = new File(photos.get(id).getPath());
            Path p = Paths.get(photos.get(id).getPath());

            File fNew = new File(p.getParent().toString() + "/" + name);
            if (f.renameTo(fNew)) {
                Photo photo = new Photo(photos.get(id).getId(),name,p.getParent().toString() + "/" + name);
                photos.put(id, photo);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean getDataPhoto(String id, Response res) {
        OutputStream outputStream = null;
        if (photos.containsKey(id)) {
            File f = new File(photos.get(id).getPath());
            try {
                outputStream = res.raw().getOutputStream();
            } catch (IOException e) {
                return false;
            }

            try {
                outputStream.write(Files.readAllBytes(f.toPath()));
            } catch (IOException e) {
                return false;
            }
            return true;
        }
        return false;
    }
}
