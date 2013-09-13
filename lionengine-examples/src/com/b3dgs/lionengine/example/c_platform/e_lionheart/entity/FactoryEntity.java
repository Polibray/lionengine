package com.b3dgs.lionengine.example.c_platform.e_lionheart.entity;

import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.AppLionheart;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.Context;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.TypeWorld;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.entity.item.FactoryEntityItem;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.entity.monster.FactoryEntityMonster;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.entity.player.Valdyn;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.entity.scenery.FactoryEntityScenery;
import com.b3dgs.lionengine.game.entity.FactoryEntityGame;
import com.b3dgs.lionengine.game.entity.SetupEntityGame;

/**
 * Handle the entity creation by containing all necessary object for their instantiation.
 */
public final class FactoryEntity
        extends FactoryEntityGame<TypeEntity, SetupEntityGame, Entity>
{
    /** Unknown entity error message. */
    public static final String UNKNOWN_ENTITY_ERROR = "Unknown entity: ";
    /** Context used. */
    private Context context;
    /** World used. */
    private TypeWorld world;

    /**
     * Standard constructor.
     */
    public FactoryEntity()
    {
        super(TypeEntity.class);

    }

    /**
     * Set the context.
     * 
     * @param context The context reference.
     */
    public void setContext(Context context)
    {
        this.context = context;
    }

    /**
     * Set the world type used.
     * 
     * @param world The world used.
     */
    public void setWorld(TypeWorld world)
    {
        this.world = world;
    }

    /**
     * Create a new valdyn.
     * 
     * @return The instance of valdyn.
     */
    public Valdyn createValdyn()
    {
        return new Valdyn(context);
    }

    /*
     * FactoryEntityGame
     */

    @Override
    public Entity createEntity(TypeEntity type)
    {
        switch (type.getCategory())
        {
            case ITEM:
                return FactoryEntityItem.createItem(context, type);
            case MONSTER:
                return FactoryEntityMonster.createMonster(context, type);
            case SCENERY:
                return FactoryEntityScenery.createScenery(context, type);
            default:
                throw new LionEngineException(FactoryEntity.UNKNOWN_ENTITY_ERROR + type);
        }
    }

    @Override
    protected SetupEntityGame createSetup(TypeEntity id)
    {
        final String pathBase = Media.getPath(AppLionheart.ENTITIES_DIR, id.getCategory().getFolder());
        final String configExtension = AppLionheart.CONFIG_FILE_EXTENSION;
        final String path;
        if (id == TypeEntity.VALDYN)
        {
            path = Media.getPath(pathBase, id.asPathName() + configExtension);
        }
        else
        {
            path = Media.getPath(pathBase, world.asPathName(), id.asPathName() + configExtension);
        }
        return new SetupEntityGame(Media.get(path));
    }
}