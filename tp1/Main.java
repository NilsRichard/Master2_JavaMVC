import javax.swing.JFrame;

import controller.CamembertController;
import controller.ICamembertController;
import data.Item;
import model.CamembertModel;
import model.ICamembertModel;
import view.CamembertView;
import view.ICamembertView;

import java.awt.GridLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] a) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setMinimumSize(new Dimension(750, 600));
        window.setBounds(30, 30, 400, 400);

        // Create an instance of the model
        Item item1 = new Item("Loyer", "Le loyer a payer chaque mois", 470);
        Item item2 = new Item("Alimentation", "Il faut bien manger pour survivre", 230);
        Item item3 = new Item("Spotify", "Pour la musique", 5);

        ArrayList<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        items.add(item3);

        ICamembertModel model = new CamembertModel("Budget", "Euros", items);

        // Maybe put some data in the model
        int oldFirst = 0;
        int oldLast = 0;

        // Create the controller and the view and link all together
        ICamembertView view = new CamembertView(model);
        ICamembertController controller = new CamembertController(view);
        view.setController(controller);

        // display layout
        GridLayout layout = new GridLayout(1, 2);

        window.getContentPane().add(controller.getView());

        window.setLayout(layout);
        window.pack();
        window.setVisible(true);
        //window.pack();
    }

}
