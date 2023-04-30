package com.sid.levelcreator;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.sid.constants.GameConstants;

public class ElementDetection {
    public ElementDetection(World world, TiledMap map) {
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / GameConstants.PIXELS_PER_METER, (rectangle.getY() + rectangle.getHeight() / 2) / GameConstants.PIXELS_PER_METER);
            body = world.createBody(bodyDef);
            shape.setAsBox((rectangle.getWidth() / 2) / GameConstants.PIXELS_PER_METER, (rectangle.getHeight() / 2) / GameConstants.PIXELS_PER_METER);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
        }
    }
}
