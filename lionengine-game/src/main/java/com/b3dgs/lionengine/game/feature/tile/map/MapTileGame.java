/*
 * Copyright (C) 2013-2017 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.game.feature.tile.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.b3dgs.lionengine.Check;
import com.b3dgs.lionengine.Constant;
import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.Localizable;
import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Verbose;
import com.b3dgs.lionengine.core.Medias;
import com.b3dgs.lionengine.core.drawable.Drawable;
import com.b3dgs.lionengine.game.Force;
import com.b3dgs.lionengine.game.feature.FeaturableModel;
import com.b3dgs.lionengine.game.feature.tile.Tile;
import com.b3dgs.lionengine.game.feature.tile.TileGame;
import com.b3dgs.lionengine.game.feature.tile.TilesExtractor;
import com.b3dgs.lionengine.graphic.ImageBuffer;
import com.b3dgs.lionengine.graphic.SpriteTiled;
import com.b3dgs.lionengine.util.UtilMath;
import com.b3dgs.lionengine.util.UtilRandom;

/**
 * Abstract representation of a standard tile based map. This class uses a List of List to store tiles, a TreeMap to
 * store sheets references ({@link SpriteTiled}), and collisions.
 * <p>
 * The way to prepare a map is the following:
 * </p>
 * 
 * <pre>
 * {@link #create(int, int, int, int)} // prepare memory to store tiles
 * {@link #loadSheets(Media)} // load tile sheets
 * </pre>
 * 
 * <p>
 * Or import a map from a level rip with {@link #create(Media, Media)}.
 * </p>
 * 
 * @see Tile
 */
public class MapTileGame extends FeaturableModel implements MapTile
{
    /** Error sheet missing message. */
    private static final String ERROR_SHEET_MISSING = "Sheet missing: ";
    /** Inconsistent tile size. */
    private static final String ERROR_TILE_SIZE = "Tile size is inconsistent between sheets !";
    /** Invalid map tile size. */
    private static final String ERROR_APPEND_MAP_TILE_SIZE = "Appended map does not have the same tile size: ";

    /** Tile set listeners. */
    private final Collection<TileSetListener> tileSetListeners = new ArrayList<TileSetListener>();
    /** Sheets list. */
    private final Map<Integer, SpriteTiled> sheets = new HashMap<Integer, SpriteTiled>();
    /** Sheet configuration file. */
    private Media sheetsConfig;
    /** Tile width. */
    private int tileWidth;
    /** Tile height. */
    private int tileHeight;
    /** Number of horizontal tiles. */
    private int widthInTile;
    /** Number of vertical tiles. */
    private int heightInTile;
    /** Map radius. */
    private int radius;
    /** Tiles map. */
    private List<List<Tile>> tiles;

    /**
     * Create a map tile.
     */
    public MapTileGame()
    {
        super();
    }

    /**
     * Resize map with new size.
     * 
     * @param newWidth The new width in tile.
     * @param newHeight The new height in tile.
     */
    private void resize(int newWidth, int newHeight)
    {
        final int oldWidth = widthInTile;
        final int oldheight = heightInTile;

        // Adjust height
        for (int v = 0; v < newHeight - oldheight; v++)
        {
            tiles.add(new ArrayList<Tile>(newWidth));
        }
        // Adjust width
        for (int v = 0; v < newHeight; v++)
        {
            final int width;
            if (v < oldheight)
            {
                width = newWidth - oldWidth;
            }
            else
            {
                width = newWidth;
            }
            for (int h = 0; h < width; h++)
            {
                tiles.get(v).add(null);
            }
        }

        widthInTile = newWidth;
        heightInTile = newHeight;
        radius = (int) Math.ceil(StrictMath.sqrt(newWidth * (double) newWidth + newHeight * (double) newHeight));
    }

    /**
     * Append the map at specified offset.
     * 
     * @param map The map to append.
     * @param offsetX The horizontal offset.
     * @param offsetY The vertical offset.
     */
    private void appendMap(MapTile map, int offsetX, int offsetY)
    {
        for (int v = 0; v < map.getInTileHeight(); v++)
        {
            final int ty = offsetY + v;
            for (int h = 0; h < map.getInTileWidth(); h++)
            {
                final int tx = offsetX + h;
                final Tile tile = map.getTile(h, v);
                if (tile != null)
                {
                    final double x = tx * (double) tileWidth;
                    final double y = ty * (double) tileHeight;
                    setTile(createTile(tile.getSheet(), tile.getNumber(), x, y));
                }
            }
        }
    }

    /*
     * MapTile
     */

    @Override
    public void create(int tileWidth, int tileHeight, int widthInTile, int heightInTile)
    {
        Check.superiorStrict(tileWidth, 0);
        Check.superiorStrict(tileHeight, 0);
        Check.superiorStrict(widthInTile, 0);
        Check.superiorStrict(heightInTile, 0);

        clear();

        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.widthInTile = widthInTile;
        this.heightInTile = heightInTile;

        radius = (int) Math.ceil(StrictMath.sqrt(widthInTile * widthInTile + heightInTile * (double) heightInTile));
        tiles = new ArrayList<List<Tile>>(heightInTile);

        for (int v = 0; v < heightInTile; v++)
        {
            tiles.add(v, new ArrayList<Tile>(widthInTile));
            for (int h = 0; h < widthInTile; h++)
            {
                tiles.get(v).add(h, null);
            }
        }
    }

