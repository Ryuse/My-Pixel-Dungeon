package com.shatteredpixel.shatteredpixeldungeon.items.helmets

import com.shatteredpixel.shatteredpixeldungeon.Dungeon
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero
import com.shatteredpixel.shatteredpixeldungeon.messages.M
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet

class GuardHelmet : Helmet() {
    init {
        image = ItemSpriteSheet.HELMET_GUARD
    }

    override fun doEquip(hero: Hero): Boolean {
        return if (super.doEquip(hero)) {
            attach(hero)
            true
        } else false
    }

    override fun doUnequip(hero: Hero, collect: Boolean, single: Boolean): Boolean {
        return if (super.doUnequip(hero, collect, single)) {
            detach(hero)
            true
        } else false
    }

    override fun uncurse() {
        super.uncurse()
        if (!cursed) return

        // redo the modifications
        //fixme:
        detach(Dungeon.hero)
        cursed = false
        attach(Dungeon.hero)
    }

    private fun detach(hero: Hero) {
        val sign = if (cursed) -1f else 1f
//        hero.magicalResistance -= 0.09f * sign
//        for (i in 0 until hero.elementalResistance.size) hero.elementalResistance[i] -= 0.06f * sign
    }

    private fun attach(hero: Hero) {
        val sign = if (cursed) -1f else 1f
//        hero.magicalResistance += 0.09f * sign
//        for (i in 0 until hero.elementalResistance.size) hero.elementalResistance[i] += 0.06f * sign
    }

    override fun desc(): String {
        var desc = super.desc()
        if (isIdentified) {
            desc += "\n\n" + M.L(this, "effect-desc")
            if (cursed)
                desc += "\n\n" + M.L(this, "cursed-desc")
        }

        return desc
    }
}