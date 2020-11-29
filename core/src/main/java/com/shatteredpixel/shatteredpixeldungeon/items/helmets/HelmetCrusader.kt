package com.shatteredpixel.shatteredpixeldungeon.items.helmets

import com.shatteredpixel.shatteredpixeldungeon.actors.Damage
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet
import com.shatteredpixel.shatteredpixeldungeon.actors.Char
import com.watabou.utils.Random

class HelmetCrusader : Helmet() {
    init {
        image = ItemSpriteSheet.HELMET_CRUSADER
    }

    override fun desc(): String {
        var desc = super.desc()
        if (isIdentified) {
            desc += "\n\n" + Messages.get(this, "effect_desc")
            if (cursed)
                desc += "\n\n" + Messages.get(Helmet::class.java, "cursed_desc")
        }

        return desc
    }

    override fun procTakenDamage(dmg: Damage) {
        if (dmg.isFeatured(Damage.Feature.RANGED)) 
            dmg.value = if (Random.Float() < 0.1f) 0 else dmg.value * 4 / 5
        
    }

    override fun viewAmend(): Int = -1
}