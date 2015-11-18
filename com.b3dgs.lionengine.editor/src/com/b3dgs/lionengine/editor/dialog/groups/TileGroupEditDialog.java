/*
 * Copyright (C) 2013-2015 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.b3dgs.lionengine.editor.dialog.groups;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.drawable.SpriteTiled;
import com.b3dgs.lionengine.editor.Focusable;
import com.b3dgs.lionengine.editor.ObjectListListener;
import com.b3dgs.lionengine.editor.dialog.AbstractDialog;
import com.b3dgs.lionengine.editor.project.dialog.group.GroupList;
import com.b3dgs.lionengine.editor.utility.UtilIcon;
import com.b3dgs.lionengine.editor.utility.UtilSwt;
import com.b3dgs.lionengine.editor.world.ObjectControl;
import com.b3dgs.lionengine.editor.world.PaletteModel;
import com.b3dgs.lionengine.editor.world.PaletteType;
import com.b3dgs.lionengine.editor.world.Selection;
import com.b3dgs.lionengine.editor.world.TileSelectionListener;
import com.b3dgs.lionengine.editor.world.WorldPart;
import com.b3dgs.lionengine.editor.world.WorldView;
import com.b3dgs.lionengine.editor.world.renderer.WorldRenderer;
import com.b3dgs.lionengine.editor.world.renderer.WorldSelectedTiles;
import com.b3dgs.lionengine.editor.world.updater.WorldInteractionTile;
import com.b3dgs.lionengine.editor.world.updater.WorldUpdater;
import com.b3dgs.lionengine.game.Camera;
import com.b3dgs.lionengine.game.map.LevelRipConverter;
import com.b3dgs.lionengine.game.map.MapTile;
import com.b3dgs.lionengine.game.map.MapTileGame;
import com.b3dgs.lionengine.game.map.MapTileGroupModel;
import com.b3dgs.lionengine.game.object.Factory;
import com.b3dgs.lionengine.game.object.Handler;
import com.b3dgs.lionengine.game.object.Services;
import com.b3dgs.lionengine.game.tile.Tile;
import com.b3dgs.lionengine.game.tile.TileGroup;
import com.b3dgs.lionengine.game.tile.TileRef;

/**
 * Edit map tile group dialog.
 */
