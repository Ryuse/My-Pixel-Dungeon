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

package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Albino;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Crab;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.FetidRat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Gnoll;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollTrickster;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.PollutedRat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Snake;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Swarm;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.helmets.StrawHat;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Statistics {
	
	public static int goldCollected;
	public static int deepestFloor;
	public static int enemiesSlain;
	public static int foodEaten;
	public static int potionsCooked;
	public static int piranhasKilled;
	public static int ankhsUsed;
	
	//used for hero unlock badges
	public static int upgradesUsed;
	public static int sneakAttacks;
	public static int thrownAssists;
	public static int mirrorImageScrollUsed;

	public static int spawnersAlive;
	
	public static float duration;
	
	public static boolean qualifiedForNoKilling = false;
	public static boolean completedWithNoKilling = false;
	
	public static boolean amuletObtained = false;


	//Mobs killed
	public static int mobsNumTypeKilled;
	public static ArrayList<Mob> mobList;
	public static int[] mobsKilledTier;

	public static void reset() {
		
		goldCollected	= 0;
		deepestFloor	= 0;
		enemiesSlain	= 0;
		foodEaten		= 0;
		potionsCooked	= 0;
		piranhasKilled	= 0;
		ankhsUsed		= 0;
		
		upgradesUsed    		= 0;
		sneakAttacks    		= 0;
		thrownAssists   		= 0;
		mirrorImageScrollUsed 	= 0;

		spawnersAlive   = 0;
		
		duration	= 0;
		
		qualifiedForNoKilling = false;
		
		amuletObtained = false;

		//Mobs killed
		mobsNumTypeKilled = 0;
		mobList = new ArrayList<Mob>();

		mobsKilledTier = new int[]{

				//SEWERS
				0, //Marsupial Rat
				0, //Albino Rat
				0, //Snake
				0, //Gnoll Scout
				0, //Crab
				0, //Swarm

				//POLLUTED SEWERS
				0, //

		};

	}
	
	private static final String GOLD		= "score";
	private static final String DEEPEST		= "maxDepth";
	private static final String SLAIN		= "enemiesSlain";
	private static final String FOOD		= "foodEaten";
	private static final String ALCHEMY		= "potionsCooked";
	private static final String PIRANHAS	= "priranhas";
	private static final String ANKHS		= "ankhsUsed";
	
	private static final String UPGRADES	= "upgradesUsed";
	private static final String SNEAKS		= "sneakAttacks";
	private static final String THROWN		= "thrownAssists";
	private static final String SUMMON		= "mirrorImageScrollUsed";

	private static final String SPAWNERS	= "spawnersAlive";
	
	private static final String DURATION	= "duration";
	
	private static final String AMULET		= "amuletObtained";


	private static final String MOBS_NUM_TYPE_KILLED	= "mobsNumTypeKilled";
	private static final String MOBS_KILLED_LIST		= "mobsKilledList";
	private static final String MOBS_KILLED_TIER		= "mobsKilledTier";




	public static void storeInBundle( Bundle bundle ) {
		bundle.put( GOLD,		goldCollected );
		bundle.put( DEEPEST,	deepestFloor );
		bundle.put( SLAIN,		enemiesSlain );
		bundle.put( FOOD,		foodEaten );
		bundle.put( ALCHEMY,	potionsCooked );
		bundle.put( PIRANHAS,	piranhasKilled );
		bundle.put( ANKHS,		ankhsUsed );
		
		bundle.put( UPGRADES,   upgradesUsed );
		bundle.put( SNEAKS,		sneakAttacks );
		bundle.put( THROWN,		thrownAssists );
		bundle.put( SUMMON,		mirrorImageScrollUsed );

		bundle.put( SPAWNERS,	spawnersAlive );
		
		bundle.put( DURATION,	duration );
		
		bundle.put( AMULET,		amuletObtained );

		bundle.put(MOBS_NUM_TYPE_KILLED, (int) mobList.size());

		bundle.put( MOBS_KILLED_LIST, mobList);
		bundle.put( MOBS_KILLED_TIER, mobsKilledTier);

//		bundle.put( MARSUPIALRATS, marsupialRatsKilled);
//		bundle.put( MARSUPIALRATSTIER, marsupialRatsKilledTier);
	}
	
	public static void restoreFromBundle( Bundle bundle ) {
		goldCollected	= bundle.getInt( GOLD );
		deepestFloor	= bundle.getInt( DEEPEST );
		enemiesSlain	= bundle.getInt( SLAIN );
		foodEaten		= bundle.getInt( FOOD );
		potionsCooked	= bundle.getInt( ALCHEMY );
		piranhasKilled	= bundle.getInt( PIRANHAS );
		ankhsUsed		= bundle.getInt( ANKHS );
		
		upgradesUsed    		= bundle.getInt( UPGRADES );
		sneakAttacks    		= bundle.getInt( SNEAKS );
		thrownAssists   		= bundle.getInt( THROWN );
		mirrorImageScrollUsed 	= bundle.getInt( SUMMON );

		spawnersAlive   = bundle.getInt( SPAWNERS );
		
		duration		= bundle.getFloat( DURATION );
		
		amuletObtained	= bundle.getBoolean( AMULET );

		mobsNumTypeKilled = bundle.getInt( MOBS_NUM_TYPE_KILLED );


		mobList = new ArrayList<>();
		for (Bundlable mob : bundle.getCollection( MOBS_KILLED_LIST )) {
			mobList.add((Mob) mob);
		}

		mobsKilledTier = bundle.getIntArray( MOBS_KILLED_TIER );

		validateMonstersSlain();
	}
	
	public static void preview( GamesInProgress.Info info, Bundle bundle ){
		info.goldCollected  = bundle.getInt( GOLD );
		info.maxDepth       = bundle.getInt( DEEPEST );
	}

	public static void validateMonstersSlain() {

		int[] tierNum = {1,2,3,4,5};
		int[] killedNum = {3,6,9,12,15};

		int[] BossKilledNum = {1,2,3,4,5};

		int[] RareKilledNum = {2,4,6,8,10};

		int ratsKilled = countMobsKilled(new Rat().name());
		int ratsKilledTier = mobsKilledTier[0];

		if(ratsKilled >= killedNum[0] && ratsKilledTier < tierNum[0]){
			mobsKilledTier[0] += 1;
			Dungeon.hero.additionalLvl += 1;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "marsupialrat1"));
		}
		else if(ratsKilled >= killedNum[1] && ratsKilledTier < tierNum[1]){
			mobsKilledTier[0] += 1;
			Dungeon.hero.additionalLvl += 1;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "marsupialrat2"));
		}
		else if(ratsKilled >= killedNum[2] && ratsKilledTier < tierNum[2]){
			mobsKilledTier[0] += 1;
			Dungeon.hero.additionalLvl += 1;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "marsupialrat3"));
		}
		else if(ratsKilled >= killedNum[3] && ratsKilledTier < tierNum[3]){
			mobsKilledTier[0] += 1;
			Dungeon.hero.additionalLvl += 1;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "marsupialrat4"));
		}
		else if(ratsKilled >= killedNum[4] && ratsKilledTier < tierNum[4]){
			mobsKilledTier[0] += 1;
			Dungeon.hero.additionalLvl += 1;
			Dungeon.hero.additionalDefenseSkill += 1;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "marsupialrat5"));
		}

		int AlbinoRatsKilled = countMobsKilled(new Albino().name());
		int AlbinoatsKilledTier = mobsKilledTier[1];

		if(AlbinoRatsKilled >= RareKilledNum[0] && AlbinoatsKilledTier < tierNum[0]){
			mobsKilledTier[1] += 1;
			Dungeon.hero.additionalLvl += 1;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "albinorat1"));
		}
		else if(AlbinoRatsKilled >= RareKilledNum[1] && AlbinoatsKilledTier < tierNum[1]){
			mobsKilledTier[1] += 1;
			Dungeon.hero.additionalLvl += 1;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "albinorat2"));
		}
		else if(AlbinoRatsKilled >= RareKilledNum[2] && AlbinoatsKilledTier < tierNum[2]){
			mobsKilledTier[1] += 1;
			Dungeon.hero.additionalLvl += 1;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "albinorat3"));
		}
		else if(AlbinoRatsKilled >= RareKilledNum[3] && AlbinoatsKilledTier < tierNum[3]){
			mobsKilledTier[1] += 1;
			Dungeon.hero.additionalLvl += 1;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "albinorat4"));
		}
		else if(AlbinoRatsKilled >= RareKilledNum[4] && AlbinoatsKilledTier < tierNum[4]){
			mobsKilledTier[1] += 1;
			Dungeon.hero.additionalLvl += 1;
			Dungeon.hero.additionalDefenseSkill += 1;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "albinorat5"));
		}

		int snakesKilled = countMobsKilled(new Snake().name());
		int snakesKilledTier = mobsKilledTier[2];

		if(snakesKilled >= killedNum[0] && snakesKilledTier < tierNum[0]){
			mobsKilledTier[2] += 1;
			Dungeon.hero.additionalLvl += 1;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "snake1"));
		}
		else if(snakesKilled >= killedNum[1] && snakesKilledTier < tierNum[1]){
			mobsKilledTier[2] += 1;
			Dungeon.hero.additionalLvl += 1;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "snake2"));
		}
		else if(snakesKilled >= killedNum[2] && snakesKilledTier < tierNum[2]){
			mobsKilledTier[2] += 1;
			Dungeon.hero.additionalLvl += 1;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "snake3"));
		}
		else if(snakesKilled >= killedNum[3] && snakesKilledTier < tierNum[3]){
			mobsKilledTier[2] += 1;
			Dungeon.hero.additionalLvl += 1;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "snake4"));
		}
		else if(snakesKilled >= killedNum[4] && snakesKilledTier < tierNum[4]){
			mobsKilledTier[2] += 1;
			Dungeon.hero.additionalLvl += 1;
			Dungeon.hero.additionalAttackSkill += 1;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "snake5"));
		}

		int gnollsKilled = countMobsKilled(new Gnoll().name());
		int gnollsKilledTier = mobsKilledTier[3];

		if(gnollsKilled >= killedNum[0] && gnollsKilledTier < tierNum[0]){
			mobsKilledTier[3] += 1;
			Dungeon.hero.additionalLvl += 1;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "gnoll1"));
		}
		else if(gnollsKilled >= killedNum[1] && gnollsKilledTier < tierNum[1]){
			mobsKilledTier[3] += 1;
			Dungeon.hero.additionalLvl += 1;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "gnoll2"));
		}
		else if(gnollsKilled >= killedNum[2] && gnollsKilledTier < tierNum[2]){
			mobsKilledTier[3] += 1;
			Dungeon.hero.additionalLvl += 1;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "gnoll3"));
		}
		else if(gnollsKilled >= killedNum[3] && gnollsKilledTier < tierNum[3]){
			mobsKilledTier[3] += 1;
			Dungeon.hero.additionalLvl += 1;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "gnoll4"));
		}
		else if(gnollsKilled >= killedNum[4] && gnollsKilledTier < tierNum[4]){
			mobsKilledTier[3] += 1;
			Dungeon.hero.additionalLvl += 1;
			Dungeon.hero.additionalAttackSkill += 2;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "gnoll5"));
		}

		int crabsKilled = countMobsKilled(new Crab().name());
		int crabsKilledTier = mobsKilledTier[4];

		if(crabsKilled >= killedNum[0] && crabsKilledTier < tierNum[0]){
			mobsKilledTier[4] += 1;
			Dungeon.hero.additionalLvl += 1;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "crab1"));
		}
		else if(crabsKilled >= killedNum[1] && crabsKilledTier < tierNum[1]){
			mobsKilledTier[4] += 1;
			Dungeon.hero.additionalLvl += 1;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "crab2"));
		}
		else if(crabsKilled >= killedNum[2] && crabsKilledTier < tierNum[2]){
			mobsKilledTier[4] += 1;
			Dungeon.hero.additionalLvl += 1;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "crab3"));
		}
		else if(crabsKilled >= killedNum[3] && crabsKilledTier < tierNum[3]){
			mobsKilledTier[4] += 1;
			Dungeon.hero.additionalLvl += 1;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "crab4"));
		}
		else if(crabsKilled >= killedNum[4] && crabsKilledTier < tierNum[4]){
			mobsKilledTier[4] += 1;
			Dungeon.hero.additionalLvl += 1;
			Dungeon.hero.additionalDefenseSkill += 2;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "crab5"));
		}

		int swarmsKilled = countMobsKilled(new Swarm().name());
		int swarmsKilledTier = mobsKilledTier[5];

		if(swarmsKilled >= killedNum[0] && swarmsKilledTier < tierNum[0]){
			mobsKilledTier[5] += 1;
			Dungeon.hero.additionalLvl += 1;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "swarm1"));
		}
		else if(swarmsKilled >= killedNum[1] && swarmsKilledTier < tierNum[1]){
			mobsKilledTier[5] += 1;
			Dungeon.hero.additionalLvl += 1;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "swarm2"));
		}
		else if(swarmsKilled >= killedNum[2] && swarmsKilledTier < tierNum[2]){
			mobsKilledTier[5] += 1;
			Dungeon.hero.additionalLvl += 1;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "swarm3"));
		}
		else if(swarmsKilled >= killedNum[3] && swarmsKilledTier < tierNum[3]){
			mobsKilledTier[5] += 1;
			Dungeon.hero.additionalLvl += 1;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "swarm4"));
		}
		else if(swarmsKilled >= killedNum[4] && swarmsKilledTier < tierNum[4]){
			mobsKilledTier[5] += 1;
			Dungeon.hero.additionalLvl += 1;
			Dungeon.hero.HTBoost += 5;
			GLog.newLine();
			GLog.p( Messages.get(Statistics.class, "swarm5"));
		}
	}

	public static int countMobsKilled(String mobName){
		int count = 0;

		for(Mob mob: mobList){
			if(mob.name() == mobName){
				count++;
			}
		}

		return count;
	}

	public static int getMobKilledTier(String mobName){
		int tier;

		if (countMobsKilled(mobName)/3 > 5){
			tier = 5;
		}
		else {
			tier = countMobsKilled(mobName)/3;
		}

		return tier;
	}

	public static int getRareMobKilledTier(String mobName){
		int tier;

		if (countMobsKilled(mobName)/2 > 5){
			tier = 5;
		}
		else {
			tier = countMobsKilled(mobName)/2;
		}

		return tier;
	}


}
