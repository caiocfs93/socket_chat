
/**
 *
 * @author caioc
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

public class ServidorSocket extends Thread {

    private static Vector clientes;
    private Socket conexao;
    private String nomeCliente;
    private static List<String> listaDeNomes = new ArrayList();

    public static Vector getClientes() {
        return clientes;
    }

    public static void setClientes(Vector clientes) {
        ServidorSocket.clientes = clientes;
    }

    ServidorSocket() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Socket getConexao() {
        return conexao;
    }

    public void setConexao(Socket conexao) {
        this.conexao = conexao;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public static List<String> getListaDeNomes() {
        return listaDeNomes;
    }

    public static void setListaDeNomes(List<String> listaDeNomes) {
        ServidorSocket.listaDeNomes = listaDeNomes;
    }

    public ServidorSocket(Socket socket) {
        this.conexao = socket;
    }

    public boolean armazenar(String nomeNovo) {
        for (int i = 0; i < listaDeNomes.size(); i++) {
            if (listaDeNomes.get(i).equals(nomeNovo)) {
                return true;
            }
        }
        listaDeNomes.add(nomeNovo);
        return false;
    }

    public void remover(String nomeAntigo) {
        for (int i = 0; i < listaDeNomes.size(); i++) {
            if (listaDeNomes.get(i).equals(nomeAntigo)) {
                listaDeNomes.remove(nomeAntigo);
            }
        }
    }

    public static void main(String args[]) {
        clientes = new Vector();
        try {
            ServerSocket server = new ServerSocket(12345);
            System.out.println("ServidorSocket rodando na porta 12345");
            while (true) {
                Socket conexao = server.accept();
                Thread t = new ServidorSocket(conexao);
                t.start();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }

    public void run() {
        try {
            BufferedReader entrada
                    = new BufferedReader(new InputStreamReader(this.conexao.getInputStream()));

            PrintStream saida = new PrintStream(this.conexao.getOutputStream());
            this.nomeCliente = entrada.readLine();
            if (armazenar(this.nomeCliente)) {
                saida.println("Este nome ja existe! Conecte novamente com outro nome.");
                clientes.add(saida);
                this.conexao.close();
                return;
            } else {
                System.out.println(this.nomeCliente + " está conectado ao servidor.");
            }
            if (this.nomeCliente == null) {
                return;
            }
            clientes.add(saida);
            String msg = entrada.readLine();
            while (msg != null && !(msg.trim().equals(""))) {
                enviarParaTodos(saida, " escreveu: ", msg);
                msg = entrada.readLine();
            }
            System.out.println(this.nomeCliente + " saiu do bate-papo.");
            enviarParaTodos(saida, " saiu", " do bate-papo.");
            remover(this.nomeCliente);
            clientes.remove(saida);
            this.conexao.close();
        } catch (IOException e) {
            System.err.println("Falha na Conexão..." + " IOException: " + e);
        }
    }

    public void enviarParaTodos(PrintStream saida, String acao, String msg) throws IOException {
        Enumeration e = clientes.elements();
        while (e.hasMoreElements()) {
            PrintStream chat = (PrintStream) e.nextElement();
            if (chat != saida) {
                chat.println(this.nomeCliente + acao + msg);
            }
        }
    }
}
