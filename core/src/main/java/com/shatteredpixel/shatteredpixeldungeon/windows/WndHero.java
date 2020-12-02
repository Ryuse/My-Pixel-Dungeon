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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Acidic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Albino;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ArmoredBrute;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bandit;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM201;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.FetidRat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.PollutedRat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Senior;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.EyeOfWisdom;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.StatusPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentsPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

public class WndHero extends WndTabbed {
	
	private static final int WIDTH		= 120;
	private static int HEIGHT			= 150;



	private static final int ITEM_HEIGHT	= 18;

	private StatsTab stats;
	private KilledTab killed;
	private TalentsTab talents;
	private BuffsTab buffs;

	public static int lastIdx = 0;

	public WndHero() {

		super();
		if (Dungeon.hero.belongings.eyeEquipped()) {
			HEIGHT = 150;
		}
		else{
			HEIGHT = 110;
		}
		resize( WIDTH, HEIGHT );
		
		stats = new StatsTab();
		add( stats );

		killed = new KilledTab();
		add (killed);
		killed.setRect(0, 0, WIDTH, HEIGHT);
		killed.setupList();

		talents = new TalentsTab();
		add(talents);
		talents.setRect(0, 0, WIDTH, HEIGHT);

		buffs = new BuffsTab();
		add( buffs );
		buffs.setRect(0, 0, WIDTH, HEIGHT);
		buffs.setupList();
		
		add( new LabeledTab( Messages.get(this, "stats") ) {
			protected void select( boolean value ) {
				super.select( value );
				if (selected) lastIdx = 0;
				stats.visible = stats.active = selected;
			}
		} );
		add( new LabeledTab( Messages.get(this, "killed") ) {
			protected void select( boolean value ) {
				super.select( value );
				if (selected) lastIdx = 1;
				killed.visible = killed.active = selected;
			}
		} );
		add( new LabeledTab( Messages.get(this, "talents") ) {
			protected void select( boolean value ) {
				super.select( value );
				if (selected) lastIdx = 2;
				if (selected) StatusPane.talentBlink = 0;
				talents.visible = talents.active = selected;
			}
		} );
		add( new LabeledTab( Messages.get(this, "buffs") ) {
			protected void select( boolean value ) {
				super.select( value );
				if (selected) lastIdx = 3;
				buffs.visible = buffs.active = selected;
			}
		} );

		layoutTabs();
		
		select( lastIdx );
	}

	private class StatsTab extends Group {
		
		private static final int GAP = 6;
		
		private float pos;
		
		public StatsTab() {
			
			Hero hero = Dungeon.hero;

			IconTitle title = new IconTitle();
			title.icon( HeroSprite.avatar(hero.heroClass, hero.tier()) );
			if (hero.name().equals(hero.className()))
				title.label( Messages.get(this, "title", hero.lvl, hero.className() ).toUpperCase( Locale.ENGLISH ) );
			else
				title.label((hero.name() + "\n" + Messages.get(this, "title", hero.lvl, hero.className())).toUpperCase(Locale.ENGLISH));
			title.color(Window.TITLE_COLOR);
			title.setRect( 0, 0, WIDTH, 0 );
			add(title);

			pos = title.bottom() + GAP;

			String heroHP = withSuffix(hero.HP);
			String heroHTBoost = withSuffix(hero.HTBoost);
			String heroHTNoBoost = withSuffix(hero.HT-hero.HTBoost);
			String heroHT = withSuffix(hero.HT);
			if(Dungeon.hero.belongings.eyeEquipped()){
				if (hero.shielding() > 0) statSlot( Messages.get(this, "health"),  heroHP + "+" + hero.shielding() + "/" + heroHTNoBoost + " (+" + heroHTBoost + ")" );
				else statSlot( Messages.get(this, "health"), heroHP + "/" + heroHTNoBoost + " (+" + heroHTBoost + ")" );
			}
			else{
				if (hero.shielding() > 0) statSlot( Messages.get(this, "health"), heroHP + "+" + hero.shielding() + "/" + heroHTNoBoost);
				else statSlot( Messages.get(this, "health"), heroHP + "/" + heroHT);
			}

			String heroSTR = withSuffix(hero.STR());
			statSlot( Messages.get(this, "str"), heroSTR );
			float healthPercentage = (float) hero.HP/hero.HT;

			if(Dungeon.hero.belongings.eyeEquipped()){
				int atkSkill = (int) (hero.attackSkill*healthPercentage);
				int addAtkSkill = (int) (hero.additionalAttackSkill*healthPercentage);

				if(hero.additionalAttackSkill > 0){
					statSlot( Messages.get(this, "attack_skill"), (atkSkill) + " (+" + withSuffix(addAtkSkill) + ")" );
				}
				else{
					statSlot( Messages.get(this, "attack_skill"), atkSkill);
				}

				int defSkill = (int) (hero.defenseSkill*healthPercentage);
				int addDefSkill = (int) (hero.additionalDefenseSkill*healthPercentage);

				if(hero.additionalDefenseSkill > 0){
					statSlot( Messages.get(this, "defense_skill"), (defSkill) + " (+" + withSuffix(addDefSkill) + ")" );
				}
				else{
					statSlot( Messages.get(this, "defense_skill"), defSkill );
				}

				pos += GAP;

				statSlot( Messages.get(this, "hunger"), (int) Hunger.level + "/" + (int) Hunger.STARVING );

				pos += GAP;
				statSlot( Messages.get(this, "level"), hero.lvl + "/" + (hero.MAX_LEVEL+hero.additionalLvl));
				statSlot( Messages.get(this, "exp"), hero.exp + "/" + hero.maxExp() );
			}
			else{

//				statSlot( Messages.get(this, "attack_skill"), "???" );
//				statSlot( Messages.get(this, "defense_skill"), "???" );
//				pos += GAP;
//				statSlot( Messages.get(this, "hunger"), "???" );
				pos += GAP;
				statSlot( Messages.get(this, "level"), hero.lvl);
				statSlot( Messages.get(this, "exp"), "???" );
			}

			pos += GAP;

			statSlot( Messages.get(this, "gold"), withSuffix(Statistics.goldCollected) );
			statSlot( Messages.get(this, "depth"), Statistics.deepestFloor );

			pos += GAP;
		}

