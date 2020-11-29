/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.EternalFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Foliage;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.EyeOfWisdom;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTileSheet;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Group;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.Arrays;

public class EyeOfWisdomLevel extends Level {

	{
		color1 = 0x801500;
		color2 = 0xa68521;

		viewDistance = Math.min(4, viewDistance);
	}

	@Override
	public String tilesTex() {
		return Assets.Environment.TILES_SEWERS;
	}

	@Override
	public String waterTex() {
		return Assets.Environment.WATER_SEWERS;
	}

	@Override
	public void create() {
		super.create();
		for (int i=0; i < length(); i++) {
			int flags = Terrain.flags[map[i]];
			if ((flags & Terrain.PIT) != 0){
				passable[i] = avoid[i] = false;
				solid[i] = true;
			}
		}
		for (int i = (height+2)*width; i < length; i++){
			passable[i] = avoid[i] = false;
			solid[i] = true;
		}
		for (int i = (height+1)*width; i < length; i++){
			if (i % width < 4 || i % width > 12 || i >= (length-width)){
				discoverable[i] = false;
			} else {
				visited[i] = true;
			}
		}
	}

	private static final int ROOM_TOP = 10;

	@Override
	protected boolean build() {
		setSize(21, 23);
		Arrays.fill( map, Terrain.CHASM );

		final int MID = width/2;

		//Fills the walls at the top
		Painter.fill( this, 0, 0, width, 2, Terrain.WALL );


		//Fills the walls at the bottom
		Painter.fill(this, 0, height-2, width, 2, Terrain.WALL);




		//Puts exit
		exit = MID + width();
		Painter.fill(this, MID, 1, 1, 1, Terrain.EXIT);
		map[exit] = Terrain.EXIT;



		//Walkway from entrance
		int walkwayHeight = 5;
		int walkwayYPos = height - 2 - walkwayHeight;
		Painter.fill( this, MID-1, walkwayYPos, 3, walkwayHeight, Terrain.EMPTY);

		//The circle thingy
		int topCircleYPos = walkwayYPos - 9;
		//Top Circle
		Painter.fill( this, MID-2, topCircleYPos						, 5	, 1, Terrain.EMPTY);
		Painter.fill( this, MID-3, topCircleYPos+1					, 7	, 1, Terrain.EMPTY);
		Painter.fill( this, MID-4, topCircleYPos+2					, 9	, 1, Terrain.EMPTY);

		int midCircleHeight = 3;
		Painter.fill( this, MID-5, topCircleYPos+3					, 11	, midCircleHeight, Terrain.EMPTY);

		Painter.fill( this, MID-4, topCircleYPos+3+midCircleHeight	, 9	, 1, Terrain.EMPTY);
		Painter.fill( this, MID-3, topCircleYPos+4+midCircleHeight	, 7	, 1, Terrain.EMPTY);
		Painter.fill( this, MID-2, topCircleYPos+5+midCircleHeight	, 5	, 1, Terrain.EMPTY);
		//Bottom Circle
		int circleHeight = 6 + midCircleHeight;

		//Walkway to Exit
		int exitWalkwayHeight = walkwayHeight;
		int exitWalkwayYPos = height - walkwayHeight - circleHeight - exitWalkwayHeight - 2;

		Painter.fill( this, MID-1, exitWalkwayYPos, 3, exitWalkwayHeight, Terrain.EMPTY);


		//Puts entrance
		entrance = (height-2) * width() + MID;
		map[entrance] = Terrain.ENTRANCE;
		Painter.fill(this, MID, height-2, 1, 1, Terrain.ENTRANCE);


		//Inner Walls
		Painter.fill(this, MID-2, topCircleYPos+2, 2, 2, Terrain.WALL);
		Painter.fill(this, MID+1, topCircleYPos+2, 2, 2, Terrain.WALL);

		Painter.fill(this, MID-2, topCircleYPos+1+3 + 1, 2, 2, Terrain.WALL);
		Painter.fill(this, MID+1, topCircleYPos+1+3 + 1, 2, 2, Terrain.WALL);

		//Outer Walls
//		Painter.fill(this, MID-2-6, 0, 2, 2, Terrain.WALL);
//		Painter.fill(this, MID+1+6, 0, 2, 2, Terrain.WALL);
//
//		Painter.fill(this, MID-2-6, 10, 2, 2, Terrain.WALL);
//		Painter.fill(this, MID+1+6, 10, 2, 2, Terrain.WALL);

		feeling = Feeling.NONE;
		viewDistance = 30;

		CustomTilemap vis = new CustomTopWall();
		vis.setRect( 0, 0, width, height-4);
		customWalls.add(vis);

		vis = new CustomTopTiles();
		vis.setRect( 0, 0, width, height-4);
		customTiles.add(vis);

		vis = new PedestalTilesVisuals();
		vis.pos(MID-5, topCircleYPos);
		customTiles.add(vis);

		vis = new PedestalWallsVisual();
		vis.pos(MID-2, topCircleYPos+1);
		customWalls.add(vis);



		vis = new WalkwayPieceVisuals();
		vis.pos(MID-1, walkwayYPos);
		customTiles.add(vis);

		vis = new WalkwayExitPieceVisuals();
		vis.pos(MID-1, exitWalkwayYPos+2);
		customTiles.add(vis);

		vis = new OuterPillar();
		vis.pos(2, 0);
		customWalls.add(vis);

		vis = new OuterPillar();
		vis.pos(17, 0);
		customWalls.add(vis);

		vis = new OuterPillar();
		vis.pos(2, 10);
		customWalls.add(vis);

		vis = new OuterPillar();
		vis.pos(17, 10);
		customWalls.add(vis);

		vis = new CustomBottomWall();
		vis.pos( 0, height-3);
		customWalls.add(vis);

		vis = new CustomBottomTiles();
		vis.pos( 0, height-2);
		customTiles.add(vis);

//		int centerPos = width/2 + (((height/2)-3) * width);
//		Foliage light = (Foliage)this.blobs.get( Foliage.class );
//		if (light == null) {
//			light = new Foliage();
//		}
//
//		for (int i=0 ; i < 5; i++) {
//			for (int j=0; j < 5; j++) {
//				light.seed( this, (centerPos-2) + j + i*width, 1 );
//			}
//		}
//		this.blobs.put( Foliage.class, light );


		return true;
	}


