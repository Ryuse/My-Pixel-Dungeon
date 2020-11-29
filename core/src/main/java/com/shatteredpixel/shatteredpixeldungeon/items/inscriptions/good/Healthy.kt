package com.shatteredpixel.shatteredpixeldungeon.items.inscriptions.good

import com.shatteredpixel.shatteredpixeldungeon.items.inscriptions.Inscription
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite


class Healthy : Inscription() {

    private var ratio = 0.05

    override fun mhpFix(mhp: Int): Int {
        return mhp + extra(mhp).toInt()
    }

    override fun glowing(): ItemSprite.Glowing = ItemSprite.Glowing(0xff0000) // red

    private fun extra(mhp: Int) = mhp * ratio
}
