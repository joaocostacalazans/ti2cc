package com.ti2cc;

import java.util.Scanner;

public class Principal {
	
	public static void main(String[] args) {
		DAO dao = new DAO();
		dao.conectar();
		Scanner scanner = new Scanner(System.in);
		int opcao = 0;
		
		do {
			System.out.println("=== MENU ===");
			System.out.println("1) Listar");
			System.out.println("2) Inserir");
			System.out.println("3) Excluir");
			System.out.println("4) Atualizar");
			System.out.println("5) Sair");
			System.out.print("Escolha uma opção: ");
			opcao = scanner.nextInt();
			scanner.nextLine(); // Limpar o buffer
			
			switch(opcao) {
				case 1: // Listar
					Usuario[] usuarios = dao.getUsuarios();
					System.out.println("==== Listar usuários === ");
					for(Usuario usuario : usuarios) {
						System.out.println(usuario.toString());
					}
					break;
					
				case 2: // Inserir
					System.out.print("Código: ");
					int codigo = scanner.nextInt();
					scanner.nextLine(); // Limpar o buffer
					System.out.print("Login: ");
					String login = scanner.nextLine();
					System.out.print("Senha: ");
					String senha = scanner.nextLine();
					System.out.print("Sexo (M/F): ");
					char sexo = scanner.nextLine().toUpperCase().charAt(0);
					
					Usuario usuario = new Usuario(codigo, login, senha, sexo);
					if(dao.inserirUsuario(usuario)) {
						System.out.println("Inserção com sucesso -> " + usuario.toString());
					} else {
						System.out.println("Erro ao inserir o usuário.");
					}
					break;
					
				case 3: // Excluir
					System.out.print("Código do usuário a excluir: ");
					int codigoExcluir = scanner.nextInt();
					
					if(dao.excluirUsuario(codigoExcluir)) {
						System.out.println("Usuário excluído com sucesso.");
					} else {
						System.out.println("Erro ao excluir o usuário.");
					}
					break;
					
				case 4: // Atualizar
					System.out.print("Código do usuário a atualizar: ");
					int codigoAtualizar = scanner.nextInt();
					scanner.nextLine(); // Limpar o buffer
					
					System.out.print("Novo login: ");
					String novoLogin = scanner.nextLine();
					System.out.print("Nova senha: ");
					String novaSenha = scanner.nextLine();
					System.out.print("Novo sexo (M/F): ");
					char novoSexo = scanner.nextLine().toUpperCase().charAt(0);
					
					Usuario usuarioAtualizar = new Usuario(codigoAtualizar, novoLogin, novaSenha, novoSexo);
					
					if(dao.atualizarUsuario(usuarioAtualizar)) {
						System.out.println("Usuário atualizado com sucesso -> " + usuarioAtualizar.toString());
					} else {
						System.out.println("Erro ao atualizar o usuário.");
					}
					break;
					
				case 5: // Sair
					System.out.println("Encerrando o programa...");
					break;
					
				default:
					System.out.println("Opção inválida. Tente novamente.");
			}
			
		} while(opcao != 5);
		
		dao.close();
		scanner.close();
	}
}
