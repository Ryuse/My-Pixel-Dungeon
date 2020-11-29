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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.KindofMisc;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.EyeOfWisdom;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.helmets.Helmet;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.Key;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Belongings implements Iterable<Item> {

	private Hero owner;
	
	public Bag backpack;

	public KindOfWeapon weapon = null;

	public int helmets_num = 5;
	public ArrayList<Helmet> helmets_list = new ArrayList<Helmet>();
	public ArrayList<String> HELMETS = new ArrayList<String>();

	public int artifacts_num = 5;
	public ArrayList<Artifact> artifacts_list = new ArrayList<Artifact>();
	public ArrayList<String> ARTIFACTS = new ArrayList<String>();

	public int rings_num = 5;
	public ArrayList<Ring> rings_list = new ArrayList<Ring>();
	public ArrayList<String> RINGS = new ArrayList<String>();

	public int armors_num = 6;
	public ArrayList<Armor> armors_list = new ArrayList<Armor>();
	public ArrayList<String> ARMORS = new ArrayList<String>();

	//used when thrown weapons temporary occupy the weapon slot
	public KindOfWeapon stashedWeapon = null;
	
	public Belongings( Hero owner ) {
		this.owner = owner;
		
		backpack = new Bag() {
			public int capacity(){
				int cap = super.capacity();
				for (Item item : items){
					if (item instanceof Bag){
						cap++;
					}
				}
				return cap;
			}
		};
		backpack.owner = owner;

		if(helmets_list.size() < helmets_num){
			for (int x = 0; x < helmets_num; x++) {
				helmets_list.add(null);
				HELMETS.add(String.format("helmet%s", x));
			}
		}

		if(armors_list.size() < armors_num){
			for (int x = 0; x < armors_num; x++) {
				armors_list.add(null);
				ARMORS.add(String.format("armor%s", x));
			}
		}

		if(artifacts_list.size() < artifacts_num){
			for (int x = 0; x < artifacts_num; x++) {
				artifacts_list.add(null);
				ARTIFACTS.add(String.format("artifact%s", x));
			}
		}

		if(rings_list.size() < rings_num){
			for (int x = 0; x < rings_num; x++) {
				rings_list.add(null);
				RINGS.add(String.format("ring%s", x));
			}
		}
	}

	private static final String WEAPON			= "weapon";

	public void storeInBundle( Bundle bundle ) {
		
		backpack.storeInBundle( bundle );
		
		bundle.put( WEAPON, weapon );

		for (int x = 0; x < helmets_num; x++) {
			bundle.put( HELMETS.get(x), helmets_list.get(x) );
		}

		for (int x = 0; x < armors_num; x++) {
			bundle.put( ARMORS.get(x), armors_list.get(x) );
		}

		for (int x = 0; x < artifacts_num; x++) {
			bundle.put( ARTIFACTS.get(x), artifacts_list.get(x) );
		}

		for (int x = 0; x < rings_num; x++) {
			bundle.put( RINGS.get(x), rings_list.get(x) );
		}

	}
	
	public void restoreFromBundle( Bundle bundle ) {
		
		backpack.clear();
		backpack.restoreFromBundle( bundle );
		
		weapon = (KindOfWeapon) bundle.get(WEAPON);
		if (weapon != null) {
			weapon.activate(owner);
		}

		for (int x = 0; x < helmets_num; x++) {
			helmets_list.set(x, (Helmet) bundle.get( HELMETS.get(x)));
			if (helmets_list.get(x) != null){
				helmets_list.get(x).activate(owner);
			}
		}

		for (int x = 0; x < armors_num; x++) {
			armors_list.set(x, (Armor) bundle.get( ARMORS.get(x)));
			if (armors_list.get(x) != null){
				armors_list.get(x).activate(owner);
			}
		}

		for (int x = 0; x < artifacts_num; x++) {
			artifacts_list.set(x, (Artifact) bundle.get( ARTIFACTS.get(x)));
			if (artifacts_list.get(x) != null){
				artifacts_list.get(x).activate(owner);
			}
		}

		for (int x = 0; x < rings_num; x++) {
			rings_list.set(x, (Ring) bundle.get( RINGS.get(x)));
			if (rings_list.get(x) != null){
				rings_list.get(x).activate(owner);
			}
		}
	}
	
	public static void preview( GamesInProgress.Info info, Bundle bundle ) {
		int highest_tier = 0;
		for (int x = 0; x < 5; x++) {
			if (bundle.contains(String.format("armor%s", x))){
				if(((Armor) bundle.get(String.format("armor%s", x))).tier > highest_tier){
					highest_tier = ((Armor) bundle.get(String.format("armor%s", x))).tier;
				}
			}
		}
		info.armorTier = highest_tier;
	}
	
	@SuppressWarnings("unchecked")
	public<T extends Item> T getItem( Class<T> itemClass ) {

		for (Item item : this) {
			if (itemClass.isInstance( item )) {
				return (T)item;
			}
		}
		
		return null;
	}
	
	public boolean contains( Item contains ){
		
		for (Item item : this) {
			if (contains == item ) {
				return true;
			}
		}
		
		return false;
	}
	
	public Item getSimilar( Item similar ){
		
		for (Item item : this) {
			if (similar != item && similar.isSimilar(item)) {
				return item;
			}
		}
		
		return null;
	}
	
	public ArrayList<Item> getAllSimilar( Item similar ){
		ArrayList<Item> result = new ArrayList<>();
		
		for (Item item : this) {
			if (item != similar && similar.isSimilar(item)) {
				result.add(item);
			}
		}
		
		return result;
	}
	
	public void identify() {
		for (Item item : this) {
			item.identify();
		}
	}
	
	public void observe() {

		ArrayList<EquipableItem> equipped = new ArrayList<EquipableItem>();
		equipped.add(weapon);
		for(Helmet helmet: helmets_list){
			equipped.add(helmet);
		}
		for(Armor armor: armors_list){
			equipped.add(armor);
		}
		for(Artifact artifact: artifacts_list){
			equipped.add(artifact);
		}
		for(Ring ring: rings_list){
			equipped.add(ring);
		}

		for (int i = 0; i < equipped.size(); i++){
			if(equipped.get(i) != null){
				equipped.get(i).identify();
				Badges.validateItemLevelAquired( equipped.get(i) );
			}
		}

	}
	
	public void uncurseEquipped() {


		ArrayList<EquipableItem> equipped = new ArrayList<EquipableItem>();
		equipped.add(weapon);
		for(Helmet helmet: helmets_list){
			equipped.add(helmet);
		}
		for(Armor armor: armors_list){
			equipped.add(armor);
		}
		for(Artifact artifact: artifacts_list){
			equipped.add(artifact);
		}
		for(Ring ring: rings_list){
			equipped.add(ring);
		}

		for (Item item: equipped){
			boolean uncursed = ScrollOfRemoveCurse.uncurse(owner, item);
			if (uncursed) break;
		}

	}
	
	public Item randomUnequipped() {
		return Random.element( backpack.items );
	}
	
	public void resurrect( int depth ) {

		for (Item item : backpack.items.toArray( new Item[0])) {
			if (item instanceof Key) {
				if (((Key)item).depth == depth) {
					item.detachAll( backpack );
				}
			} else if (item.unique) {
				item.detachAll(backpack);
				//you keep the bag itself, not its contents.
				if (item instanceof Bag){
					((Bag)item).resurrect();
				}
				item.collect();
			} else if (!item.isEquipped( owner )) {
				item.detachAll( backpack );
			}
		}

//		EquipableItem[] equipped = {weapon};

		ArrayList<EquipableItem> equipped = new ArrayList<EquipableItem>();
		equipped.add(weapon);
		for(Helmet helmet: helmets_list){
			equipped.add(helmet);
		}
		for(Armor armor: armors_list){
			equipped.add(armor);
		}
		for(Artifact artifact: artifacts_list){
			equipped.add(artifact);
		}
		for(Ring ring: rings_list){
			equipped.add(ring);
		}


		for (int i = 0; i < equipped.size(); i++){
			equipped.get(i).cursed = false;
			equipped.get(i).activate(owner);
		}
	}
	
	public int charge( float charge ) {
		
		int count = 0;
		
		for (Wand.Charger charger : owner.buffs(Wand.Charger.class)){
			charger.gainCharge(charge);
			count++;
		}
		
		return count;
	}

	@Override
	public Iterator<Item> iterator() {
		return new ItemIterator();
	}
	
	private class ItemIterator implements Iterator<Item> {

		private int index = 0;



		private Iterator<Item> backpackIterator = backpack.iterator();

		Item[] getItems(){
			ArrayList<Item> equipped = new ArrayList<Item>();
			equipped.add((KindOfWeapon) weapon);
			for(Helmet helmet: helmets_list){
				equipped.add(helmet);
			}
			for(Armor armor: armors_list){
				equipped.add(armor);
			}
			for(Artifact artifact: artifacts_list){
				equipped.add(artifact);
			}
			for(Ring ring: rings_list){
				equipped.add(ring);
			}

			Item[] equipped_array = new Item[equipped.size()];
			equipped_array = equipped.toArray(equipped_array);

			return equipped_array;
		}

		private Item[] equipped = getItems();

		private int backpackIndex = equipped.length;




		@Override
		public boolean hasNext() {
			
			for (int i=index; i < backpackIndex; i++) {
				if (equipped[i] != null) {
					return true;
				}
			}
			
			return backpackIterator.hasNext();
		}

		@Override
		public Item next() {
			
			while (index < backpackIndex) {
				Item item = equipped[index++];
				if (item != null) {
					return item;
				}
			}
			
			return backpackIterator.next();
		}

		@Override
		public void remove() {

			boolean broken = false;
			for (int i = 0; i < equipped.length; i++){
				if (index == i){
					equipped[i] = null;
					broken = true;
					break;
				}
			}

			if (!broken){
				backpackIterator.remove();
			}

		}
	}

	public boolean eyeEquipped(){
		for(int i = 0; i < artifacts_list.size(); i++){
			Artifact artifact = artifacts_list.get(i);
			if(artifact instanceof EyeOfWisdom){
				return true;
			}
		}
		return false;
	};

}
