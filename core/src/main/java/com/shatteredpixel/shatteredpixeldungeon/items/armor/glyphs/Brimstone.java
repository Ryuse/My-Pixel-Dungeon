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

package com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShieldBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;

import java.util.ArrayList;

public class Brimstone extends Armor.Glyph {

	private static ItemSprite.Glowing ORANGE = new ItemSprite.Glowing( 0xFF4400 );

	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {
		//no proc effect, see Hero.isImmune and GhostHero.isImmune
		return damage;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return ORANGE;
	}

	//pre-0.7.4 saves
	public static class BrimstoneShield extends ShieldBuff {
		
		{
			type = buffType.POSITIVE;
		}

		@Override
		public boolean act() {
			Hero hero = (Hero)target;

			ArrayList<Armor> armor_list = hero.belongings.armors_list;
			boolean has_brimstone = false;
			for (int i = 0; i < armor_list.size(); i++){
				Armor armor = armor_list.get(i);
				if (armor != null && armor.hasGlyph(Brimstone.class, hero)){
					has_brimstone = true;
				}
			}

			if(!has_brimstone){
				detach();
				return true;
			}

			if (shielding() > 0){
				decShield();

				//shield decays at a rate of 1 per turn.
				spend(TICK);
			} else {
				detach();
			}

			return true;
		}

	}

}