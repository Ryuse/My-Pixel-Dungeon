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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.watabou.noosa.ui.Component;

public class WndInfoMob extends WndTitledMessage {
	
	public WndInfoMob( Mob mob ) {

		super( new MobTitle( mob ), mob.info() );
		
	}
	
	private static class MobTitle extends Component {

		private static final int GAP	= 2;
		
		private CharSprite image;
		private RenderedTextBlock name;
		private HealthBar health;
		private BuffIndicator buffs;

		private RenderedTextBlock mobHP;
		private RenderedTextBlock mobHT;

		private boolean isGhostHero;
		
		public MobTitle( Mob mob ) {
			
			name = PixelScene.renderTextBlock( Messages.titleCase( mob.name() ), 9 );

			name.hardlight( TITLE_COLOR );
			add( name );

			image = mob.sprite();
			add( image );

			if (Dungeon.hero.belongings.eyeEquipped()){
				health = new HealthBar();
				health.level(mob);
				add( health );

				buffs = new BuffIndicator( mob );
				add( buffs );

				mobHP = PixelScene.renderTextBlock( mob.HP + "/", 4 );
				add(mobHP);
				mobHT = PixelScene.renderTextBlock( Integer.toString(mob.HT), 4 );
				add(mobHT);
			}


			if(mob instanceof DriedRose.GhostHero && Dungeon.hero.heroClass == HeroClass.SUMMONER){
				isGhostHero = true;
			}

		}

		@Override
		protected void layout() {

			image.x = 0;
			image.y = Math.max( 0, name.height() - image.height() );
			if (Dungeon.hero.belongings.eyeEquipped()){
				image.y = Math.max( 0, name.height() + health.height() - image.height() );
			}

			name.setPos(x + image.width + GAP, image.height() > name.height() ? y +(image.height() - name.height()) / 2 : y);
			height = name.bottom() + GAP;

			if (Dungeon.hero.belongings.eyeEquipped()){
				float w = width - image.width() - GAP;
				health.setRect(image.width() + GAP, name.bottom() + GAP, w, health.height());
				buffs.setPos(
						name.right() + GAP-1,
						name.bottom() - BuffIndicator.SIZE-2
				);

//				if(isGhostHero){
				health.setRect(image.width() + GAP, name.bottom() + GAP, w, health.height()+1);
				mobHP.setPos(health.left() + 1, health.top());
				mobHT.setPos(mobHP.left() + mobHP.width(), health.top());
//				}

				height = health.bottom() + GAP;
			}




		}
	}
}