		private void statSlot( String label, String value ) {
			
			RenderedTextBlock txt = PixelScene.renderTextBlock( label, 8 );
			txt.setPos(0, pos);
			add( txt );
			
			txt = PixelScene.renderTextBlock( value, 8 );
			txt.setPos(WIDTH * 0.6f, pos);
			PixelScene.align(txt);
			add( txt );
			
			pos += GAP + txt.height();
		}
		
		private void statSlot( String label, int value ) {
			statSlot( label, Integer.toString( value ) );
		}
		
		public float height() {
			return pos;
		}
	}

	private class KilledTab extends Component{

		private static final int GAP = 2;
		private SmartTexture icons;
		private TextureFilm film;


		private float pos;
		private ScrollPane mobList;
		private ArrayList<mobSlot> slots = new ArrayList<>();

		@Override
		protected void createChildren() {
			super.createChildren();



			mobList = new ScrollPane( new Component() ){
				@Override
				public void onClick( float x, float y ) {
					int size = slots.size();
					for (int i=0; i < size; i++) {
						if (slots.get( i ).onClick( x, y )) {
							break;
						}
					}
				}
			};
			add(mobList);
		}

		@Override
		protected void layout() {
			super.layout();
			RenderedTextBlock title;
			title = PixelScene.renderTextBlock( "Monsters Killed", 9 );
			title.hardlight(TITLE_COLOR);
			title.maxWidth( WIDTH - 2 );
			title.setPos( (WIDTH - title.width())/2f, pos + GAP);
			PixelScene.align(title);
			add(title);

			ColorBlock line = new ColorBlock( WIDTH, 1, 0xFF222222);
			line.y = title.bottom() + 2*GAP;
			add(line);

			float titleHeight = title.height() + line.height + GAP + 2*GAP;

			mobList.setRect(0, line.y, width, height-titleHeight);
		}

		private void setupList() {
			Component content = mobList.content();
			ArrayList<Mob> mobDuplicateCheck = new ArrayList<>();



			for (Mob mob : Statistics.mobList) {
				boolean recorded = false;
				for (Mob mobCheck: mobDuplicateCheck){
					if(mobCheck.getClass() == mob.getClass()){
						recorded = true;
						break;
					}
				}


				if(!recorded && !(mob.alignment == Char.Alignment.ALLY) && !(mob instanceof Ghost)){
					mobSlot slot = new mobSlot(mob);
					slot.setRect(0, pos, WIDTH, slot.height + GAP);
					content.add(slot);
					slots.add(slot);
					pos += 2*GAP + slot.height();
					mobDuplicateCheck.add(mob);
				}

			}
			content.setSize(mobList.width(), pos);
			mobList.setSize(mobList.width(), mobList.height());


		}

		private class mobSlot extends Component {

			ColorBlock line;

			private CharSprite image;
			RenderedTextBlock txt;
			RenderedTextBlock killedTxt;
			RenderedTextBlock tierTxt;
			RenderedTextBlock buffTxt;

			ColorBlock bottomLine;

			float height = 0;