public class TileGroupEditDialog extends AbstractDialog implements WorldView, Focusable, KeyListener,
                                 TileSelectionListener, ObjectListListener<TileGroup>
{
    /** Icon. */
    private static final Image ICON = UtilIcon.get("dialog", "import.png");

    /** Service reference. */
    private final Services services = new Services();
    /** Map reference. */
    private final MapTile map;
    /** Groups list. */
    private final GroupList groupList = new GroupList();
    /** World view. */
    private Composite view;
    /** Part service. */
    @Inject
    private EPartService partService;

    /**
     * Create the dialog.
     * 
     * @param parent The parent reference.
     */
    public TileGroupEditDialog(Shell parent)
    {
        super(parent, Messages.Title, Messages.HeaderTitle, Messages.HeaderDesc, ICON, SWT.SHELL_TRIM);

        services.add(new Camera());
        services.add(new Handler());
        services.add(new Factory(services));
        map = services.create(MapTileGame.class);
        map.addFeature(new MapTileGroupModel(services));
        services.add(new Selection());
        services.add(new ObjectControl(services));

        final PaletteModel palette = new PaletteModel();
        palette.setSelectedPalette(PaletteType.POINTER_TILE);
        services.add(palette);

        createDialog();
        dialog.setMinimumSize(640, 448);
        finish.setEnabled(true);
    }

    /**
     * Load the levels rip.
     * 
     * @param tileWidth The tile width.
     * @param tileHeight The tile height.
     * @param sheets The sheets used.
     * @param levelRips The level rips.
     */
    public void load(int tileWidth, int tileHeight, Collection<SpriteTiled> sheets, Media... levelRips)
    {
        int offsetX = 0;
        int offsetY = 0;
        boolean first = true;
        final int size = (int) Math.ceil(Math.sqrt(levelRips.length));
        int count = 0;
        for (final Media levelRip : levelRips)
        {
            count++;
            final MapTile part;
            if (first)
            {
                part = map;
                first = false;
            }
            else
            {
                part = new MapTileGame();
            }
            part.loadSheets(tileWidth, tileHeight, sheets);
            LevelRipConverter.start(levelRip, part);
            if (!first)
            {
                map.append(part, offsetX, offsetY);
            }

            offsetX += part.getInTileWidth();
            if (count >= size)
            {
                offsetY += part.getInTileHeight();
                offsetX = 0;
                count = 0;
            }
        }
        update();
    }

    /**
     * Get the defined map tile groups.
     * 
     * @return The defined map tile groups.
     */
    public Collection<TileGroup> getGroups()
    {
        final Map<String, Collection<TileRef>> groupsRef = new HashMap<>();
        for (int ty = 0; ty < map.getInTileHeight(); ty++)
        {
            for (int tx = 0; tx < map.getInTileWidth(); tx++)
            {
                final Tile tile = map.getTile(tx, ty);
                if (tile != null)
                {
                    addToGroup(groupsRef, tile);
                }
            }
        }
        final Collection<TileGroup> groups = new HashSet<>();
        for (final Entry<String, Collection<TileRef>> entry : groupsRef.entrySet())
        {
            groups.add(new TileGroup(entry.getKey(), entry.getValue()));
        }
        return groups;
    }

    /**
     * Add tile to group list.
     * 
     * @param groups The group list reference.
     * @param tile The tile to add.
     */
    private void addToGroup(Map<String, Collection<TileRef>> groups, Tile tile)
    {
        final String group = tile.getGroup();
        if (group != null)
        {
            if (!groups.containsKey(group))
            {
                groups.put(group, new HashSet<>());
            }
            final Collection<TileRef> tiles = groups.get(group);
            tiles.add(new TileRef(tile));
        }
    }

    /**
     * Get the selected tile group name.
     * 
     * @return The selected tile group name, <code>null</code> if none.
     */
    private String getSelectedGroup()
    {
        final TileGroup group = groupList.getSelectedObject();
        if (group != null)
        {
            return group.getName();
        }
        return null;
    }

    /*
     * AbstractDialog
     */

    @Override
    protected void createContent(Composite content)
    {
        content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final Composite area = new Composite(content, SWT.NONE);
        area.setLayout(new GridLayout(2, false));
        area.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final Composite areaList = new Composite(area, SWT.NONE);
        areaList.setLayout(new GridLayout(1, false));
        areaList.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true));
        groupList.create(areaList);
        groupList.addListener(this);

        services.add(this);

        final Composite areaView = new Composite(area, SWT.BORDER);
        areaView.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final WorldUpdater updater = new WorldUpdater(partService, services);
        services.add(updater);
        final WorldRenderer renderer = new WorldRenderer(partService, services);

        view = WorldPart.createPart(areaView, updater, renderer);
        view.addMouseTrackListener(UtilSwt.createFocusListener(this));
        view.addKeyListener(this);

        final WorldInteractionTile tileInteraction = services.get(WorldInteractionTile.class);
        tileInteraction.addListener(this);
    }

    @Override
    protected void onFinish()
    {
        // Nothing to do
    }

    @Override
    @Focus
    public void focus()
    {
        view.forceFocus();
    }

    /*
     * WorldView
     */

    @Override
    public void update()
    {
        if (!view.isDisposed())
        {
            view.redraw();
        }
    }

    @Override
    public void setToolItemText(String item, String text)
    {
        // Nothing to do
    }

    @Override
    public <T> T getToolItem(String item, Class<T> clazz)
    {
        return null;
    }

    /*
     * KeyListener
     */

    @Override
    public void keyPressed(KeyEvent e)
    {
        update();
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        // Nothing to do
    }

    /*
     * TileSelectionListener
     */

    @Override
    public void notifyTileSelected(Tile tile)
    {
        tile.setGroup(getSelectedGroup());
        for (int ty = 0; ty < map.getInTileHeight(); ty++)
        {
            for (int tx = 0; tx < map.getInTileWidth(); tx++)
            {
                final Tile current = map.getTile(tx, ty);
                if (current != null
                    && current.getSheet().equals(tile.getSheet())
                    && current.getNumber() == tile.getNumber())
                {
                    current.setGroup(tile.getGroup());
                }
            }
        }
    }

    @Override
    public void notifyTileGroupSelected(String group)
    {
        // Nothing to do
    }

    /*
     * ObjectListListener
     */

    @Override
    public void notifyObjectSelected(TileGroup object)
    {
        services.get(WorldSelectedTiles.class).notifyTileGroupSelected(getSelectedGroup());
        update();
    }

    @Override
    public void notifyObjectDeleted(TileGroup object)
    {
        services.get(WorldSelectedTiles.class).notifyTileGroupSelected(null);
        update();
    }
}