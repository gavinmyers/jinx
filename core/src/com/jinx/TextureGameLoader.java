package com.jinx;

import java.util.Iterator;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;


public class TextureGameLoader {

    Texture level;
    TiledMap levelMap;
    OrthogonalTiledMapRenderer levelMapRenderer;
    TextureRegion[] levels;
    
    Texture monster;
    TextureRegion[] monsters;
    
    public TextureGameLoader(World world) {
    	levelMap = new TmxMapLoader().load("level01.tmx");
    	levelMapRenderer = new OrthogonalTiledMapRenderer(levelMap);
    	int tileX = 48;
    	int tileY = 48;
    	level = new Texture("levels.png");
    	levels = TextureRegion.split(level, 24, 24)[0];
    	TiledMapTileLayer tmtl = (TiledMapTileLayer)levelMap.getLayers().get("ground");
    	for(int x = 0; x < 1000; x++) {
    		for(int y = 0; y < 1000; y++) {
    			Cell c = tmtl.getCell(x,y);
    			if(c != null) {
    				int posX = x * tileX;
    				int posY = y * tileY;
    				System.out.println(c.getTile().getId() + " at " + posX + "," + posY);
    				TiledMapTile t = c.getTile();
    				new Thing(world, t.getTextureRegion(), BodyDef.BodyType.StaticBody, posX, posY); 
    				
    			}
    		}
    	}
    	monster = new Texture("Humanoid0.png");
    	monsters = TextureRegion.split(monster, 16, 16)[0];  
    }
    
    public TextureRegion getMonster(int idx) {
    	return monsters[idx];
    }
    
    
}
