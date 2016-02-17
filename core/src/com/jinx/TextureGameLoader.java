package com.jinx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class TextureGameLoader {

    Texture level;
    TiledMap levelMap;
    OrthogonalTiledMapRenderer levelMapRenderer;
    TextureRegion[] levels;
    
    Texture monster;
    TextureRegion[] monsters;
    
    public TextureGameLoader() {
    	levelMap = new TmxMapLoader().load("level01.tmx");
    	levelMapRenderer = new OrthogonalTiledMapRenderer(levelMap);
    	level = new Texture("levels.png");
    	levels = TextureRegion.split(level, 24, 24)[0];
    	
    	monster = new Texture("Humanoid0.png");
    	monsters = TextureRegion.split(monster, 16, 16)[0];  
    }
    
    public TextureRegion getMonster(int idx) {
    	return monsters[idx];
    }
    
    
}
