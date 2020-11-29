package com.shatteredpixel.shatteredpixeldungeon.items.helmets

import com.shatteredpixel.shatteredpixeldungeon.items.Item
import com.shatteredpixel.shatteredpixeldungeon.messages.M
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet

class StrawHat : Helmet() {
    init {
        image = ItemSpriteSheet.STRAW_HAT
    }

    override fun desc(): String = super.desc() + "\n\n" + M.L(this, "effect-desc")

    override fun isIdentified(): Boolean {
        return true
    }

    override fun random(): Item {
        cursed = false
        return this
    }

    override fun value(): Int = 0
}