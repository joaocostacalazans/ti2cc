package service;

import dao.CarroDAO;
import model.Carro;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static spark.Spark.*;

public class CarroService {
    private CarroDAO carroDAO;
    private Gson gson = new Gson();

    public CarroService() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/luxurycars", "postgres", "");
            carroDAO = new CarroDAO(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void iniciarServicos() {
        get("/", (req, res) -> {
            res.redirect("/formulario");
            return null;
        });

        get("/formulario", (req, res) -> {
            res.type("text/html");
            return new String(Files.readAllBytes(Paths.get("src/main/resources/formulario.html")));
        });

        post("/carros", this::adicionarCarro);

        get("/carros", this::listarCarros);

        get("/carros/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Carro carro = carroDAO.getCarroById(id);
            if (carro != null) {
                res.status(200);
                return gson.toJson(carro);
            } else {
                res.status(404);
                return "Carro não encontrado";
            }
        });

        put("/carros/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Carro carro = gson.fromJson(req.body(), Carro.class);
            carro.setId(id);
            carroDAO.atualizarCarro(carro);
            res.status(200);
            return "Carro atualizado com sucesso!";
        });

        delete("/carros/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            carroDAO.removerCarro(id);
            res.status(200);
            return "Carro removido com sucesso!";
        });
    }

    private String adicionarCarro(Request req, Response res) throws SQLException {
        Carro carro = gson.fromJson(req.body(), Carro.class);
        carroDAO.adicionarCarro(carro);
        res.status(201);
        return "Carro adicionado com sucesso!";
    }

    private String listarCarros(Request req, Response res) throws SQLException {
        List<Carro> carros = carroDAO.listarCarros();
        res.status(200);
        return gson.toJson(carros);
    }
}