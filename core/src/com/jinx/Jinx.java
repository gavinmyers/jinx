package com.jinx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

public class Jinx extends ApplicationAdapter implements InputProcessor {
	
	
    SpriteBatch batch;
    World world;
    OrthographicCamera cam;

    TextureGameLoader tgl;
    
    Thing thing1;
    
    Box2DDebugRenderer debugRenderer;

    
    @Override
    public void create() {
    	world = new World(new Vector2(0, -100f), true);
    	
    	tgl = new TextureGameLoader(world);
    	
        batch = new SpriteBatch();
        
    	
        cam = new OrthographicCamera();
    	cam.setToOrtho(false, 1024, 768);
    	batch.setProjectionMatrix(cam.combined);
    	
    	thing1 = new Thing(world, tgl.getMonster(0), BodyDef.BodyType.DynamicBody, 100, 150);
    	new Thing(world, tgl.getMonster(1), BodyDef.BodyType.DynamicBody, 100, 100);    
    	
    	debugRenderer=new Box2DDebugRenderer();
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
       
        //tgl.levelMapRenderer.setView(cam);
        //tgl.levelMapRenderer.render();
        cam.translate(0,0,0);
        cam.update();
        
        batch.begin();
        Box2DSprite.draw(batch, world);
        batch.end();
        

        
        debugRenderer.render(world, cam.combined);
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
