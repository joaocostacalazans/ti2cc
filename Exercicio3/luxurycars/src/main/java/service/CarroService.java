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
            // Certifique-se de usar a senha correta do PostgreSQL
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/luxurycars", "postgres", "sua_senha_aqui");
            carroDAO = new CarroDAO(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void iniciarServicos() {
        // Redireciona a rota raiz (/) para o formulário
        get("/", (req, res) -> {
            res.redirect("/formulario");
            return null;
        });

        // Serve o formulário HTML
        get("/formulario", (req, res) -> {
            res.type("text/html");
            return new String(Files.readAllBytes(Paths.get("src/main/resources/formulario.html")));
        });

        // Rota para adicionar carro
        post("/carros", this::adicionarCarro);

        // Rota para listar carros
        get("/carros", this::listarCarros);

        // Rota para detalhar um carro específico
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

        // Rota para atualizar um carro específico
        put("/carros/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Carro carro = gson.fromJson(req.body(), Carro.class);
            carro.setId(id);
            carroDAO.atualizarCarro(carro);
            res.status(200);
            return "Carro atualizado com sucesso!";
        });

        // Rota para remover um carro específico
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
        res.status(201); // Created
        return "Carro adicionado com sucesso!";
    }

    private String listarCarros(Request req, Response res) throws SQLException {
        List<Carro> carros = carroDAO.listarCarros();
        res.status(200); // OK
        return gson.toJson(carros);
    }
}