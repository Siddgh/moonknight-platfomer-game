package com.sid.characters;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class ActiveEmeny {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tiledMapTile;
    protected Rectangle rectangle;
    protected Body body;

    public ActiveEmeny(World world, TiledMap map, Rectangle rectangle) {
        this.world = world;
        this.map = map;
        this.rectangle = rectangle;
    }

}
