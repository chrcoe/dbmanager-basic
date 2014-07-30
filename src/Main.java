import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * Creates the DBFrame object which starts the whole program.
 *
 * @author chrcoe
 *
 */
public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        DBFrame dbf = new DBFrame();
        dbf.setTitle("DBManager");
        dbf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dbf.setResizable(false);
        dbf.setPreferredSize(new Dimension(425,360));
        dbf.pack();
        dbf.setLocationRelativeTo(null);
        dbf.setVisible(true);
    }

}
