import greenfoot.*; 
import java.util.List;

/**
 * Mouse to monitor hoverables and clickables (right now only buttons)
 */
public class Mouse {
    
    private Renderer world; //world
    
    /**
     * constructor sets world
     */
    public Mouse(Renderer world) {
        this.world = world;
    }
    
    /**
     * act monitors all hoverables and clickables in world
     * if mouse moved off hovered calls onMove
     * if mouse moved on hovered calls onHover
     * if mouse clicks clickable calls clicked passing world and mousebutton used
     */
    public void act() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if(mouse != null) {
            List<Hoverable> hoverable = world.getActive(Hoverable.class);
            List<Hoverable> hovered = world.getActiveAt(mouse.getX(), mouse.getY(), Hoverable.class);
            
            for(Hoverable h : hovered) {
                if(!h.hover()) {
                    h.onHover();
                }
                hoverable.remove(h);
            }
            
            for(Hoverable h : hoverable) {
                if(h.hover()) {
                    h.onMove();
                }
            }
            if(mouse.getButton() != 0) {
                List<Clickable> clicked = world.getActiveAt(mouse.getX(), mouse.getY(), Clickable.class);
                for(Clickable c : clicked) {
                    c.onClick(world, mouse.getButton());
                }
            }
        }
    }
}
