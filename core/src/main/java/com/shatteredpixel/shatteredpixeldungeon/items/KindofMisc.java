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

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CapeOfThorns;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.EtherealChains;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.LloydsBeacon;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.MasterThievesArmband;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SandalsOfNature;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.UnstableSpellbook;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfAccuracy;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfElements;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEvasion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfForce;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfFuror;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfTenacity;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfWealth;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;

import java.util.ArrayList;


public abstract class KindofMisc extends EquipableItem {

	private static final float TIME_TO_EQUIP = 1f;

	@Override
	public boolean doEquip(final Hero hero) {


		detach( hero.belongings.backpack );

		ArrayList<Artifact> artifact_lists = hero.belongings.artifacts_list;
		ArrayList<com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring> 	ring_list = hero.belongings.rings_list;

		if (this instanceof Artifact){
			if (artifact_lists.contains(null)){
				for(int i = 0; i < artifact_lists.size(); i++){
					Artifact artifact = artifact_lists.get(i);
					if (artifact == null){
						artifact_lists.set(i, (Artifact) this);
						break;
					}
				}
			}
			else {
				return false;
			}
		}
		else if (this instanceof com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring){
			if (ring_list.contains(null)){
				for(int i = 0; i < ring_list.size(); i++){
					com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring ring = ring_list.get(i);
					if (ring == null){
						ring_list.set(i, (com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring) this);
						break;
					}
				}
			}
			else {
				return false;
			}
		}

		Talent.onItemEquipped(hero, this);
		activate( hero );

		cursedKnown = true;
		if (cursed) {
			equipCursed( hero );
			GLog.n( Messages.get(this, "equip_cursed", this) );
		}

		hero.spendAndNext( TIME_TO_EQUIP );
		return true;

//		boolean equipFull = false;
//		if ( this instanceof Artifact && hero.belongings.artifact != null && hero.belongings.misc != null){
//
//			//see if we can re-arrange items first
//			// If wearing 1 ring and want to wear an artifact
//			if (hero.belongings.misc instanceof Ring && hero.belongings.ring == null){
//				hero.belongings.ring = (Ring) hero.belongings.misc;
//				hero.belongings.misc = null;
//			} else {
//				equipFull = true;
//			}
//		}
//		else if (this instanceof Ring && hero.belongings.misc != null && hero.belongings.ring != null){
//			//see if we can re-arrange items first
//			// If wearing 1 arifact and want to wear a ring
//			if (hero.belongings.misc instanceof Artifact && hero.belongings.artifact == null){
//				hero.belongings.artifact = (Artifact) hero.belongings.misc;
//				hero.belongings.misc = null;
//			} else {
//				equipFull = true;
//			}
//		}
//
//		if (equipFull) {
//
//			final KindofMisc[] miscs = new KindofMisc[3];
//			miscs[0] = hero.belongings.artifact;
//			miscs[1] = hero.belongings.misc;
//			miscs[2] = hero.belongings.ring;
//
//			final boolean[] enabled = new boolean[3];
//			enabled[0] = miscs[0] != null;
//			enabled[1] = miscs[1] != null;
//			enabled[2] = miscs[2] != null;
//
//			//force swapping with the same type of item if 2x of that type is already present
//			if (this instanceof Ring && hero.belongings.misc instanceof Ring){
//				enabled[0] = false; //disable artifact
//			} else if (this instanceof Artifact && hero.belongings.misc instanceof Artifact){
//				enabled[2] = false; //disable ring
//			}
//
//			GameScene.show(
//					new WndOptions(Messages.get(KindofMisc.class, "unequip_title"),
//							Messages.get(KindofMisc.class, "unequip_message"),
//							miscs[0] == null ? "---" : Messages.titleCase(miscs[0].toString()),
//							miscs[1] == null ? "---" : Messages.titleCase(miscs[1].toString()),
//							miscs[2] == null ? "---" : Messages.titleCase(miscs[2].toString())) {
//
//						@Override
//						protected void onSelect(int index) {
//
//							KindofMisc equipped = miscs[index];
//							int slot = Dungeon.quickslot.getSlot(KindofMisc.this);
//							detach(hero.belongings.backpack);
//							if (equipped.doUnequip(hero, true, false)) {
//								//swap out equip in misc slot if needed
//								if (index == 0 && KindofMisc.this instanceof Ring){
//									hero.belongings.artifact = (Artifact)hero.belongings.misc;
//									hero.belongings.misc = null;
//								} else if (index == 2 && KindofMisc.this instanceof Artifact){
//									hero.belongings.ring = (Ring) hero.belongings.misc;
//									hero.belongings.misc = null;
//								}
//								doEquip(hero);
//							} else {
//								collect();
//							}
//							if (slot != -1) Dungeon.quickslot.setSlot(slot, KindofMisc.this);
//							updateQuickslot();
//						}
//
//						@Override
//						protected boolean enabled(int index) {
//							return enabled[index];
//						}
//					});
//
//			return false;
//
//		}
//
//		else {
//			if (this instanceof Artifact){
//				if (hero.belongings.artifact == null)   hero.belongings.artifact = (Artifact) this;
//				else                                    hero.belongings.misc = (Artifact) this;
//			} else if (this instanceof Ring){
//				if (hero.belongings.ring == null)   hero.belongings.ring = (Ring) this;
//				else                                hero.belongings.misc = (Ring) this;
//			}
//
//			detach( hero.belongings.backpack );
//
//			Talent.onItemEquipped(hero, this);
//			activate( hero );
//
//			cursedKnown = true;
//			if (cursed) {
//				equipCursed( hero );
//				GLog.n( Messages.get(this, "equip_cursed", this) );
//			}
//
//			hero.spendAndNext( TIME_TO_EQUIP );
//			return true;
//
//		}

	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single) {
		if (super.doUnequip(hero, collect, single)){

			ArrayList<Artifact> artifact_lists = hero.belongings.artifacts_list;
			ArrayList<com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring> 	ring_list = hero.belongings.rings_list;

			if(this instanceof Artifact){
				for(int i = 0; i < artifact_lists.size(); i++){
					Artifact artifact = artifact_lists.get(i);
					if (artifact == this){
						artifact_lists.set(i, null);
						return true;
					}
				}
			}
			else if(this instanceof com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring){
				for(int i = 0; i < ring_list.size(); i++){
					com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring ring = ring_list.get(i);
					if (ring == this){
						ring_list.set(i, null);
						return true;
					}
				}
			}


		}
		return false;
	}

	@Override
	public boolean isEquipped( Hero hero ) {

		ArrayList<Artifact> artifact_lists = hero.belongings.artifacts_list;
		ArrayList<com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring> 	ring_list = hero.belongings.rings_list;

		if(this instanceof Artifact){
			for(int i = 0; i < artifact_lists.size(); i++){
				Artifact artifact = artifact_lists.get(i);
				if (artifact == this){
					return artifact == this;
				}
			}
		}
		else if(this instanceof com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring){
			for(int i = 0; i < ring_list.size(); i++){
				Ring ring = ring_list.get(i);
				if (ring == this){
					return ring == this;
				}
			}
		}
		return false;

	}
}
