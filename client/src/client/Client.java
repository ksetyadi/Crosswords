/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thiego
 */
public class Client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //System.out.println(args.clone());
        String words = null;
        BufferedReader reader;
	reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("Qual o seu nome jogador? ");
            try {
			words = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return;
            }
            Socket socket = new Socket("localhost", 5555);
            System.out.println("Servidor conectado");
            System.out.println("Olá, "+words+". Idenficação enviada, aguardando desafio...");
            ObjectOutputStream outpout = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            outpout.writeUTF(words);
            outpout.flush();
            words = input.readUTF();
            System.out.println(words);
            System.out.println("Qual a palavra relacionada com SD disposta na matriz: ");
            words = reader.readLine();
            outpout.writeUTF(words);
            outpout.flush();
            while(!input.readBoolean()){
                System.out.println("Resposta incorreta, tente novamente: ");
                words = reader.readLine();
                outpout.writeUTF(words);
                outpout.flush();
            }
            System.out.println("Resposta correta!");
            input.close();
            outpout.close();
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        //}else{
        //    System.out.println("Necessário informar o IP do servidor como parametro");
        //   System.exit(0);
        //}
    }
}