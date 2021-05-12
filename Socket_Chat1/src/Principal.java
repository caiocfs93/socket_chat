
/**
 *
 * @author caioc
 */
import javax.swing.JOptionPane;

public class Principal {

    public static void main(String[] args) {
        String ip = (String) JOptionPane.showInputDialog("Informe o IP: ", "192.168.88.38");
        int porta = Integer.parseInt(JOptionPane.showInputDialog("Informe a Porta: ", "12345"));

        Conexao c = new Conexao(ip, porta);

        JanelaChat j = new JanelaChat(c);
        j.setLocationRelativeTo(null);
        j.setVisible(true);
    }
}
