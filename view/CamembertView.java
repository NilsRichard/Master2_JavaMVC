package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

import controller.ICamembertController;
import model.ICamembertModel;

/**
 * CamembertView
 * 
 * @author Dorian Bouillet
 * @author Nils Richard
 */
public class CamembertView extends JComponent implements MouseListener, MouseMotionListener, Observer, ICamembertView {

	private static final long serialVersionUID = -3890581956506829542L;

	static final Point2D pieCenter = new Point2D.Double(300, 300);
	static final Dimension pieSize = new Dimension(300, 300);
	static final double pieThickness = 50;
	static final Dimension selectedPieSize = new Dimension(320, 320);

	static final double tagOffset = 20;
	static final Dimension tagSizeSelected = new Dimension(130, 100);
	static final Dimension tagSizeNotSelected = new Dimension(100, 30);

	static final Dimension pieCenterSize = new Dimension(150, 150);

	static final double pieRadialGap = 1.0; // < one degree

	ArrayList<Arc2D> arcs;

	ArrayList<Arc2D> selectedArcs;

	Arc2D emptyCenter;

	Arc2D center;

	// a link to the controller interface
	ICamembertController controller;

	// a link to the Model interface
	ICamembertModel model;

	double startingAngle;

	GeneralPath previous;
	GeneralPath next;

	double prevPosX;
	double prevPosY;

	Graphics2D g2d;

	Image offscreen;

	Font fontCenter;
	Font fontTags;

	boolean mousePressed = false;

	/**
	 * Constructor
	 * 
	 * @param m the model linked to the view
	 */
	public CamembertView(ICamembertModel m) {
		model = m;
		startingAngle = 0.0;

		// reminder: we don't want the model to have an oberserver: use an adapter
		// ((CamembertModel) model).addObserver(this);

		arcs = new ArrayList<Arc2D>();
		selectedArcs = new ArrayList<Arc2D>();

		addMouseListener(this);
		addMouseMotionListener(this);

		setSize(600, 600);

		buildGraphics();

	}

	/**
	 * Build the Arc2D (the pieces of pie)
	 */
	public void buildGraphics() {

		// create previous button
		int x1Points[] = { 20, 40, 20 };
		int y1Points[] = { 25, 45, 45 };
		previous = new GeneralPath(GeneralPath.WIND_EVEN_ODD, x1Points.length);
		previous.moveTo(x1Points[0], y1Points[0]);

		for (int index = 1; index < x1Points.length; index++) {
			previous.lineTo(x1Points[index], y1Points[index]);
		}
		;

		previous.closePath();

		// create next button
		int x1PointsN[] = { 25, 45, 45 };
		int y1PointsN[] = { 20, 20, 40 };
		next = new GeneralPath(GeneralPath.WIND_EVEN_ODD, x1PointsN.length);
		next.moveTo(x1PointsN[0], y1PointsN[0]);

		for (int index = 1; index < x1Points.length; index++) {
			next.lineTo(x1PointsN[index], y1PointsN[index]);
		}
		;

		next.closePath();

		// create non-selected arcs
		arcs.clear();
		double angle = startingAngle;
		for (int i = 0; i < model.size(); i++) {

			Arc2D arc = new Arc2D.Double(pieCenter.getX() - pieSize.width / 2, pieCenter.getY() - pieSize.height / 2,
					pieSize.width, pieSize.height, angle, model.getValue(i) / model.total() * 360 - pieRadialGap,
					Arc2D.PIE);
			arcs.add(arc);
			angle += model.getValue(i) / model.total() * 360;
		}

		// create selected arcs
		selectedArcs.clear();
		angle = startingAngle;
		for (int i = 0; i < model.size(); i++) {

			Arc2D arc = new Arc2D.Double(pieCenter.getX() - selectedPieSize.width / 2,
					pieCenter.getY() - selectedPieSize.height / 2, selectedPieSize.width, selectedPieSize.height, angle,
					model.getValue(i) / model.total() * 360 - pieRadialGap, Arc2D.PIE);
			selectedArcs.add(arc);
			angle += model.getValue(i) / model.total() * 360;
		}

		// create central arcs
		emptyCenter = new Arc2D.Double(pieCenter.getX() - (pieSize.width / 2 - pieThickness),
				pieCenter.getY() - (pieSize.height / 2 - pieThickness), pieSize.width - 2 * pieThickness,
				pieSize.width - 2 * pieThickness, 0, 360, Arc2D.PIE);

		center = new Arc2D.Double(pieCenter.getX() - pieCenterSize.width / 2,
				pieCenter.getY() - pieCenterSize.height / 2, pieCenterSize.width, pieCenterSize.height, 0, 360,
				Arc2D.PIE);

		fontCenter = new Font("Arial", Font.BOLD, 14);
		fontTags = new Font("Serial", Font.PLAIN, 12);

	}