			public mobSlot( Mob mob ){
				super();


				line = new ColorBlock( WIDTH, 1, 0xFF222222);

				height += line.height;

				image = mob.sprite();
				add( image );
				image.y = this.y + GAP;
				image.height = 16;
				image.width = 16;


				txt = PixelScene.renderTextBlock( Messages.titleCase( mob.name() ), 9 );
				add( txt );
				height += txt.height() + GAP;


				int killed = Statistics.countMobsKilled(mob.name());

				if(Dungeon.hero.belongings.eyeEquipped()){
					int tier = Statistics.getMobKilledTier(mob.name());
					if (mob instanceof Albino
							|| mob instanceof Bandit
							|| mob instanceof ArmoredBrute
							|| mob instanceof DM201
							|| mob instanceof Senior
							|| mob instanceof Acidic
					){
						tier = Statistics.getRareMobKilledTier(mob.name());
					}

					tierTxt = PixelScene.renderTextBlock("Tier: " + tier + "/5", 6);
					add( tierTxt );
					height += tierTxt.height();

					buffTxt = PixelScene.renderTextBlock("Buffs: " + mob.ACHIEVEMENT_BUFF[tier], 6);
					add( buffTxt );
					height += buffTxt.height() + 2* GAP;
				}



				killedTxt = PixelScene.renderTextBlock("Killed: " + killed, 6);
				add( killedTxt );
				height += killedTxt.height();



				bottomLine = new ColorBlock( WIDTH, 1, 0xFF222222);
				add( bottomLine );
				height += bottomLine.height + 2*GAP;
			}

			@Override
			protected void layout() {
				super.layout();

				line.y = this.y;

				image.y = this.y + GAP + line.height;

				txt.setPos(
						image.width + GAP,
						image.y + (image.height - txt.height()) / 2);
				killedTxt.setPos(
						image.width + GAP,
						txt.bottom() + ((image.height - killedTxt.height()) / 2));

				if(Dungeon.hero.belongings.eyeEquipped()){
					tierTxt.setPos(
							image.width + killedTxt.width() + 4*GAP,
							txt.bottom() + ((image.height - tierTxt.height()) / 2));
					buffTxt.setPos(
							image.width + GAP,
							tierTxt.bottom() + (image.height - buffTxt.height()) / 2
					);

					bottomLine.y = buffTxt.bottom() + 2 * GAP;
				}
				else{
					bottomLine.y = killedTxt.bottom() + 2 * GAP;
				}


			}

			protected boolean onClick ( float x, float y ) {
				if (inside( x, y )) {
//					GameScene.show(new WndInfoBuff(buff));
					return true;
				} else {
					return false;
				}
			}
		}
	}

	public class TalentsTab extends Component {

		TalentsPane pane;

		@Override
		protected void createChildren() {
			super.createChildren();
			pane = new TalentsPane(true);
			add(pane);
		}

		@Override
		protected void layout() {
			super.layout();
			pane.setRect(x, y, width, height);
		}

	}
	
	private class BuffsTab extends Component {
		
		private static final int GAP = 2;

		private SmartTexture icons;
		private TextureFilm film;
		
		private float pos;
		private ScrollPane buffList;
		private ArrayList<BuffSlot> slots = new ArrayList<>();

		@Override
		protected void createChildren() {
			icons = TextureCache.get( Assets.Interfaces.BUFFS_LARGE );
			film = new TextureFilm( icons, 16, 16 );

			super.createChildren();

			buffList = new ScrollPane( new Component() ){
				@Override
				public void onClick( float x, float y ) {
					int size = slots.size();
					for (int i=0; i < size; i++) {
						if (slots.get( i ).onClick( x, y )) {
							break;
						}
					}
				}
			};
			add(buffList);
		}
		
		@Override
		protected void layout() {
			super.layout();
			buffList.setRect(0, 0, width, height);
		}
		
		private void setupList() {
			Component content = buffList.content();
			for (Buff buff : Dungeon.hero.buffs()) {
				if (buff.icon() != BuffIndicator.NONE) {
					BuffSlot slot = new BuffSlot(buff);
					slot.setRect(0, pos, WIDTH, slot.icon.height());
					content.add(slot);
					slots.add(slot);
					pos += GAP + slot.height();
				}
			}
			content.setSize(buffList.width(), pos);
			buffList.setSize(buffList.width(), buffList.height());
		}

		private class BuffSlot extends Component {

			private Buff buff;

			Image icon;
			RenderedTextBlock txt;

			public BuffSlot( Buff buff ){
				super();
				this.buff = buff;
				int index = buff.icon();

				icon = new Image( icons );
				icon.frame( film.get( index ) );
				buff.tintIcon(icon);
				icon.y = this.y;
				add( icon );

				txt = PixelScene.renderTextBlock( buff.toString(), 8 );
				txt.setPos(
						icon.width + GAP,
						this.y + (icon.height - txt.height()) / 2
				);
				PixelScene.align(txt);
				add( txt );

			}

			@Override
			protected void layout() {
				super.layout();
				icon.y = this.y;
				txt.setPos(
						icon.width + GAP,
						this.y + (icon.height - txt.height()) / 2
				);
			}
			
			protected boolean onClick ( float x, float y ) {
				if (inside( x, y )) {
					GameScene.show(new WndInfoBuff(buff));
					return true;
				} else {
					return false;
				}
			}
		}
	}

	public static String withSuffix(long count) {
		if (count < 1000) return "" + count;
		int exp = (int) (Math.log(count) / Math.log(1000));
		return String.format("%.1f%c",
				count / Math.pow(1000, exp),
				"KMGTPE".charAt(exp-1));
	}
}
