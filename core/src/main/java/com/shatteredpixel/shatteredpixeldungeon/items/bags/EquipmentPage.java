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

package com.shatteredpixel.shatteredpixeldungeon.items.bags;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class EquipmentPage extends Bag {

	{
		image = ItemSpriteSheet.WEAPON_HOLDER;
	}

	public static final float HOLSTER_SCALE_FACTOR = 0.85f;
//	public static final float HOLSTER_DURABILITY_FACTOR = 1.2f;
	
	@Override
	public boolean canHold( Item item ) {
//		if (super.items.size() >= super.capacity() && super.items.size() < (super.capacity() + capacity())){
//			return true;
//		} else if (item.stackable) {
//			for (Item i : items) {
//				if (item.isSimilar( i )) {
//					return true;
//				}
//			}
//		}
//		if (items.contains(item) || item instanceof Bag || items.size() < capacity()){
//			return true;
//		} else if (item.stackable) {
//			for (Item i : items) {
//				if (item.isSimilar( i )) {
//					return true;
//				}
//			}
//		}
		return false;
	}

	public int capacity(){
		return 0;
	}

	@Override
	public int value() {
		return 60;
	}

}
