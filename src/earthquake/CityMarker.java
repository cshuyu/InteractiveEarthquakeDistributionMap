package earthquake;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import processing.core.PConstants;
import processing.core.PGraphics;

/** Implements a visual marker for cities on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * 
 */
public class CityMarker extends CommonMarker {
	private int countOfThreat;
	private float aveOfMagnitude;
	private String recentEarthquake;
	private List<ScreenPosition> threatenLocs;
	private ScreenPosition screenPosition;
	private boolean isThreatLocsAdded;
	public static int TRI_SIZE = 5;  // The size of the triangle marker


	public CityMarker(Location location) {
		super(location);
		countOfThreat = -1;
		aveOfMagnitude = (float)-1;
		recentEarthquake = null;
		threatenLocs = new ArrayList<ScreenPosition>();
		isThreatLocsAdded = false;
	}
	
	public CityMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
		// Cities have properties: "name" (city name), "country" (country name)
		// and "population" (population, in millions)
		countOfThreat = -1;
		aveOfMagnitude = (float)-1;
		recentEarthquake = null;
		threatenLocs = new ArrayList<ScreenPosition>();
		isThreatLocsAdded = false;
	}
	
	
	// pg is the graphics object on which you call the graphics
	// methods.  e.g. pg.fill(255, 0, 0) will set the color to red
	// x and y are the center of the object to draw. 
	// They will be used to calculate the coordinates to pass
	// into any shape drawing methods.  
	// e.g. pg.rect(x, y, 10, 10) will draw a 10x10 square
	// whose upper left corner is at position x, y
	/**
	 * Implementation of method to draw marker on the map.
	 */
	public void drawMarker(PGraphics pg, float x, float y) {
		//System.out.println("Drawing a city");
		// Save previous drawing style
		pg.pushStyle();
		// IMPLEMENT: drawing triangle for each city
		pg.fill(150, 30, 30);
		pg.triangle(x, y-TRI_SIZE, x-TRI_SIZE, y+TRI_SIZE, x+TRI_SIZE, y+TRI_SIZE);
		if (clicked) {
			showPopMenu(pg, x, y);
			if(threatenLocs.size()>0)
				drawConnection(pg, x, y);
		}
		else if(threatenLocs.size()>0) {
			drawClear(pg, x, y);
		}
		// Restore previous drawing style
		pg.popStyle();
	}
	
	public void setCountOfThreat(int cot) {
		countOfThreat = cot;
	}
	
	public int getCountOfThreat(){
		return countOfThreat;
	}
	
	public void setAveOfMagnitude(float aom) {
		aveOfMagnitude = aom;
	}
	
	public float getAveOfMagnitude() {
		return aveOfMagnitude;
	}
	
	public void setRecentEarthquake(String re){
		recentEarthquake = re;
	}
	
	public String getRecentEarthquake() {
		return recentEarthquake;
	}
	
	public void setScreenPosition(ScreenPosition sp){
		screenPosition = sp;
	}
	
	public ScreenPosition getScreenPosition() {
		return screenPosition;
	}
	
	public void addThreatenLoc(ScreenPosition tl){
		threatenLocs.add(tl);
	}
	
	public boolean isThreatLocsAdded() {
		return isThreatLocsAdded;
	}
	
	public void setTheatLocsAdded(boolean b){
		isThreatLocsAdded = b;
	}
	
	
	public void showPopMenu(PGraphics pg, float x, float y) {
		String row1 = "There are " + countOfThreat + " earthquakes within threatCircle."; 
		String row2 = String.format("%s %.2f", "The average magnitude within threatCircle is",
				aveOfMagnitude);
		String row3 = "The most recent earthquake is occured in " + recentEarthquake;
		float textWidth = Math.max(pg.textWidth(row1),
				Math.max(pg.textWidth(row2),pg.textWidth(row3)));
		
		pg.pushStyle();
		pg.fill(255, 255, 255);
		pg.textSize(16);
		pg.rect(x-textWidth/2-30, y+TRI_SIZE+25, textWidth+150, 90);
		pg.fill(0, 0, 0);
		pg.text(row1, x-textWidth/2-15, y+TRI_SIZE+50);
		pg.text(row2, x-textWidth/2-15, y+TRI_SIZE+75);
		pg.text(row3, x-textWidth/2-15, y+TRI_SIZE+100);
		pg.popStyle();
	}
	
	public void drawConnection(PGraphics pg, float x, float y) {	
		pg.strokeWeight(2);
		for(ScreenPosition threatenLoc : threatenLocs) {
			pg.line(screenPosition.x + x-screenPosition.x, screenPosition.y + y-screenPosition.y, 
					threatenLoc.x + x-screenPosition.x, threatenLoc.y + y-screenPosition.y);
		}
	}
	
	public void drawClear(PGraphics pg, float x, float y) {	
		pg.strokeWeight(2);
		pg.noStroke();
		for(ScreenPosition threatenLoc : threatenLocs) {
			pg.line(screenPosition.x + x-screenPosition.x, screenPosition.y + y-screenPosition.y, 
					threatenLoc.x + x-screenPosition.x, threatenLoc.y + y-screenPosition.y);
			System.out.println(screenPosition.x + "," + screenPosition.y);
			System.out.println("thretenLoc" + threatenLoc.x + "," +threatenLoc.y);
		}
	}	
	
	/** Show the title of the city if this marker is selected */
	public void showTitle(PGraphics pg, float x, float y)
	{
		String name = getCity() + " " + getCountry() + " ";
		String pop = "Pop: " + getPopulation() + " Million";
		
		pg.pushStyle();
		
		pg.fill(255, 255, 255);
		pg.textSize(12);
		pg.rectMode(PConstants.CORNER);
		pg.rect(x, y-TRI_SIZE-39, Math.max(pg.textWidth(name), pg.textWidth(pop)) + 6, 39);
		pg.fill(0, 0, 0);
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.text(name, x+3, y-TRI_SIZE-33);
		pg.text(pop, x+3, y - TRI_SIZE -18);
		
		pg.popStyle();
	}
	
	private String getCity()
	{
		return getStringProperty("name");
	}
	
	private String getCountry()
	{
		return getStringProperty("country");
	}
	
	private float getPopulation()
	{
		return Float.parseFloat(getStringProperty("population"));
	}
}
