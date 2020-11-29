package com.shatteredpixel.shatteredpixeldungeon.items.helmets

import com.shatteredpixel.shatteredpixeldungeon.actors.Damage
import com.shatteredpixel.shatteredpixeldungeon.items.inscriptions.good.AntiMagic
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet
import com.watabou.utils.Random

class MaskOfClown : Helmet() {
    init {
        image = ItemSpriteSheet.HELMET_CLOWN
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
        if (!cursed && Random.Float() < 0.1f)
            dmg.addFeature(Damage.Feature.PURE)
    }
}