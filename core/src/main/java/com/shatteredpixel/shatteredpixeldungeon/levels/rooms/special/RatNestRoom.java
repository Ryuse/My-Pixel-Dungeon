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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.EternalFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Albino;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EntranceRoom;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import java.util.ArrayList;

public class RatNestRoom extends SpecialRoom {

	int size = 7;
	public static int mapCenterPos;
	@Override
	public int minWidth() { return size; }
	public int maxWidth() { return size+2; }

	@Override
	public int minHeight() { return size; }
	public int maxHeight() { return size+2; }


	@Override
	public void paint(Level level) {

		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY );

		Point c = center();
		int cx = c.x;
		int cy = c.y;

		int numMob = Random.NormalIntRange(4, 8);
		int mysteryMeatNum = Random.NormalIntRange(3, 6);

		Door door = entrance();
		door.set(Door.Type.UNLOCKED);

		Mob nestMob;
		Mob nestAlphaMob;

		nestAlphaMob = new Albino();

		int centerPos = cx + cy * level.width();
		nestAlphaMob.pos = centerPos;
		level.mobs.add( nestAlphaMob );

//		EternalFire light = (EternalFire)level.blobs.get( EternalFire.class );
//		if (light == null) {
//			light = new EternalFire();
//		}
//
//		light.seed(level, centerPos, 1);
//		level.blobs.put( EternalFire.class, light );

		for (int i=0; i < numMob; i++) {
			nestMob = new Rat();
			int pos;
			do {
				pos = level.pointToCell(random());
			}
			while (level.findMob(pos) != null);

			nestMob.pos = pos;
			level.mobs.add( nestMob );
		}

		for (int i=0; i < mysteryMeatNum; i++){
			int pos;
			do {
				pos = level.pointToCell(random());
			}
			while (level.heaps.get( pos ) != null);
			level.drop( new MysteryMeat(), pos );
		}

		CustomFloor nest = new CustomFloor();
		nest.setRect(left, top, width(), height());
		level.customTiles.add(nest);

	}

	@Override
	public boolean connect(Room room) {
		//cannot connect to entrance, otherwise works normally
		if (room instanceof EntranceRoom) return false;
		else                              return super.connect(room);
	}

	@Override
	public boolean canPlaceTrap(Point p) {
		return false;
	}

	@Override
	public boolean canPlaceWater(Point p) {
		return false;
	}

	@Override
	public boolean canPlaceGrass(Point p) {
		return false;
	}

	public static class CustomFloor extends CustomTilemap {

	{
		texture = Assets.Environment.SEWER_SP;
	}

	@Override
	public Tilemap create() {
		Tilemap v = super.create();
		int cell = tileX + tileY * Dungeon.level.width();
		int[] data = new int[tileW*tileH];


		ArrayList<Integer> nestTiles = new ArrayList<Integer>();
		for (int i = 0; i < data.length; i++){

			if (i % tileW == 0){
				cell = tileX + (tileY + i / tileW) * Dungeon.level.width();
			}

			if (cell == center()){

				GLog.newLine();
				GLog.p("" + mapCenterPos);

				//First row of sprite
				data[i-2*tileW-2] 	= 0;
				data[i-2*tileW-1] 	= 1;
				data[i-2*tileW] 	= 2;
				data[i-2*tileW+1] 	= 3;
				data[i-2*tileW+2] 	= 4;

				nestTiles.add(i-2*tileW-2);
				nestTiles.add(i-2*tileW-1);
				nestTiles.add(i-2*tileW);
				nestTiles.add(i-2*tileW+1);
				nestTiles.add(i-2*tileW+2);

				//Second row of sprite
				data[i-tileW-2] = 0 + 1*10;
				data[i-tileW-1] = 1 + 1*10;
				data[i-tileW] 	= 2 + 1*10;
				data[i-tileW+1] = 3 + 1*10;
				data[i-tileW+2] = 4 + 1*10;

				nestTiles.add(i-tileW-2);
				nestTiles.add(i-tileW-1);
				nestTiles.add(i-tileW);
				nestTiles.add(i-tileW+1);
				nestTiles.add(i-tileW+2);

				//Third row of sprite
				data[i-2] 	= 0 + 2*10;
				data[i-1] 	= 1 + 2*10;
				data[i] 	= 2 + 2*10;
				data[i+1] 	= 3 + 2*10;
				data[i+2] 	= 4 + 2*10;

				nestTiles.add(i-2);
				nestTiles.add(i-1);
				nestTiles.add(i);
				nestTiles.add(i+1);
				nestTiles.add(i+2);

				//Fourth row of sprite
				data[i+tileW-2] = 0 + 3*10;
				data[i+tileW-1] = 1 + 3*10;
				data[i+tileW] 	= 2 + 3*10;
				data[i+tileW+1] = 3 + 3*10;
				data[i+tileW+2] = 4 + 3*10;

				nestTiles.add(i+tileW-2);
				nestTiles.add(i+tileW-1);
				nestTiles.add(i+tileW);
				nestTiles.add(i+tileW+1);
				nestTiles.add(i+tileW+2);

				//Fifth row of sprite
				data[i+2*tileW-2] 	= 0 + 4*10;
				data[i+2*tileW-1] 	= 1 + 4*10;
				data[i+2*tileW] 	= 2 + 4*10;
				data[i+2*tileW+1] 	= 3 + 4*10;
				data[i+2*tileW+2] 	= 4 + 4*10;

				nestTiles.add(i+2*tileW-2);
				nestTiles.add(i+2*tileW-1);
				nestTiles.add(i+2*tileW);
				nestTiles.add(i+2*tileW+1);
				nestTiles.add(i+2*tileW+2);

				cell++;;
				break;
			}
			else if (nestTiles.contains(i)){
				cell++;
			}
			else{
				data[i] = 7;
				cell++;
			}
		}
		v.map( data, tileW );
		return v;
	}

	}

}
