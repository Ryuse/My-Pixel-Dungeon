package com.shatteredpixel.shatteredpixeldungeon.items.helmets

import com.shatteredpixel.shatteredpixeldungeon.actors.Damage
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet
import kotlin.math.max

class RangerHat : Helmet() {
    init {
        image = ItemSpriteSheet.HELMET_RANGER
    }

    override fun desc(): String {
        var desc = super.desc()
        if (isIdentified) {
            desc += "\n\n" + Messages.get(this, "effect-desc")
            if (cursed)
                desc += "\n\n" + Messages.get(this, "cursed-desc")
        }

        return desc
    }

    override fun procGivenDamage(dmg: Damage) {
        if (dmg.isFeatured(Damage.Feature.RANGED)) {
            val v = max(dmg.value / 5, 2)
            dmg.value += if (cursed) -v else v
        }
    }

}