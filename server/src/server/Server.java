/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 *
 * @author thiego
 */
public class Server {
    private ServerSocket serverSocket;
    public static String[][] matrix;
    public static final int RIGHT = 0;
    public static final int DIAGONAL = 1;
    public static final int DOWN = 2;
    
    private void criarServerSocket(int porta) throws IOException{
        serverSocket = new ServerSocket(porta);
    }
    
    private Socket esperaConexao() throws IOException{
        Socket socket = serverSocket.accept();
        return socket;
    }
    
    private void fechaSocket(Socket s) throws IOException{
        s.close();
    }
    
    private void trataConexao(Socket socket) throws IOException{
        try {
            ObjectOutputStream outpout = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            String msg = input.readUTF();
            
            System.out.println("Mensagem recebida de " +msg);
            String words = null;
		BufferedReader reader;
		reader = new BufferedReader(new InputStreamReader(System.in));
		while(!isValid(words)){
                System.out.println("Entre com uma palavra relacionada com SD de até 9 caracteres: ");
                //System.out.print("Entre com 3 palavras com 9 caracteres e separadas por espaço: ");
		
		try {
			words = reader.readLine();
                        msg = words;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		// check the input
		if (isValid(words)) {
			// process the input to create the crosswords
			showCrosswords(words, outpout);
		} else {
			System.out.println("As palavras inseridas não foram válidas.");
                }
                }
            outpout.flush();
            System.out.println("Aguardando resposta do desafio...");
            boolean check=false;
            while(!check){
            msg = input.readUTF();
            System.out.println("Resposta recebida: "+msg);
            if (msg.trim().toUpperCase().equals(words.trim().toUpperCase())) {
                System.out.println("Resposta correta!");
                check=true;
                outpout.writeBoolean(check);
            }else{
                outpout.writeBoolean(check);
                System.out.println("Resposta incorreta, aguardando nova tentativa...");
            }
            outpout.flush();
            }
            input.close(); //fecha stream de entrada
            outpout.close(); // fecha stream de saída            
        } catch (IOException ex) {
            //tratamento de falhas
            System.out.println("Problema no tratamento da conexão com o cliente: "+
                    socket.getInetAddress());
            System.out.println("Erro: "+ex.getMessage());
        } finally {
            //final do tratamento do protocolo (comunicação)
            //fechar socket de comunicação entre client/servidor
            
            fechaSocket(socket);
        }
    }
    
    public static boolean isValid(String words) {
		// check for the null value
		if (words == null) {
			return false;
		}
		
		String[] arrWord = words.split(" ");
		
		// check if the array's length is less then 3
		if (arrWord.length != 1) {
			return false;
		}
		
		// check if each words not exceed 10 chars length
		for (int counter = 0; counter <= arrWord.length - 1; counter++) {
			if (arrWord[counter].length() > 9) {
				return false;
			}
		}
		
		// we can check for more conditions here but I think it's enough
		// to have those basic conditions
		
		return true;
	}
	
	public static void showCrosswords(String words, ObjectOutputStream outputreply) throws IOException {
            	
                String[] arrWord = words.split(" ");
		
		// create a matrix of 10x10
		matrix = new String[10][10];
		String reply = null;
		// fill it with asterix
		for (int row = 0; row <= 9; row++) {
			for (int col = 0; col <= 9; col++) {
				matrix[row][col] = "*";
			}
		}
		
		// put the 3 words randomly in matrix
		for (int counter = 0; counter <= arrWord.length - 1; counter++) {
			String word = arrWord[counter];
			putWord(word);
		}
	
		// formed the final crossword
		// put random chars on unoccupied cells
		
		System.out.println();
		reply = "\n";
		//reply = ("X" + " 1 2 3 4 5 6 7 8 9 0\n");
		
		for (int row = 0; row <= 9; row++) {
			for (int col = 0; col <= 9; col++) {
				if (matrix[row][col].equals(("*"))) {
					matrix[row][col] = anyRandomChar();
				}
				
				//if (col == 0) {
				//	reply = reply + (((row + 1) % 10) + " " + matrix[row][col] + " ");
				//} else {
					reply = reply + (matrix[row][col] + " ");
				//}
			}
			reply = reply + ("");
                        reply = reply + ("\n");
		}
                outputreply.writeUTF(reply);
	}
	
	public static void putWord(String word) {
		System.out.println("Tentando adiconar a palavra: " + word);
		
		Random random = new Random();
		boolean isPutted = false;
		
		while (!isPutted) {
			int row = random.nextInt(10 - word.length() + 1);
			int col = random.nextInt(10 - word.length() + 1);
			int direction = random.nextInt(3);
			
			boolean isClear = true;

			switch (direction) {
			case RIGHT:
				for (int counter = col; counter <= col + word.length() - 1; counter++) {
					if (!matrix[row][counter].equals("*")) {
						isClear = false;
					}
				}
				
				if (isClear) {
					for (int counter = col; counter <= col + word.length() - 1; counter++) {
						matrix[row][counter] = word.substring(counter - col, counter - col + 1).toUpperCase();
					}
					
					System.out.println("A palavra " + word + " está adicionada a DIREITA em " + (row + 1) + ", " + (col + 1));
					isPutted = true;
				}
			
				break;
				
			case DIAGONAL:
				int cols = col;
				
				for (int pos = row; pos <= row + word.length() - 1; pos++) {
					if (!matrix[pos][cols++].equals("*")) {
						isClear = false;
						break;
					}
				}
				
				if (isClear) {
					cols = col;
					
					for (int pos = row; pos <= row + word.length() - 1; pos++) {
						matrix[pos][cols++] = word.substring(pos - row, pos - row + 1).toUpperCase();
					}
					
					System.out.println("A palavra " + word + " está adicionada DIAGONALMENTE em " + (row + 1) + ", " + (col + 1));
					isPutted = true;
				}
				
				break;
				
			case DOWN: 
				for (int counter = row; counter <= row + word.length() - 1; counter++) {
					if (!matrix[counter][col].equals("*")) {
						isClear = false;
					}
				}
				
				if (isClear) {
					for (int counter = row; counter <= row + word.length() - 1; counter++) {
						matrix[counter][col] = word.substring(counter - row, counter - row + 1).toUpperCase();
					}
					
					System.out.println("A palavra " + word + " está adicionada PARA BAIXO em " + (row + 1) + ", " + (col + 1));
					isPutted = true;
				}
				
				break;
			}
		}
	}
        
    public static String anyRandomChar() {
            Random random = new Random();
            String result = new String(Character.toChars(65 + random.nextInt(26)));
            //System.out.println("Random char: " + result);
            return result;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            Server server = new Server();
            server.criarServerSocket(5555);
            System.out.println("Aguardando conexão do jogador..");
            while (true) {                
                Socket socket = server.esperaConexao();
                System.out.println("Cliente conectado");
                server.trataConexao(socket);
                System.out.println("Cliente finalizado, aguardando nova conexao\n");   
            }
        }catch(IOException e){
            
        }
    }
    
}
