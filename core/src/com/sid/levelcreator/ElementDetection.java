package com.sid.levelcreator;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.sid.characters.DemonEnemy;
import com.sid.characters.SkullEnemy;
import com.sid.constants.CharacterBits;
import com.sid.constants.GameConstants;
import com.sid.screens.StartScreen;

/**
 * This class is responsible for extracting metadata from the Level Map and creating Platforms and Enemies more interactable
 */
public class ElementDetection {
    private Fixture fixture;
    private Array<SkullEnemy> skullEnemyArray;

    private Array<DemonEnemy> demonEnemyArray;

    public ElementDetection(StartScreen startScreen) {

        // I create a body definition object. in LibGDX with we can define specific body properties for each element.
        // For example, if the body is static or dynamic (moving)
        BodyDef bodyDef = new BodyDef();

        // We also create polygon shapes that we would later assign to all the ground elements in the map.
        // This way I'm able to differentiate between my players, my enemies and the platform.
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        // In the Level 1, I've defined everything in layers. All my platforms are on the 5th layers.
        // Here I loop through all my platforms, to create a polygon shape for all of them
        for (MapObject object : startScreen.getLevelMap().getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / GameConstants.PIXELS_PER_METER, (rectangle.getY() + rectangle.getHeight() / 2) / GameConstants.PIXELS_PER_METER);
            body = startScreen.getWorld().createBody(bodyDef);
            shape.setAsBox((rectangle.getWidth() / 2) / GameConstants.PIXELS_PER_METER, (rectangle.getHeight() / 2) / GameConstants.PIXELS_PER_METER);
            fixtureDef.shape = shape;
            fixture = body.createFixture(fixtureDef);
        }

        // Similar to above, I have all my code regarding the enemy stored on the 6th index. So here I'm creating a rectangle and assigning them to the
        skullEnemyArray = new Array<SkullEnemy>();
        for (MapObject object : startScreen.getLevelMap().getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            skullEnemyArray.add(new SkullEnemy(startScreen, rectangle.getX() / GameConstants.PIXELS_PER_METER, rectangle.getY() / GameConstants.PIXELS_PER_METER, new Vector2(0, 0)));
        }

        // Similar to above, I have all my code regarding the enemy stored on the 7th index. So here I'm creating a rectangle and assigning them to the
        demonEnemyArray = new Array<DemonEnemy>();
        for (MapObject object : startScreen.getLevelMap().getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            demonEnemyArray.add(new DemonEnemy(startScreen, rectangle.getX() / GameConstants.PIXELS_PER_METER, rectangle.getY() / GameConstants.PIXELS_PER_METER, new Vector2(1, 0)));
        }

        for (MapObject object : startScreen.getLevelMap().getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / GameConstants.PIXELS_PER_METER, (rectangle.getY() + rectangle.getHeight() / 2) / GameConstants.PIXELS_PER_METER);
            body = startScreen.getWorld().createBody(bodyDef);
            shape.setAsBox((rectangle.getWidth() / 2) / GameConstants.PIXELS_PER_METER, (rectangle.getHeight() / 2) / GameConstants.PIXELS_PER_METER);
            fixtureDef.shape = shape;
            fixture = body.createFixture(fixtureDef);
        }

    }


    // I also individually set categorybits for both the platform elements and other elements.
    // These category bits would later let us know if there are any collisions between the enemey and the player or the player and the ground, etc.
    public void setCategoryFilter(short bit) {
        Filter filter = new Filter();
        filter.categoryBits = bit;
        fixture.setFilterData(filter);
    }

    public Array<SkullEnemy> getSkullEnemyArray() {
        return skullEnemyArray;
    }

    public Array<DemonEnemy> getDemonEnemyArray() {
        return demonEnemyArray;
    }
}
