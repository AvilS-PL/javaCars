import static spark.Spark.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controller.PhotoService;
import controller.PhotoServiceImpl;
import model.Photo;
import response.ResponseEntity;
import response.ResponseStatus;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;


public class AppREST {

    private static Gson gson =  new GsonBuilder().setPrettyPrinting().create();
    private static PhotoServiceImpl photoService = new PhotoServiceImpl();

    public static void main(String[] args) {
        port(7777);
        get("/api/photos", (req, res) -> getAllPhotos(req,res));
        get("/api/photos/:id", (req, res) -> getPhotoById(req,res));
        get("/api/photos/photo/:name", (req, res) -> getPhotoByName(req,res));
        delete("api/photos/:id", (req,res) -> deletePhotoById(req,res));
        put("api/photos/:id", (req,res) -> editPhotoById(req,res));
        get("/api/photos/data/:id", (req, res) -> getDataPhoto(req,res));
    }

    private static Object getDataPhoto(Request req, Response res) {
        res.header("Access-Control-Allow-Origin", "*");
        res.type("image/jpeg");

        boolean answer = photoService.getDataPhoto(req.params(":id"), res);

        System.out.println(answer);
        if (answer) return res.raw();
        else {
            res.type("application/json");
            return gson.toJson(new ResponseEntity(ResponseStatus.ERROR, "nie znaleziono zdjęcia"));
        }
    }

    private static Object editPhotoById(Request req, Response res) {
        boolean answer = photoService.editPhotoById(req.params(":id"), gson.fromJson(req.body(), Photo.class).getName());

        res.header("Access-Control-Allow-Origin", "*");
        res.type("application/json");
        if (answer) return gson.toJson(new ResponseEntity(ResponseStatus.SUCCESS, "zmieniono nazwę"));
        else return gson.toJson(new ResponseEntity(ResponseStatus.ERROR, "nie znaleziono zdjęcia lub nie zmieniono nazwy"));
    }

    private static Object deletePhotoById(Request req, Response res) {
        boolean answer = photoService.deletePhotoById(req.params(":id"));

        res.header("Access-Control-Allow-Origin", "*");
        res.type("application/json");
        if (answer) return gson.toJson(new ResponseEntity(ResponseStatus.SUCCESS, "usunięto zdjęcie"));
        else return gson.toJson(new ResponseEntity(ResponseStatus.ERROR, "nie znaleziono zdjęcia lub nie usunięto"));
    }

    private static Object getPhotoByName(Request req, Response res) {
        Photo answer = photoService.getPhotoByName(req.params(":name"));

        res.header("Access-Control-Allow-Origin", "*");
        res.type("application/json");
        if (answer != null) return gson.toJson(new ResponseEntity(ResponseStatus.SUCCESS, "załadowano zdjęcie", gson.toJsonTree(answer)));
        else return gson.toJson(new ResponseEntity(ResponseStatus.ERROR, "nie znaleziono zdjęcia o nazwie: " + req.params(":name")));
    }

    private static Object getPhotoById(Request req, Response res) {
        Photo answer = photoService.getPhotoById(req.params(":id"));

        res.header("Access-Control-Allow-Origin", "*");
        res.type("application/json");
        if (answer != null) return gson.toJson(new ResponseEntity(ResponseStatus.SUCCESS, "załadowano zdjęcie", gson.toJsonTree(answer)));
        else return gson.toJson(new ResponseEntity(ResponseStatus.ERROR, "nie znaleziono zdjęcia o id: " + req.params(":id")));
    }

    private static Object getAllPhotos(Request req, Response res) {
        HashMap<String, Photo> answer = photoService.getAllPhotos();

        res.header("Access-Control-Allow-Origin", "*");
        res.type("application/json");
        if (answer != null) return gson.toJson(new ResponseEntity(ResponseStatus.SUCCESS, "załadowano wszystkie zdjęcia", gson.toJsonTree(answer)));
        else return gson.toJson(new ResponseEntity(ResponseStatus.ERROR, "nie załadowano zdjęć"));
    }
}