    @Override
    public Tile createTile(Integer sheet, int number, double x, double y)
    {
        return new TileGame(sheet, number, x, y, tileWidth, tileHeight);
    }

    @Override
    public void create(Media levelrip, int tileWidth, int tileHeight, int horizontalTiles)
    {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        final TilesExtractor tilesExtractor = new TilesExtractor();
        final Collection<ImageBuffer> tiles = tilesExtractor.extract(tileWidth, tileHeight, Arrays.asList(levelrip));
        loadSheets(SheetsExtractor.extract(tiles, horizontalTiles));

        for (final ImageBuffer tile : tiles)
        {
            tile.dispose();
        }

        LevelRipConverter.start(levelrip, this);
    }

    @Override
    public void create(Media levelrip)
    {
        create(levelrip, Medias.create(levelrip.getParentPath(), TileSheetsConfig.FILENAME));
    }

    @Override
    public void create(Media levelrip, Media sheetsConfig)
    {
        clear();

        loadSheets(sheetsConfig);
        final int errors = LevelRipConverter.start(levelrip, this);
        if (errors > 0)
        {
            Verbose.warning(getClass(), "create", "Number of missing tiles: ", String.valueOf(errors));
        }
        this.sheetsConfig = sheetsConfig;
    }

    @Override
    public void loadSheets(Collection<SpriteTiled> sheets)
    {
        int sheetId = 0;
        for (final SpriteTiled sheet : sheets)
        {
            this.sheets.put(Integer.valueOf(sheetId), sheet);
            if ((tileWidth != 0 || tileHeight != 0)
                && tileWidth != sheet.getTileWidth()
                && tileHeight != sheet.getTileHeight())
            {
                throw new LionEngineException(ERROR_TILE_SIZE);
            }
            tileWidth = sheet.getTileWidth();
            tileHeight = sheet.getTileHeight();
            sheetId++;
        }
    }

    @Override
    public void loadSheets(Media sheetsConfig)
    {
        this.sheetsConfig = sheetsConfig;

        final TileSheetsConfig config = TileSheetsConfig.imports(sheetsConfig);
        tileWidth = config.getTileWidth();
        tileHeight = config.getTileHeight();

        final String path = sheetsConfig.getPath();
        final String folder = path.substring(0, path.length() - sheetsConfig.getFile().getName().length());
        sheets.clear();
        int sheetId = 0;
        for (final String sheet : config.getSheets())
        {
            final Media media = Medias.create(folder, sheet);
            final SpriteTiled sprite = Drawable.loadSpriteTiled(media, tileWidth, tileHeight);
            sprite.load();
            sprite.prepare();
            sheets.put(Integer.valueOf(sheetId), sprite);
            sheetId++;
        }
    }

    @Override
    public void append(MapTile map, int offsetX, int offsetY)
    {
        Check.notNull(map);

        if (!isCreated())
        {
            create(map.getTileWidth(), map.getTileHeight(), 1, 1);
        }

        if (map.getTileWidth() != getTileWidth() || map.getTileHeight() != getTileHeight())
        {
            throw new LionEngineException(ERROR_APPEND_MAP_TILE_SIZE,
                                          map.getTileWidth() + Constant.SPACE + getTileHeight());
        }

        final int newWidth = Math.max(widthInTile, widthInTile - (widthInTile - offsetX) + map.getInTileWidth());
        final int newHeight = Math.max(heightInTile, heightInTile - (heightInTile - offsetY) + map.getInTileHeight());
        resize(newWidth, newHeight);

        appendMap(map, offsetX, offsetY);
    }

    @Override
    public void append(Collection<MapTile> maps, int offsetX, int offsetY, int randX, int randY)
    {
        int newWidth = widthInTile;
        int newHeight = heightInTile;
        final int[] randsX = new int[maps.size()];
        final int[] randsY = new int[randsX.length];
        int i = 0;
        int tw = 0;
        int th = 0;
        for (final MapTile map : maps)
        {
            randsX[i] = UtilRandom.getRandomInteger(randX);
            randsY[i] = UtilRandom.getRandomInteger(randY);

            newWidth += map.getInTileWidth() + randsX[i];
            newHeight += map.getInTileHeight() + randsY[i];

            if (tw == 0)
            {
                tw = map.getTileWidth();
                th = map.getTileHeight();
            }
            else if (tw != map.getTileWidth() || th != map.getTileHeight())
            {
                throw new LionEngineException(ERROR_APPEND_MAP_TILE_SIZE,
                                              map.getTileWidth() + Constant.SPACE + map.getTileHeight());
            }

            i++;
        }
        if (!isCreated())
        {
            create(tw, th, 1, 1);
        }
        resize(newWidth, newHeight);

        int ox = 0;
        int oy = 0;
        i = 0;
        for (final MapTile map : maps)
        {
            appendMap(map, ox, oy);
            ox += map.getInTileWidth() * offsetX + randsX[i];
            oy += map.getInTileHeight() * offsetY + randsY[i];
            i++;
        }
    }

