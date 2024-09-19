package dao;

import model.Carro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarroDAO {
    private Connection conn;

    public CarroDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Carro> listarCarros() throws SQLException {
        List<Carro> carros = new ArrayList<>();
        String query = "SELECT * FROM carros";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Carro carro = new Carro(
                    rs.getInt("id"),
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getInt("ano"),
                    rs.getDouble("preco")
                );
                carros.add(carro);
            }
        }

        return carros;
    }

    public void adicionarCarro(Carro carro) throws SQLException {
        String query = "INSERT INTO carros (marca, modelo, ano, preco) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, carro.getMarca());
            pstmt.setString(2, carro.getModelo());
            pstmt.setInt(3, carro.getAno());
            pstmt.setDouble(4, carro.getPreco());
            pstmt.executeUpdate();
        }
    }

    public Carro getCarroById(int id) throws SQLException {
        Carro carro = null;
        String query = "SELECT * FROM carros WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    carro = new Carro(
                        rs.getInt("id"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getInt("ano"),
                        rs.getDouble("preco")
                    );
                }
            }
        }
        return carro;
    }

    public void atualizarCarro(Carro carro) throws SQLException {
        String query = "UPDATE carros SET marca = ?, modelo = ?, ano = ?, preco = ? WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, carro.getMarca());
            pstmt.setString(2, carro.getModelo());
            pstmt.setInt(3, carro.getAno());
            pstmt.setDouble(4, carro.getPreco());
            pstmt.setInt(5, carro.getId());
            pstmt.executeUpdate();
        }
    }

    public void removerCarro(int id) throws SQLException {
        String query = "DELETE FROM carros WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}