	/**
	 * Compute rotation
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void computeRotation(int x, int y) {
		double dx = pieCenter.getX() - x;
		double dy = pieCenter.getY() - y;
		double angle1 = Math.atan2(dy, dx) / Math.PI * 180;

		dx = pieCenter.getX() - prevPosX;
		dy = pieCenter.getY() - prevPosY;
		double angle2 = Math.atan2(dy, dx) / Math.PI * 180;

		startingAngle += (angle2 - angle1);

		prevPosX = x;
		prevPosY = y;
	}

	/**
	 * Deselect all pieces
	 */
	public void deSelect() {
		controller.setSelected(false);
		paint(getGraphics());
	}

	/**
	 * Draw the previous and next buttons
	 */
	private void drawPreviousNextButtons(Graphics2D g2d) {

		g2d.setColor(Color.RED);
		g2d.fill(previous);
		g2d.fill(next);

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

		// if (center.contains(arg0.getX(), arg0.getY())) {
		// // On est dans le centre
		// } else {
		boolean onDeselectPosition = true;
		for (int i = 0; i < arcs.size(); i++) {
			if (arcs.get(i).contains(arg0.getX(), arg0.getY()) && !emptyCenter.contains(arg0.getX(), arg0.getY())) {
				selectPie(i);
				onDeselectPosition = false;
			}
		}

		// }

		if (previous.contains(arg0.getX(), arg0.getY())) {
			nextPie();
			onDeselectPosition = false;
		}

		if (next.contains(arg0.getX(), arg0.getY())) {
			previousPie();
			onDeselectPosition = false;
		}

		if (onDeselectPosition)
			deSelect();

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// if the user drags a pie we rotate it by a given angle 'angle1'

		// difference in x from center:
		double dx = pieCenter.getX() - e.getX();
		double dy = pieCenter.getY() - e.getY();
		double angle1 = Math.atan2(dy, dx) / Math.PI * 180;

		dx = pieCenter.getX() - prevPosX;
		dy = pieCenter.getY() - prevPosY;
		double angle2 = Math.atan2(dy, dx) / Math.PI * 180;

		startingAngle += (angle2 - angle1);

		prevPosX = e.getX();
		prevPosY = e.getY();

		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		mousePressed = false;
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		mousePressed = false;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (mousePressed)
			mouseDragged(e);

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		prevPosX = arg0.getX();
		prevPosY = arg0.getY();

		boolean onArc = false;
		for (int i = 0; i < arcs.size(); i++) {
			if (arcs.get(i).contains(arg0.getX(), arg0.getY()) && !emptyCenter.contains(arg0.getX(), arg0.getY())) {
				onArc = true;
			}
		}
		if (onArc)
			mousePressed = true;

	}

	// if the user clicks on a pie, it gets selected, otherwise you deselect all.

	@Override
	public void mouseReleased(MouseEvent arg0) {
		mousePressed = false;
		mouseDragged(arg0);
	}

	/**
	 * Select the next piece of pie
	 */
	public void nextPie() {
		int pie = (controller.getSelectedPie() + 1) % model.size();
		if (pie < 0)
			pie = -pie;
		controller.setSelectedPie(pie);
		System.out.println("Selected pie : " + controller.getSelectedPie());
		paint(getGraphics());

	}