    @Override
    public void clear()
    {
        if (tiles != null)
        {
            for (final List<Tile> list : tiles)
            {
                list.clear();
            }
            tiles.clear();
            widthInTile = 0;
            heightInTile = 0;
        }
    }

    @Override
    public void addListener(TileSetListener listener)
    {
        tileSetListeners.add(listener);
    }

    @Override
    public void removeListener(TileSetListener listener)
    {
        tileSetListeners.remove(listener);
    }

    @Override
    public void setTile(Tile tile)
    {
        final int tx = tile.getInTileX();
        final int ty = tile.getInTileY();
        Check.inferiorStrict(tx, getInTileWidth());
        Check.inferiorStrict(ty, getInTileHeight());

        tiles.get(ty).set(tx, tile);

        for (final TileSetListener listener : tileSetListeners)
        {
            listener.onTileSet(tile);
        }
    }

    @Override
    public Tile getTile(int tx, int ty)
    {
        if (!UtilMath.isBetween(tx, 0, getInTileWidth() - 1) || !UtilMath.isBetween(ty, 0, getInTileHeight() - 1))
        {
            return null;
        }
        return tiles.get(ty).get(tx);
    }

    @Override
    public Tile getTile(Localizable localizable, int offsetX, int offsetY)
    {
        return getTileAt(localizable.getX() + offsetX, localizable.getY() + offsetY);
    }

    @Override
    public Tile getTileAt(double x, double y)
    {
        final int tx = (int) Math.floor(x / getTileWidth());
        final int ty = (int) Math.floor(y / getTileHeight());
        return getTile(tx, ty);
    }

    @Override
    public Collection<Tile> getNeighbors(Tile tile)
    {
        final int tx = tile.getInTileX();
        final int ty = tile.getInTileY();
        final Collection<Tile> neighbors = new HashSet<Tile>(8);
        for (int ox = -1; ox < 2; ox++)
        {
            for (int oy = -1; oy < 2; oy++)
            {
                final Tile neighbor = getTile(tx + ox, ty + oy);
                if (neighbor != null && (ox != 0 || oy != 0))
                {
                    neighbors.add(neighbor);
                }
            }
        }
        return neighbors;
    }

    @Override
    public Collection<Tile> getTilesHit(double ox, double oy, double x, double y)
    {
        final Force force = Force.fromVector(ox, oy, x, y);
        final double sx = force.getDirectionHorizontal();
        final double sy = force.getDirectionVertical();

        double h = ox;
        double v = oy;

        final Collection<Tile> found = new ArrayList<Tile>();
        for (int count = 0; count < force.getVelocity(); count++)
        {
            v += sy;
            Tile tile = getTileAt(UtilMath.getRound(sx, h), UtilMath.getRound(sy, v));
            if (tile != null && !found.contains(tile))
            {
                found.add(tile);
            }

            h += sx;
            tile = getTileAt(UtilMath.getRound(sx, h), UtilMath.getRound(sy, v));
            if (tile != null && !found.contains(tile))
            {
                found.add(tile);
            }
        }
        return found;
    }

    @Override
    public int getInTileX(Localizable localizable)
    {
        return (int) Math.floor(localizable.getX() / getTileWidth());
    }

    @Override
    public int getInTileY(Localizable localizable)
    {
        return (int) Math.floor(localizable.getY() / getTileHeight());
    }

    @Override
    public Media getMedia()
    {
        return sheetsConfig;
    }

    @Override
    public Collection<Integer> getSheets()
    {
        return sheets.keySet();
    }

    @Override
    public SpriteTiled getSheet(Integer sheet)
    {
        if (!sheets.containsKey(sheet))
        {
            throw new LionEngineException(ERROR_SHEET_MISSING, sheet.toString());
        }
        return sheets.get(sheet);
    }

    @Override
    public int getSheetsNumber()
    {
        return sheets.size();
    }

    @Override
    public int getTilesNumber()
    {
        int tilesNumber = 0;
        for (int ty = 0; ty < heightInTile; ty++)
        {
            for (int tx = 0; tx < widthInTile; tx++)
            {
                final Tile tile = getTile(tx, ty);
                if (tile != null)
                {
                    tilesNumber++;
                }
            }
        }
        return tilesNumber;
    }

    @Override
    public int getTileWidth()
    {
        return tileWidth;
    }

    @Override
    public int getTileHeight()
    {
        return tileHeight;
    }

    @Override
    public int getInTileWidth()
    {
        return widthInTile;
    }

    @Override
    public int getInTileHeight()
    {
        return heightInTile;
    }

    @Override
    public int getInTileRadius()
    {
        return radius;
    }

    @Override
    public boolean isCreated()
    {
        return tiles != null;
    }

    /*
     * Surface
     */

    @Override
    public int getWidth()
    {
        return getInTileWidth() * getTileWidth();
    }

    @Override
    public int getHeight()
    {
        return getInTileHeight() * getTileHeight();
    }
}
