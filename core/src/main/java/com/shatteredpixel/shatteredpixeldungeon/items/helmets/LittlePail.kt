package com.shatteredpixel.shatteredpixeldungeon.items.helmets

import com.shatteredpixel.shatteredpixeldungeon.actors.Damage
import com.shatteredpixel.shatteredpixeldungeon.items.Item
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet
import com.watabou.utils.Random

class LittlePail : Helmet() {
    init {
        image = ItemSpriteSheet.LITTLE_PAIL
    }

    override fun procTakenDamage(dmg: Damage) {
        dmg.value -= Random.NormalIntRange(0, 2)
    }

    override fun random(): Item = this.apply { cursed = Random.Float() < 0.1f }

    override fun desc(): String {
        var desc = super.desc()
        if (isIdentified) {
            desc += "\n\n" + Messages.get(this, "effect-desc")
            if (cursed)
                desc += "\n\n" + Messages.get(Helmet::class.java, "cursed_desc")
        }

        return desc
    }
}