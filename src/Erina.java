import greenfoot.*;

public final class Erina extends World {
	private static final int WORLD_WIDTH = 1024;
    private static final int WORLD_HEIGHT = 720;
    
    private static final String BGM_FILENAME = "resources/17 Disc Wars 1.wav";
    private static final GreenfootSound BGM = new GreenfootSound(BGM_FILENAME);
    
    public Erina() {
    	super(WORLD_WIDTH, WORLD_HEIGHT, 1);
    	
    	System.out.println("Welcome to The Erina!");
    }
    
    
    @Override
    public void act() {
    	
    }
}