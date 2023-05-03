package com.sid.levelcreator;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.sid.characters.SkullEnemy;
import com.sid.constants.CharacterBits;
import com.sid.constants.GameConstants;
import com.sid.screens.StartScreen;

public class ElementDetection {
    private Fixture fixture;
    private Array<SkullEnemy> skullEnemyArray;

    public ElementDetection(StartScreen startScreen) {
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        for (MapObject object : startScreen.getLevelMap().getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / GameConstants.PIXELS_PER_METER, (rectangle.getY() + rectangle.getHeight() / 2) / GameConstants.PIXELS_PER_METER);
            body = startScreen.getWorld().createBody(bodyDef);
            shape.setAsBox((rectangle.getWidth() / 2) / GameConstants.PIXELS_PER_METER, (rectangle.getHeight() / 2) / GameConstants.PIXELS_PER_METER);
            fixtureDef.shape = shape;
            fixture = body.createFixture(fixtureDef);
        }

        skullEnemyArray = new Array<SkullEnemy>();
        for (MapObject object : startScreen.getLevelMap().getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            skullEnemyArray.add(new SkullEnemy(startScreen, rectangle.getX() / GameConstants.PIXELS_PER_METER, rectangle.getY() / GameConstants.PIXELS_PER_METER, new Vector2(0, 0)));
        }

    }

    public void setCategoryFilter(short bit) {
        Filter filter = new Filter();
        filter.categoryBits = bit;
        fixture.setFilterData(filter);
    }

    public Array<SkullEnemy> getSkullEnemyArray() {
        return skullEnemyArray;
    }
}