	/**
	 * How to draw the Pie Chart. Called everytime a refresh is performed by the UI
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Dimension d = getSize();
		if (offscreen == null || offscreen.getWidth(null) != d.width || offscreen.getHeight(null) != d.height) {
			offscreen = createImage(d.width, d.height);
		}

		Graphics offG = offscreen.getGraphics();
		offG.setColor(getBackground());
		offG.fillRect(0, 0, d.width, d.height);

		// Draw into the offscreen image.

		// g2d = (Graphics2D) g;
		g2d = (Graphics2D) offG;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setColor(Color.WHITE);
		Rectangle2D rect2D = new Rectangle(0, 0, d.width, d.height);
		g2d.fill(rect2D);

		if (controller.isSelected()) {

			drawPreviousNextButtons(g2d);
		}

		double angle = startingAngle;
		for (int i = 0; i < model.size(); i++) {

			if (controller.isSelected() && controller.getSelectedPie() == i) {
				g2d.setColor(new Color(0, 100, (100 + 20 * i) % 255));
				Arc2D arc = selectedArcs.get(i);
				arc.setAngleStart(angle);
				arc.setAngleExtent(model.getValue(i) / model.total() * 360 - pieRadialGap);
				g2d.fill(arc);

				// draw detailed information
			} else {
				g2d.setColor(new Color(100, 100, (100 + 20 * i) % 255));
				Arc2D arc = arcs.get(i);
				arc.setAngleStart(angle);
				arc.setAngleExtent(model.getValue(i) / model.total() * 360 - pieRadialGap);
				g2d.fill(arc);
			}
			angle += model.getValue(i) / model.total() * 360;
		}

		g2d.setColor(Color.WHITE);
		g2d.fill(emptyCenter);

		GradientPaint gp = new GradientPaint(0, 110, Color.WHITE, 0, 150, Color.BLUE);
		g2d.setPaint(gp);
		// g2d.setColor(Color.BLUE);
		g2d.fill(center);

		g2d.setFont(fontCenter);
		g2d.setColor(Color.WHITE);
		g2d.drawString(model.getTitle(),
				(int) (pieCenter.getX() - model.getTitle().length() / 2 * fontCenter.getSize() * 0.7),
				(int) pieCenter.getY() - fontCenter.getSize());

		g2d.setFont(fontTags);
		g2d.setColor(Color.WHITE);
		String total = "" + model.total() + " " + model.getUnit();
		g2d.drawString(total, (int) (pieCenter.getX() - total.length() / 2 * fontTags.getSize() * 0.7),
				(int) pieCenter.getY() - fontTags.getSize() + 20);
		// draw tags
		if (!controller.isSelected()) {

			angle = startingAngle;
			for (int i = 0; i < model.size(); i++) {

				double midangle = angle + model.getValue(i) / model.total() * 360.0 / 2.0;

				double x = pieCenter.getX() + positionXOnCircle(pieSize.getWidth() / 2 + tagOffset, midangle);
				double y = pieCenter.getY() - positionYOnCircle(pieSize.getHeight() / 2 + tagOffset, midangle);

				g2d.setColor(new Color(100, 100, (100 + 20 * i) % 255));
				if (y < pieCenter.getY()) {
					if (x > pieCenter.getX()) { // top right
						Rectangle2D tag = new Rectangle2D.Double(x, y - tagSizeNotSelected.height,
								tagSizeNotSelected.width, tagSizeNotSelected.height);
						g2d.fill(tag);
						GeneralPath stem = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);
						stem.moveTo(pieCenter.getX() + positionXOnCircle(pieSize.width / 2, midangle),
								pieCenter.getY() - positionYOnCircle(pieSize.width / 2, midangle));
						stem.lineTo(x + 1, y - tagSizeNotSelected.height);
						stem.lineTo(x + 1, y + 3);
						stem.closePath();
						g2d.fill(stem);
						g2d.setColor(Color.black);
						Rectangle2D tagUnderline = new Rectangle2D.Double(x, y, tagSizeNotSelected.width, 3);
						g2d.fill(tagUnderline);
						g2d.setColor(Color.WHITE);
						g2d.drawString(model.getTitle(i), (int) x + 15, (int) y - tagSizeNotSelected.height + 15);

					} else { // top left
						Rectangle2D tag = new Rectangle2D.Double(x - tagSizeNotSelected.width,
								y - tagSizeNotSelected.height, tagSizeNotSelected.width, tagSizeNotSelected.height);
						g2d.fill(tag);
						GeneralPath stem = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);
						stem.moveTo(pieCenter.getX() + positionXOnCircle(pieSize.width / 2, midangle),
								pieCenter.getY() - positionYOnCircle(pieSize.width / 2, midangle));
						stem.lineTo(x - 1, y - tagSizeNotSelected.height);
						stem.lineTo(x - 1, y + 3);
						stem.closePath();
						g2d.fill(stem);
						g2d.setColor(Color.black);
						Rectangle2D tagUnderline = new Rectangle2D.Double(x - tagSizeNotSelected.width, y,
								tagSizeNotSelected.width, 3);
						g2d.fill(tagUnderline);
						g2d.setColor(Color.WHITE);
						g2d.drawString(model.getTitle(i), (int) x - tagSizeNotSelected.width + 15,
								(int) y - tagSizeNotSelected.height + 15);

					}
				} else {
					if (x > pieCenter.getX()) { // bottom right
						Rectangle2D tag = new Rectangle2D.Double(x, y, tagSizeNotSelected.width,
								tagSizeNotSelected.height);
						g2d.fill(tag);
						GeneralPath stem = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);
						stem.moveTo(pieCenter.getX() + positionXOnCircle(pieSize.width / 2, midangle),
								pieCenter.getY() - positionYOnCircle(pieSize.width / 2, midangle));
						stem.lineTo(x + 1, y);
						stem.lineTo(x + 1, y + tagSizeNotSelected.height + 3);
						stem.closePath();
						g2d.fill(stem);
						g2d.setColor(Color.black);
						Rectangle2D tagUnderline = new Rectangle2D.Double(x, y + tagSizeNotSelected.height,
								tagSizeNotSelected.width, 3);
						g2d.fill(tagUnderline);
						g2d.setColor(Color.WHITE);
						g2d.drawString(model.getTitle(i), (int) x + 15, (int) y + 15);

					} else { // bottom left
						Rectangle2D tag = new Rectangle2D.Double(x - tagSizeNotSelected.width, y,
								tagSizeNotSelected.width, tagSizeNotSelected.height);
						g2d.fill(tag);
						GeneralPath stem = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);
						stem.moveTo(pieCenter.getX() + positionXOnCircle(pieSize.width / 2, midangle),
								pieCenter.getY() - positionYOnCircle(pieSize.width / 2, midangle));
						stem.lineTo(x - 1, y);
						stem.lineTo(x - 1, y + tagSizeNotSelected.height + 3);
						stem.closePath();
						g2d.fill(stem);
						g2d.setColor(Color.black);
						Rectangle2D tagUnderline = new Rectangle2D.Double(x - tagSizeNotSelected.width,
								y + tagSizeNotSelected.height, tagSizeNotSelected.width, 3);
						g2d.fill(tagUnderline);
						g2d.setColor(Color.WHITE);
						g2d.drawString(model.getTitle(i), (int) x - tagSizeNotSelected.width + 15, (int) y + 15);

					}
				}

				angle += model.getValue(i) / model.total() * 360;

			}
		}

		if (controller.isSelected()) {
			angle = startingAngle;
			for (int i = 0; i < model.size(); i++) {

				if (i == controller.getSelectedPie()) {

					double midangle = angle + model.getValue(i) / model.total() * 360.0 / 2.0;

					double x = pieCenter.getX() + positionXOnCircle(160, midangle);
					double y = pieCenter.getX() - positionYOnCircle(160, midangle);

					g2d.setColor(new Color(100, 100, (100 + 20 * i) % 255));
					if (y < pieCenter.getY()) {
						if (x > pieCenter.getX()) { // top right
							Rectangle2D tag = new Rectangle2D.Double(x, y - tagSizeSelected.height,
									tagSizeSelected.width, tagSizeSelected.height);
							g2d.fill(tag);
							GeneralPath stem = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);
							stem.moveTo(pieCenter.getX() + positionXOnCircle(pieSize.width / 2, midangle),
									pieCenter.getY() - positionYOnCircle(pieSize.height / 2, midangle));
							stem.lineTo(x + 1, y - tagSizeSelected.height);
							stem.lineTo(x + 1, y + 3);
							stem.closePath();
							g2d.fill(stem);
							g2d.setColor(Color.black);
							Rectangle2D tagUnderline = new Rectangle2D.Double(x, y, tagSizeSelected.width, 3);
							g2d.fill(tagUnderline);
							g2d.setColor(Color.WHITE);
							g2d.drawString(model.getTitle(i), (int) x + 15, (int) y - tagSizeSelected.height + 15);
							g2d.drawString("" + model.getValue(i) + " " + model.getUnit(), (int) x + 15,
									(int) y - tagSizeSelected.height + 30);
							//
							x += 15;
							y = y - tagSizeSelected.height + 45;
							AttributedCharacterIterator characterIterator = new AttributedString(
									model.getDescription(i)).getIterator();
							FontRenderContext fontRenderContext = g2d.getFontRenderContext();
							LineBreakMeasurer measurer = new LineBreakMeasurer(characterIterator, fontRenderContext);
							while (measurer.getPosition() < characterIterator.getEndIndex()) {
								TextLayout textLayout = measurer.nextLayout(tagSizeSelected.width - 15);
								y += textLayout.getAscent();
								textLayout.draw(g2d, (int) x, (int) y);
								y += textLayout.getDescent() + textLayout.getLeading();
							}

						} else { // top left
							Rectangle2D tag = new Rectangle2D.Double(x - tagSizeSelected.width,
									y - tagSizeSelected.height, tagSizeSelected.width, tagSizeSelected.height);
							g2d.fill(tag);
							GeneralPath stem = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);
							stem.moveTo(pieCenter.getX() + positionXOnCircle(pieSize.width / 2, midangle),
									pieCenter.getY() - positionYOnCircle(pieSize.height / 2, midangle));
							stem.lineTo(x - 1, y - tagSizeSelected.height);
							stem.lineTo(x - 1, y + 3);
							stem.closePath();
							g2d.fill(stem);
							g2d.setColor(Color.black);
							Rectangle2D tagUnderline = new Rectangle2D.Double(x - tagSizeSelected.width, y,
									tagSizeSelected.width, 3);
							g2d.fill(tagUnderline);
							g2d.setColor(Color.WHITE);
							g2d.drawString(model.getTitle(i), (int) x - tagSizeSelected.width + 15,
									(int) y - tagSizeSelected.height + 15);
							g2d.drawString("" + model.getValue(i) + " " + model.getUnit(),
									(int) x - tagSizeSelected.width + 15, (int) y - tagSizeSelected.height + 30);

							x = x - tagSizeSelected.width + 15;
							y = y - tagSizeSelected.height + 45;
							AttributedCharacterIterator characterIterator = new AttributedString(
									model.getDescription(i)).getIterator();
							FontRenderContext fontRenderContext = g2d.getFontRenderContext();
							LineBreakMeasurer measurer = new LineBreakMeasurer(characterIterator, fontRenderContext);
							while (measurer.getPosition() < characterIterator.getEndIndex()) {
								TextLayout textLayout = measurer.nextLayout(tagSizeSelected.width - 15);
								y += textLayout.getAscent();
								textLayout.draw(g2d, (int) x, (int) y);
								y += textLayout.getDescent() + textLayout.getLeading();
							}

						}
					} else {
						if (x > pieCenter.getX()) { // bottom right
							Rectangle2D tag = new Rectangle2D.Double(x, y, tagSizeSelected.width,
									tagSizeSelected.height);
							g2d.fill(tag);
							GeneralPath stem = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);
							stem.moveTo(pieCenter.getX() + positionXOnCircle(pieSize.width / 2, midangle),
									pieCenter.getY() - positionYOnCircle(pieSize.height / 2, midangle));
							stem.lineTo(x + 1, y);
							stem.lineTo(x + 1, y + tagSizeSelected.height + 3);
							stem.closePath();
							g2d.fill(stem);
							g2d.setColor(Color.black);
							Rectangle2D tagUnderline = new Rectangle2D.Double(x, y + tagSizeSelected.height,
									tagSizeSelected.width, 3);
							g2d.fill(tagUnderline);
							g2d.setColor(Color.WHITE);
							g2d.drawString(model.getTitle(i), (int) x + 15, (int) y + 15);
							g2d.drawString("" + model.getValue(i) + " " + model.getUnit(), (int) x + 15, (int) y + 30);

							x = x + 15;
							y = y + 45;
							AttributedCharacterIterator characterIterator = new AttributedString(
									model.getDescription(i)).getIterator();
							FontRenderContext fontRenderContext = g2d.getFontRenderContext();
							LineBreakMeasurer measurer = new LineBreakMeasurer(characterIterator, fontRenderContext);
							while (measurer.getPosition() < characterIterator.getEndIndex()) {
								TextLayout textLayout = measurer.nextLayout(tagSizeSelected.width - 15);
								y += textLayout.getAscent();
								textLayout.draw(g2d, (int) x, (int) y);
								y += textLayout.getDescent() + textLayout.getLeading();
							}

						} else { // bottom left
							Rectangle2D tag = new Rectangle2D.Double(x - tagSizeSelected.width, y,
									tagSizeSelected.width, tagSizeSelected.height);
							g2d.fill(tag);
							GeneralPath stem = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);
							stem.moveTo(pieCenter.getX() + positionXOnCircle(pieSize.width / 2, midangle),
									pieCenter.getY() - positionYOnCircle(pieSize.height / 2, midangle));
							stem.lineTo(x - 1, y);
							stem.lineTo(x - 1, y + tagSizeSelected.height + 3);
							stem.closePath();
							g2d.fill(stem);
							g2d.setColor(Color.black);
							Rectangle2D tagUnderline = new Rectangle2D.Double(x - tagSizeSelected.width,
									y + tagSizeSelected.height, tagSizeSelected.width, 3);
							g2d.fill(tagUnderline);
							g2d.setColor(Color.WHITE);
							g2d.drawString(model.getTitle(i), (int) x - tagSizeSelected.width + 15, (int) y + 15);
							g2d.drawString("" + model.getValue(i) + " " + model.getUnit(),
									(int) x - tagSizeSelected.width + 15, (int) y + 30);

							x = x - tagSizeSelected.width + 15;
							y = y + 45;
							AttributedCharacterIterator characterIterator = new AttributedString(
									model.getDescription(i)).getIterator();
							FontRenderContext fontRenderContext = g2d.getFontRenderContext();
							LineBreakMeasurer measurer = new LineBreakMeasurer(characterIterator, fontRenderContext);
							while (measurer.getPosition() < characterIterator.getEndIndex()) {
								TextLayout textLayout = measurer.nextLayout(tagSizeSelected.width - 15);
								y += textLayout.getAscent();
								textLayout.draw(g2d, (int) x, (int) y);
								y += textLayout.getDescent() + textLayout.getLeading();
							}

						}
					}
				}

				angle += model.getValue(i) / model.total() * 360;
			}
		}

		// Put the offscreen image on the screen.
		g.drawImage(offscreen, 0, 0, null);

	}

	/**
	 * Get the X position on circle
	 * 
	 * @param radius the radius
	 * @param angle  the angled
	 * @return the X position on circle
	 */
	public double positionXOnCircle(double radius, double angle) {

		return radius * Math.cos(angle * Math.PI / 180.0);
	}

	/**
	 * Get the Y position on circle
	 * 
	 * @param radius the radius
	 * @param angle  the angled
	 * @return the Y position on circle
	 */
	public double positionYOnCircle(double radius, double angle) {
		return radius * Math.sin(angle * Math.PI / 180.0);
	}

	/**
	 * Select the previous piece of pie
	 */
	public void previousPie() {
		int pie = (controller.getSelectedPie() - 1) % model.size();
		if (pie < 0)
			pie = model.size() - 1;
		controller.setSelectedPie(pie);
		System.out.println("Selected pie : " + controller.getSelectedPie());

		paint(getGraphics());
	}

	/**
	 * Select a piece of pie
	 * 
	 * @param i the number of the piece to select
	 */
	public void selectPie(int i) {
		controller.setSelected(true);
		controller.setSelectedPie(i);
		System.out.println("Selected pie : " + i);
		paint(getGraphics());
	}

	/**
	 * Set the controller
	 * 
	 * @param c the controller to set
	 */
	public void setController(ICamembertController c) {
		controller = c;
	}

	@Override
	public void update(Observable arg0, Object arg1) {

		buildGraphics();
		paint(getGraphics());

	}

}
