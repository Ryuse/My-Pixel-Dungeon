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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.helmets.HeaddressRegeneration;
import com.shatteredpixel.shatteredpixeldungeon.items.helmets.Helmet;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;

import java.util.ArrayList;

public class Regeneration extends Buff {
	
	{
		//unlike other buffs, this one acts after the hero and takes priority against other effects
		//healing is much more useful if you get some of it off before taking damage
		actPriority = HERO_PRIO - 1;
	}
	
	private static final float REGENERATION_DELAY = 10;
	
	@Override
	public boolean act() {
		if (target.isAlive()) {
			float delay = REGENERATION_DELAY;

			if (!(target instanceof DriedRose.GhostHero) && target.HP < regencap() && !((Hero)target).isStarving()) {
				LockedFloor lock = target.buff(LockedFloor.class);
				if (target.HP > 0 && (lock == null || lock.regenOn())) {

					ArrayList<Helmet> helmets_list = Dungeon.hero.belongings.helmets_list;

					for(int i = 0; i < helmets_list.size(); i++){
						Helmet helmet = helmets_list.get(i);
						if(helmet instanceof HeaddressRegeneration){
							if (helmet.cursed){
								delay *= 1.1;
							}
							else{
								target.HP += 1;
							}
						}
					}

					target.HP += 1;
					if (target.HP == regencap()) {
						((Hero) target).resting = false;
					}
				}
			}
			if (Dungeon.hero.heroClass == HeroClass.SUMMONER){
				for (Artifact artifact : Dungeon.hero.belongings.artifacts_list){
					if (artifact instanceof DriedRose && DriedRose.ghost != null && target instanceof DriedRose.GhostHero && target.HP < regencap() ){
						LockedFloor lock = target.buff(LockedFloor.class);
						if (target.HP > 0 && (lock == null || lock.regenOn())) {
							target.HP += 1;
							if (((DriedRose) artifact).helmet instanceof HeaddressRegeneration){
								target.HP += 1;
							}
						}
					}
				}
			}


			ChaliceOfBlood.chaliceRegen regenBuff = Dungeon.hero.buff( ChaliceOfBlood.chaliceRegen.class);

			if (regenBuff != null) {
				if (regenBuff.isCursed()) {
					delay *= 1.5f;
				} else {
					delay -= regenBuff.itemLevel()*1.0f;
					delay /= RingOfEnergy.artifactChargeMultiplier(target);
				}
			}
			spend( delay );
			
		} else {
			
			diactivate();
			
		}
		
		return true;
	}
	
	public int regencap(){
		return target.HT;
	}
}
