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
//        post("/api/users", (req, res) -> addUser(req,res));
    }

    private static Object deletePhotoById(Request req, Response res) {
        boolean answer = photoService.deletePhotoById(req.params(":id"));
        System.out.println(answer);
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
