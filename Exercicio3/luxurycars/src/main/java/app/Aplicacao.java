package app;

import service.CarroService;

public class Aplicacao {
    public static void main(String[] args) {
        CarroService carroService = new CarroService();
        carroService.iniciarServicos();
        System.out.println("Servidor iniciado na porta 4567...");
    }
}