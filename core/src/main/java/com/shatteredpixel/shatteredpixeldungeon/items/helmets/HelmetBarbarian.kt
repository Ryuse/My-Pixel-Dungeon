package com.shatteredpixel.shatteredpixeldungeon.items.helmets

import com.shatteredpixel.shatteredpixeldungeon.actors.Char
import com.shatteredpixel.shatteredpixeldungeon.actors.Damage
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet
import com.watabou.utils.Random

class HelmetBarbarian : Helmet() {
    init {
        image = ItemSpriteSheet.HELMET_BARBARIAN
    }

    override fun desc(): String {
        var desc = super.desc()
        if (isIdentified) {
            desc += "\n\n" + Messages.get(this, "effect-desc")
            if (cursed)
                desc += "\n\n" + Messages.get(Helmet::class.java, "cursed_desc")
        }

        return desc
    }

    override fun procGivenDamage(dmg: Damage) {
        if (dmg.from != null && dmg.from is Char && (dmg.from as Char).HP < (dmg.from as Char).HT / 2)
            dmg.value = dmg.value * 5 / 4
    }
}