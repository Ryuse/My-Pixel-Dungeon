package com.shatteredpixel.shatteredpixeldungeon.items.helmets

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet

class MaskOfLider : Helmet() {
    init {
        image = ItemSpriteSheet.RIDER_MASK
    }

    override fun desc(): String {
        var desc = super.desc()
        if (isIdentified) desc += "\n\n" + Messages.get(this, "effect-desc")

        return desc
    }

    override fun viewAmend(): Int = 1
}