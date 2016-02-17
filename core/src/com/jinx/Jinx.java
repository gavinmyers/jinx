package com.jinx;

import java.util.Iterator;

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
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

public class Jinx extends ApplicationAdapter implements InputProcessor {
	
	
    SpriteBatch batch;
    World world;
    OrthographicCamera cam;
    
    TextureGameLoader tgl;
    
    Thing thing1;
    Thing thing2;
    
    @Override
    public void create() {
    	tgl = new TextureGameLoader();
    	
        batch = new SpriteBatch();
        world = new World(new Vector2(0, -100f), true);
    	cam = new OrthographicCamera();
    	cam.setToOrtho(false, 320, 240);
    	batch.setProjectionMatrix(cam.combined);
    	
    	thing1 = new Thing(world, tgl.getMonster(0), BodyDef.BodyType.DynamicBody, 100, 100);
    	thing2 = new Thing(world, tgl.getMonster(0), BodyDef.BodyType.StaticBody, 100, 25);    	        
    }


    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        
        Vector2 vel = thing1.body.getLinearVelocity();
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
        thing1.body.setLinearVelocity(vel);
        thing1.sprite.setPosition(thing1.body.getPosition().x, thing1.body.getPosition().y);
        
        tgl.levelMapRenderer.setView(cam);
        tgl.levelMapRenderer.render();
        
        batch.begin();
        thing1.sprite.draw(batch);
        thing2.sprite.draw(batch);
        batch.end();
        
        cam.translate(0,0,0);
        cam.update();
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
