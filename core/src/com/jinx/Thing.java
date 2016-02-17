package com.jinx;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Thing {

	public Thing(World world, TextureRegion texture, BodyDef.BodyType bodyType, int posX, int posY) {
        sprite = new Sprite(texture);  
        sprite.setPosition(posX, posY);
        bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        body = world.createBody(bodyDef);
        bodyDef.position.set(sprite.getX(), sprite.getY());        
        shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth()/2, sprite.getHeight()/2);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixture = body.createFixture(fixtureDef);
        shape.dispose();		
        
	}
	
	public Sprite sprite;
	public BodyDef bodyDef;
	public Body body;
	public PolygonShape shape;
	public FixtureDef fixtureDef;
	public Fixture fixture;
	
}
