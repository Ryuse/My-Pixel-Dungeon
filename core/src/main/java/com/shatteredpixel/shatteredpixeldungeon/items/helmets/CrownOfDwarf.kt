package com.shatteredpixel.shatteredpixeldungeon.items.helmets

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet

/**
 * Created by 93942 on 9/24/2018.
 */

class CrownOfDwarf : Helmet() {
    init {
        image = ItemSpriteSheet.DWARF_CROWN
    }

    override fun isIdentified(): Boolean {
        return true
    }

    override fun value() = 500
}
