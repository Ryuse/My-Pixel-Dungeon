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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.PollutedCrab;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.CityLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.DeadEndLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.HallsLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.LastLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.LastShopLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.NewCavesBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.NewCityBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.NewHallsBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.NewPrisonBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.PollutedSewerLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.PrisonLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.SewerBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.SewerLevel;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bestiary {

	public static Class<?>[] sewerMobs = new Class<?>[]{
			Rat.class, // Albino Rat
			Snake.class,
			Gnoll.class,
			Crab.class,
			Swarm.class
	};

	public static float[] sewerMobsProb = new float[]{
			5 - Dungeon.depth,
			4 - Dungeon.depth,
			Dungeon.depth - 1,
			Dungeon.depth - 2,
			Dungeon.depth - 3
	};

	public static Class<?>[] pollutedSewerMobs = new Class<?>[]{
			PollutedRat.class, // Fetid Rat
			Swarm.class,
			PollutedCrab.class,
			Slime.class,
			CausticSlime.class,
	};

	public static float[] pollutedSewerMobsProb = new float[]{
			10 - Dungeon.depth,
			9 - Dungeon.depth,
			Dungeon.depth - 6,
			Dungeon.depth - 7,
			Dungeon.depth - 8
	};

	public static Class<?>[] prisonMobs = new Class<?>[]{
			Skeleton.class,
			Thief.class, // Bandit
			Guard.class,
			DM100.class,
			Necromancer.class,
	};

	public static float[] prisonMobsProb = new float[]{
			15 - Dungeon.depth,
			14 - Dungeon.depth,
			Dungeon.depth - 11,
			Dungeon.depth - 12,
			Dungeon.depth - 13
	};

	public static ArrayList<Class<? extends Mob>> getMobRotation( int depth ){
		ArrayList<Class<? extends Mob>> mobs = standardMobRotation( depth );
		addRareMobs(depth, mobs);
		swapMobAlts(mobs);
		Random.shuffle(mobs);
		return mobs;
	}
	
	//returns a rotation of standard mobs, unshuffled.
	private static ArrayList<Class<? extends Mob>> standardMobRotation( int depth ){


		ArrayList<Class<? extends Mob>> moblist = new ArrayList<>();

		if(Dungeon.sewers.contains(depth))
		{

			for(int i = 0; i < sewerMobs.length; i++) {
				if (sewerMobsProb[i] > 0){
					for(int j = 0; j < sewerMobsProb[i]; j++){
						moblist.add((Class<? extends Mob>) sewerMobs[i]);
					}
				}
			}

			return moblist;
		}
		else if(Dungeon.polluted_sewers.contains(depth)){
			for(int i = 0; i < pollutedSewerMobs.length; i++) {
				if (pollutedSewerMobsProb[i] > 0){
					for(int j = 0; j < pollutedSewerMobsProb[i]; j++){
						moblist.add((Class<? extends Mob>) pollutedSewerMobs[i]);
					}
				}
			}
			return moblist;
		}
		else if(Dungeon.prison.contains(depth)){
			return new ArrayList<>(Arrays.asList(
					Skeleton.class, Skeleton.class,
					Thief.class,
					DM100.class, DM100.class,
					Guard.class, Guard.class,
					Necromancer.class));
		}
		else if(Dungeon.caves.contains(depth)){
			return new ArrayList<>(Arrays.asList(
					Bat.class,
					Brute.class, Brute.class,
					Shaman.random(), Shaman.random(),
					Spinner.class, Spinner.class,
					DM200.class));
		}
		else if(Dungeon.city.contains(depth)){
			return new ArrayList<>(Arrays.asList(
					Ghoul.class,
					Elemental.random(),
					Warlock.class, Warlock.class,
					Monk.class, Monk.class,
					Golem.class));
		}
		else if(Dungeon.halls.contains(depth)){
			return new ArrayList<>(Arrays.asList(
					Succubus.class,
					Eye.class, Eye.class,
					Scorpio.class, Scorpio.class, Scorpio.class));
		}
		else{
			return new ArrayList<>(Arrays.asList(Rat.class, Rat.class, Rat.class, Snake.class));
		}
//		switch(depth){
//
//			// Sewers
//			case 1: default:
//				//3x rat, 1x snake
//				return new ArrayList<>(Arrays.asList(
//						Rat.class, Rat.class, Rat.class,
//						Snake.class));
//			case 2:
//				//2x rat, 1x snake, 2x gnoll
//				return new ArrayList<>(Arrays.asList(Rat.class, Rat.class,
//						Snake.class,
//						Gnoll.class, Gnoll.class));
//			case 3:
//				//1x rat, 1x snake, 3x gnoll, 1x swarm, 1x crab
//				return new ArrayList<>(Arrays.asList(Rat.class,
//						Snake.class,
//						Gnoll.class, Gnoll.class, Gnoll.class,
//						Swarm.class,
//						Crab.class));
//			case 4: case 5:
//				//1x gnoll, 1x swarm, 2x crab, 2x slime
//				return new ArrayList<>(Arrays.asList(Gnoll.class,
//						Swarm.class,
//						Crab.class, Crab.class,
//						Slime.class, Slime.class));
//
//			// Prison
//			case 6:
//				//3x skeleton, 1x thief, 1x swarm
//				return new ArrayList<>(Arrays.asList(Skeleton.class, Skeleton.class, Skeleton.class,
//						Thief.class,
//						Swarm.class));
//			case 7:
//				//3x skeleton, 1x thief, 1x DM-100, 1x guard
//				return new ArrayList<>(Arrays.asList(Skeleton.class, Skeleton.class, Skeleton.class,
//						Thief.class,
//						DM100.class,
//						Guard.class));
//			case 8:
//				//2x skeleton, 1x thief, 2x DM-100, 2x guard, 1x necromancer
//				return new ArrayList<>(Arrays.asList(Skeleton.class, Skeleton.class,
//						Thief.class,
//						DM100.class, DM100.class,
//						Guard.class, Guard.class,
//						Necromancer.class));
//			case 9: case 10:
//				//1x skeleton, 1x thief, 2x DM-100, 2x guard, 2x necromancer
//				return new ArrayList<>(Arrays.asList(Skeleton.class,
//						Thief.class,
//						DM100.class, DM100.class,
//						Guard.class, Guard.class,
//						Necromancer.class, Necromancer.class));
//
//			// Caves
//			case 11:
//				//3x bat, 1x brute, 1x shaman
//				return new ArrayList<>(Arrays.asList(
//						Bat.class, Bat.class, Bat.class,
//						Brute.class,
//						Shaman.random()));
//			case 12:
//				//2x bat, 2x brute, 1x shaman, 1x spinner
//				return new ArrayList<>(Arrays.asList(
//						Bat.class, Bat.class,
//						Brute.class, Brute.class,
//						Shaman.random(),
//						Spinner.class));
//			case 13:
//				//1x bat, 2x brute, 2x shaman, 2x spinner, 1x DM-200
//				return new ArrayList<>(Arrays.asList(
//						Bat.class,
//						Brute.class, Brute.class,
//						Shaman.random(), Shaman.random(),
//						Spinner.class, Spinner.class,
//						DM200.class));
//			case 14: case 15:
//				//1x bat, 1x brute, 2x shaman, 2x spinner, 2x DM-300
//				return new ArrayList<>(Arrays.asList(
//						Bat.class,
//						Brute.class,
//						Shaman.random(), Shaman.random(),
//						Spinner.class, Spinner.class,
//						DM200.class, DM200.class));
//
//			// City
//			case 16:
//				//2x ghoul, 2x elemental, 1x warlock
//				return new ArrayList<>(Arrays.asList(
//						Ghoul.class, Ghoul.class,
//						Elemental.random(), Elemental.random(),
//						Warlock.class));
//			case 17:
//				//1x ghoul, 2x elemental, 1x warlock, 1x monk
//				return new ArrayList<>(Arrays.asList(
//						Ghoul.class,
//						Elemental.random(), Elemental.random(),
//						Warlock.class,
//						Monk.class));
//			case 18:
//				//1x ghoul, 1x elemental, 2x warlock, 2x monk, 1x golem
//				return new ArrayList<>(Arrays.asList(
//						Ghoul.class,
//						Elemental.random(),
//						Warlock.class, Warlock.class,
//						Monk.class, Monk.class,
//						Golem.class));
//			case 19: case 20:
//				//1x elemental, 2x warlock, 2x monk, 3x golem
//				return new ArrayList<>(Arrays.asList(
//						Elemental.random(),
//						Warlock.class, Warlock.class,
//						Monk.class, Monk.class,
//						Golem.class, Golem.class, Golem.class));
//
//			// Halls
//			case 21:
//				//2x succubus, 1x evil eye
//				return new ArrayList<>(Arrays.asList(
//						Succubus.class, Succubus.class,
//						Eye.class));
//			case 22:
//				//1x succubus, 1x evil eye
//				return new ArrayList<>(Arrays.asList(
//						Succubus.class,
//						Eye.class));
//			case 23:
//				//1x succubus, 2x evil eye, 1x scorpio
//				return new ArrayList<>(Arrays.asList(
//						Succubus.class,
//						Eye.class, Eye.class,
//						Scorpio.class));
//			case 24: case 25: case 26:
//				//1x succubus, 2x evil eye, 3x scorpio
//				return new ArrayList<>(Arrays.asList(
//						Succubus.class,
//						Eye.class, Eye.class,
//						Scorpio.class, Scorpio.class, Scorpio.class));
//		}
		
	}
	
	//has a chance to add a rarely spawned mobs to the rotation
	public static void addRareMobs( int depth, ArrayList<Class<?extends Mob>> rotation ){
		
		switch (depth){
			
			// Sewers
			default:
				return;
			case 4:
				if (Random.Float() < 0.025f) rotation.add(PollutedRat.class);
				return;
			//Polluted Sewers
			case 9:
				if (Random.Float() < 0.025f) rotation.add(Thief.class);
				return;
			// Prison
			case 14:
				if (Random.Float() < 0.025f) rotation.add(Bat.class);
				return;
			// Caves
			case 19:
				if (Random.Float() < 0.025f) rotation.add(Ghoul.class);
				return;
			// City
			case 24:
				if (Random.Float() < 0.025f) rotation.add(Succubus.class);
				return;
		}
	}
	
	//switches out regular mobs for their alt versions when appropriate
	private static void swapMobAlts(ArrayList<Class<?extends Mob>> rotation){
		for (int i = 0; i < rotation.size(); i++){
			if (Random.Int( 10 ) == 0) {
				Class<? extends Mob> cl = rotation.get(i);
				if (cl == Rat.class) {
					cl = Albino.class;
				} else if (cl == Slime.class) {
					cl = CausticSlime.class;
				} else if (cl == PollutedRat.class) {
					cl = FetidRat.class;
				} else if (cl == Thief.class) {
					cl = Bandit.class;
				} else if (cl == Brute.class) {
					cl = ArmoredBrute.class;
				} else if (cl == DM200.class) {
					cl = DM201.class;
				} else if (cl == Monk.class) {
					cl = Senior.class;
				} else if (cl == Scorpio.class) {
					cl = Acidic.class;
				}
				rotation.set(i, cl);
			}
		}
	}
}
