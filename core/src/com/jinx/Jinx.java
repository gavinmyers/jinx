package com.jinx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Jinx extends ApplicationAdapter implements InputProcessor {
    SpriteBatch batch;
    World world;
    final float PIXELS_TO_METERS = 100f;
    Texture img;
    TextureRegion[] monsters = new TextureRegion[4];
    Sprite m1;
    Sprite m2; 
    Body body;
    Body body2;
    
    @Override
    public void create() {

        
        batch = new SpriteBatch();
        world = new World(new Vector2(0, -100f), true);
        img = new Texture("Humanoid0.png");

        
        {
            m1 = new Sprite(img, 0, 0, 16, 16);
            m1.setPosition(100, 100);        	
	        BodyDef bodyDef = new BodyDef();
	        bodyDef.type = BodyDef.BodyType.DynamicBody;
	        bodyDef.position.set(m1.getX(), m1.getY());
	        body = world.createBody(bodyDef);        
	        PolygonShape shape = new PolygonShape();
	        shape.setAsBox(m1.getWidth()/2, m1.getHeight()/2);
	        FixtureDef fixtureDef = new FixtureDef();
	        fixtureDef.shape = shape;
	        fixtureDef.density = 1f;
	        Fixture fixture = body.createFixture(fixtureDef);
	        shape.dispose();
        }
        
        
        {
        	m2 = new Sprite(img, 0, 16, 16, 16);
        	m2.setPosition(100, 25);
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(m2.getX(), m2.getY());
            body2 = world.createBody(bodyDef);        
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(m2.getWidth()/2, m2.getHeight()/2);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 1f;
            Fixture fixture = body2.createFixture(fixtureDef);
            shape.dispose();        
        }
        
        
        
    }


    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        
        Vector2 vel = body.getLinearVelocity();
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
        	vel.x -= 5;        	
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
        	vel.x += 5;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
        	vel.y += 5;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
        	vel.y -= 5;
        } 
        body.setLinearVelocity(vel);
        m1.setPosition(body.getPosition().x, body.getPosition().y);

        batch.begin();
        m1.draw(batch);
        m2.draw(batch);
        batch.end();
        
    }

    @Override
    public void dispose() {

    }


	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
