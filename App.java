import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;

import controller.CamembertController;
import controller.ICamembertController;
import data.Item;
import model.CamembertModel;
import model.ICamembertModel;
import view.CamembertView;
import view.ICamembertView;

/**
 * App
 * 
 * @author Dorian Bouillet
 * @author Nils Richard
 */
public class App {

	public static void main(String[] a) {
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setMinimumSize(new Dimension(750, 600));
		window.setBounds(30, 30, 400, 400);

		// Create an instance of the model
		Item item1 = new Item("Loyer", "Le loyer à payer chaque mois", 470);
		Item item2 = new Item("Alimentation", "Il faut bien manger pour survivre", 230);
		Item item3 = new Item("Spotify", "Pour la musique", 5);

		ArrayList<Item> items = new ArrayList<>();
		items.add(item1);
		items.add(item2);
		items.add(item3);

		ICamembertModel model = new CamembertModel("Budget", "Euros", items);

		// Create the controller and the view and link all together
		ICamembertView view = new CamembertView(model);
		ICamembertController controller = new CamembertController(view, model);
		view.setController(controller);

		// display layout
		GridLayout layout = new GridLayout(1, 2);

		window.getContentPane().add(controller.getView());

		window.setLayout(layout);
		window.pack();
		window.setVisible(true);
		window.pack();
	}

}