	@Override
	public Mob createMob() {
		return null;
	}
	
	@Override
	protected void createMobs() {
	}

	public Actor addRespawner() {
		return null;
	}

	@Override
	protected void createItems() {
		int pos = width/2 + (((height/2)) * width);
		drop( new EyeOfWisdom(), pos );
	}

	@Override
	public int randomRespawnCell( Char ch ) {
		int cell;
		do {
			cell = entrance + PathFinder.NEIGHBOURS8[Random.Int(8)];
		} while (!passable[cell]
				|| (Char.hasProp(ch, Char.Property.LARGE) && !openSpace[cell])
				|| Actor.findChar(cell) != null);
		return cell;
	}

	@Override
	public String tileName( int tile ) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(HallsLevel.class, "water_name");
			case Terrain.GRASS:
				return Messages.get(HallsLevel.class, "grass_name");
			case Terrain.HIGH_GRASS:
				return Messages.get(HallsLevel.class, "high_grass_name");
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return Messages.get(HallsLevel.class, "statue_name");
			default:
				return super.tileName( tile );
		}
	}

	@Override
	public String tileDesc(int tile) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(HallsLevel.class, "water_desc");
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return Messages.get(HallsLevel.class, "statue_desc");
			case Terrain.BOOKSHELF:
				return Messages.get(HallsLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc( tile );
		}
	}

	@Override
	public Group addVisuals () {
		super.addVisuals();
		HallsLevel.addHallsVisuals(this, visuals);
		return visuals;
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		for (int i=0; i < length(); i++) {
			int flags = Terrain.flags[map[i]];
			if ((flags & Terrain.PIT) != 0){
				passable[i] = avoid[i] = false;
				solid[i] = true;
			}
		}
		for (int i = (height+2)*width; i < length; i++){
			passable[i] = avoid[i] = false;
			solid[i] = true;
		}
		for (int i = (height+1)*width; i < length; i++){
			if (i % width < 4 || i % width > 12 || i >= (length-width)){
				discoverable[i] = false;
			} else {
				visited[i] = true;
			}
		}
	}

	public static class CustomTopWall extends CustomTilemap {

		{
			texture = Assets.Environment.EYE_OF_WISDOM_TILE;
		}

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			int cell = tileX + tileY * Dungeon.level.width();
			int[] map = Dungeon.level.map;
			int[] data = new int[tileW*tileH];
			for (int i = 0; i < data.length; i++){
				if (i % tileW == 0){
					cell = tileX + (tileY + i / tileW) * Dungeon.level.width();
				}

				if (i < 21 && map[i] == Terrain.WALL){
					data[i] = i;
				}
				else{
					data[i] = -1;
				}

				cell++;
			}
			v.map( data, tileW );
			return v;
		}

	}

	public static class CustomTopTiles extends CustomTilemap {

		{
			texture = Assets.Environment.EYE_OF_WISDOM_TILE;
		}

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			int cell = tileX + tileY * Dungeon.level.width();
			int[] map = Dungeon.level.map;
			int[] data = new int[tileW*tileH];
			for (int i = 0; i < data.length; i++){
				if (i % tileW == 0){
					cell = tileX + (tileY + i / tileW) * Dungeon.level.width();
				}

				if (i < 63 && i > 20){
					data[i] = i;
				}
				else{
					data[i] = -1;
				}

				cell++;
			}
			v.map( data, tileW );
			return v;
		}

	}

	public static class PedestalTilesVisuals extends CustomTilemap {

		{
			texture = Assets.Environment.EYE_OF_WISDOM_TILE;

			tileW = 11;
			tileH = 9;
		}

		private static int[] centers = new int[]{
				115,
				115+21,
				115+21*2,
				115+21*3,
				115+21*4,
				115+21*5,
				115+21*6,
				115+21*7,
				115+21*8,
//				115+21*9,

		};

		private static final int[] map = new int[]{
				-1, 			-1, 			-1, 			centers[0]-2, 	centers[0]-1, 	centers[0], 	centers[0]+1, centers[0]+2, -1,				-1, 			-1,
				-1, 			-1, 			centers[1]-3, 	centers[1]-2, 	centers[1]-1, 	centers[1], 	centers[1]+1, centers[1]+2, centers[1]+3, 	-1, 			-1,
				-1, 			centers[2]-4, 	centers[2]-3, 	centers[2]-2, 	centers[2]-1,	centers[2], 	centers[2]+1, centers[2]+2, centers[2]+3, 	centers[2]+4, 	-1,
				centers[3]-5, 	centers[3]-4, 	centers[3]-3, 	centers[3]-2, 	centers[3]-1, 	centers[3], 	centers[3]+1, centers[3]+2, centers[3]+3, 	centers[3]+4, 	centers[3]+5,
				centers[4]-5, 	centers[4]-4, 	centers[4]-3, 	centers[4]-2, 	centers[4]-1, 	centers[4], 	centers[4]+1, centers[4]+2, centers[4]+3, 	centers[4]+4, 	centers[4]+5,
				centers[5]-5, 	centers[5]-4, 	centers[5]-3, 	centers[5]-2, 	centers[5]-1, 	centers[5], 	centers[5]+1, centers[5]+2, centers[5]+3, 	centers[5]+4, 	centers[5]+5,
				-1, 			centers[6]-4, 	centers[6]-3, 	centers[6]-2, 	centers[6]-1, 	centers[6], 	centers[6]+1, centers[6]+2, centers[6]+3, 	centers[6]+4, 	-1,
				-1, 			-1, 			centers[7]-3, 	centers[7]-2, 	centers[7]-1,	centers[7], 	centers[7]+1, centers[7]+2, centers[7]+3, 	-1, 			-1,
				-1, 			-1, 			-1, 			centers[8]-2, 	centers[8]-1, 	centers[8], 	centers[8]+1, centers[8]+2, -1, 			-1, 			-1,
		};

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			v.map(map, tileW);
			return v;
		}
	}

	public static class PedestalWallsVisual extends CustomTilemap {

		{
			texture = Assets.Environment.EYE_OF_WISDOM_TILE;

			tileW = 5;
			tileH = 6;
		}

		private static final int[] map = new int[]{
				189, 	190, 	-1, 	208, 	209,
				210, 	211, 	-1, 	229, 	230,
				231, 	232, 	-1, 	250, 	251,

				252, 	253, 	-1, 	271, 	272,
				273, 	274, 	-1, 	292, 	293,
				294, 	295, 	-1, 	313, 	314,
		};

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			v.map(map, tileW);
			return v;
		}
	}

	public static class WalkwayPieceVisuals extends CustomTilemap {
		{
			texture = Assets.Environment.EYE_OF_WISDOM_TILE;

			tileW = 3;
			tileH = 5;
		}

		private static int spritePos = 14*21 + 10;
		private static int[] centers = new int[]{
				spritePos,
				spritePos+21,
				spritePos+21*2,
				spritePos+21*3,
				spritePos+21*4,

		};
		private static final int[] map = new int[]{
				centers[0]-1, centers[0], centers[0]+1,
				centers[1]-1, centers[1], centers[1]+1,
				centers[2]-1, centers[2], centers[2]+1,
				centers[3]-1, centers[3], centers[3]+1,
				centers[4]-1, centers[4], centers[4]+1,

		};

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			v.map(map, tileW);
			return v;
		}
	}

	public static class WalkwayExitPieceVisuals extends CustomTilemap {
		{
			texture = Assets.Environment.EYE_OF_WISDOM_TILE;

			tileW = 3;
			tileH = 3;
		}

		private static int[] centers = new int[]{
				52,
				52+21,
				52+21*2,
		};
		private static final int[] map = new int[]{
				centers[0]-1, centers[0], centers[0]+1,
				centers[1]-1, centers[1], centers[1]+1,
				centers[2]-1, centers[2], centers[2]+1,

		};

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			v.map(map, tileW);
			return v;
		}
	}

	public static class OuterPillar extends CustomTilemap {
		{
			texture = Assets.Environment.EYE_OF_WISDOM_TILE;

			tileW = 2;
			tileH = 9;
		}

		private static int[] centers = new int[]{
				2,
				2+21,
				2+21*2,
				2+21*3,
				2+21*4,
				2+21*5,
				2+21*6,
				2+21*7,
				2+21*8,



		};
		private static final int[] map = new int[]{
				centers[0], centers[0]+1,
				centers[1], centers[1]+1,
				centers[2], centers[2]+1,
				centers[3], centers[3]+1,
				centers[4], centers[4]+1,
				centers[5], centers[5]+1,
				centers[6], centers[6]+1,
				centers[7], centers[7]+1,
				centers[8], centers[8]+1,

		};

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			v.map(map, tileW);
			return v;
		}
	}

	public static class CustomBottomTiles extends CustomTilemap {

		{
			texture = Assets.Environment.EYE_OF_WISDOM_TILE;
			tileW = 21;
			tileH = 1;
		}

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			int[] data = new int[tileW*tileH];
			for (int i = 0; i < data.length; i++){
				if (i == 10){
					data[i] = 367;
				}
				else{
					data[i] = i + 399;
				}
			}
			v.map( data, tileW );
			return v;
		}

	}
	public static class CustomBottomWall extends CustomTilemap {

		{
			texture = Assets.Environment.EYE_OF_WISDOM_TILE;
			tileW = 21;
			tileH = 2;
		}

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			int cell = tileX + tileY * Dungeon.level.width();
			int[] map = Dungeon.level.map;
			int[] data = new int[tileW*tileH];
			for (int i = 0; i < data.length; i++){
				if (i % tileW == 0){
					cell = tileX + (tileY + i / tileW) * Dungeon.level.width();
				}

				if (i == 9){
					data[i] = 363;
				}
				else if (i == 10){
					data[i] = 364;
				}
				else if (i == 11){
					data[i] = 365;
				}
				else{
					data[i] = i + 378;
				}

				cell++;
			}
			v.map( data, tileW );
			return v;
		}

	}


}
