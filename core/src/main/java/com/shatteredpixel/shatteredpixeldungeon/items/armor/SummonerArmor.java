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

package com.shatteredpixel.shatteredpixeldungeon.items.armor;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Shuriken;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.HashMap;

public class SummonerArmor extends ClassArmor {

	
	{
		image = ItemSpriteSheet.ARMOR_SUMMONER;
	}
	
	@Override
	public void doSpecial() {
		GameScene.selectCell( teleporter );
	}

	protected CellSelector.Listener teleporter = new CellSelector.Listener() {

		@Override
		public void onSelect( Integer target ) {
			if (target != null) {

				PathFinder.buildDistanceMap(curUser.pos, BArray.not(Dungeon.level.solid,null));

				int pos = target + PathFinder.NEIGHBOURS8[Random.Int(8)];

				//Checks whether an ally is clicked
				if (!Dungeon.level.heroFOV[target] || (Actor.findChar( target ) != null && Actor.findChar( target ).alignment != Char.Alignment.ALLY)) {
					GLog.w( Messages.get(SummonerArmor.class, "fov") );
					return;
				}

				RegularLevel level = (RegularLevel) Dungeon.level;
				while (Actor.findChar(pos) != null || !level.passable[pos]
						|| level.avoid[pos]
						|| level.losBlocking[pos]
						|| level.pit[pos]){
					pos = target + PathFinder.NEIGHBOURS8[Random.Int(8)];
				}

				charge -= 35;
				updateQuickslot();

				for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
					if (Dungeon.level.adjacent(mob.pos, curUser.pos) && mob.alignment != Char.Alignment.ALLY) {
						mob.sprite.emitter().burst( Speck.factory( Speck.LIGHT ), 4 );
					}
				}

//				Buff.affect(curUser, Invisibility.class, Invisibility.DURATION/2f);

//				CellEmitter.get( curUser.pos ).burst( Speck.factory( Speck.WOOL ), 10 );

				ScrollOfTeleportation.appear( curUser, pos );
//				Sample.INSTANCE.play( Assets.Sounds.PUFF );
				Dungeon.level.occupyCell(curUser );
				Dungeon.observe();
				GameScene.updateFog();

				curUser.spendAndNext( Actor.TICK );
			}
		}

		@Override
		public String prompt() {
			return Messages.get(SummonerArmor.class, "prompt");
		}
	};
}