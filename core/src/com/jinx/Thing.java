package com.jinx;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

public class Thing {

	public Thing(World world, TextureRegion texture, BodyDef.BodyType bodyType, int posX, int posY) {
		bodyDef = new BodyDef();
		
		bodyDef.type = bodyType;
		bodyDef.position.set(posX, posY);
		
		fixtureDef = new FixtureDef();
		
		shape = new PolygonShape();
		
		fixtureDef.shape = shape;
		fixtureDef.density = 5;
		fixtureDef.friction = .4f;
		
		sprite = new Box2DSprite(texture);
		shape.setAsBox(sprite.getHeight(), sprite.getWidth());
		
		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		body.setUserData(sprite);
		
		
		//
		
/*
        sprite = new Box2DSprite(texture);
        sprite.setPosition(posX, posY);
        
        bodyDef = new BodyDef();
        bodyDef.type = bodyType;        
        bodyDef.position.set(sprite.getX(), sprite.getY());
        
        
        body = world.createBody(bodyDef);
        body.setUserData(sprite);
        
        shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth(), sprite.getHeight());
        
        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
                
        fixture = body.createFixture(fixtureDef);
        fixture.setUserData(sprite); 
        
        shape.dispose();		
        */
        
	}
	
	public Sprite sprite;
	public BodyDef bodyDef;
	public Body body;
	public PolygonShape shape;
	public FixtureDef fixtureDef;
	public Fixture fixture;
	
}
