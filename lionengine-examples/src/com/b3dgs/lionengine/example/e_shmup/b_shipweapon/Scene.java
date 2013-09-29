package com.b3dgs.lionengine.example.e_shmup.b_shipweapon;

import com.b3dgs.lionengine.Graphic;
import com.b3dgs.lionengine.Loader;
import com.b3dgs.lionengine.Resolution;
import com.b3dgs.lionengine.Sequence;
import com.b3dgs.lionengine.input.Keyboard;

/**
 * Game loop designed to handle our little world.
 */
final class Scene
        extends Sequence
{
    /** Native resolution. */
    private static final Resolution NATIVE = new Resolution(320, 200, 60);

    /** World reference. */
    private final World world;

    /**
     * Constructor.
     * 
     * @param loader The loader reference.
     */
    Scene(Loader loader)
    {
        super(loader, Scene.NATIVE);
        world = new World(this);
        setMouseVisible(false);
    }

    /*
     * Sequence
     */

    @Override
    protected void load()
    {
        // Nothing to load here
    }

    @Override
    protected void update(double extrp)
    {
        world.update(extrp);
        if (keyboard.isPressed(Keyboard.ESCAPE))
        {
            end();
        }
    }

    @Override
    protected void render(Graphic g)
    {
        world.render(g);
    }
}