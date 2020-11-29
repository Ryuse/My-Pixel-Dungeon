package com.shatteredpixel.shatteredpixeldungeon.items.helmets

import com.shatteredpixel.shatteredpixeldungeon.actors.Char
import com.shatteredpixel.shatteredpixeldungeon.actors.Damage
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog
import com.watabou.utils.Bundle

class MaskOfMadness : Helmet() {
    init {
        image = ItemSpriteSheet.MASK_OF_MADNESS

        unique = true
    }

    private var exp = 0

    override fun doUnequip(hero: Hero, collect: Boolean, single: Boolean): Boolean {
        GLog.n(Messages.get(this, "cannot_unequip"))
        return false
    }

    override fun procGivenDamage(dmg: Damage) {
        val ratio = 2.1f - 1.5f / (Math.exp((level() / 3f).toDouble()).toFloat() + .5f)
        dmg.value = (dmg.value * ratio).toInt()
    }

    override fun procTakenDamage(dmg: Damage) {
        val ratio = if (dmg.type == Damage.Type.MENTAL) 1.75f
        else 1.8f - 1.5f / (Math.exp((level() / 3f).toDouble()).toFloat() + 1f) + 0.05f * level()

        dmg.value = (dmg.value * ratio).toInt()
    }

    fun onEnemySlayed(ch: Char) {
        exp += if (ch.properties().contains(Char.Property.BOSS)) 3 else 1

        if (exp >= level() * 2 && level() < 10) {
            exp -= level() * 2

            upgrade()
            GLog.p(Messages.get(this, "levelup"))
        }
    }

    override fun value(): Int = 0

    override fun storeInBundle(bundle: Bundle) {
        super.storeInBundle(bundle)
        bundle.put(EXP, exp)
    }

    override fun restoreFromBundle(bundle: Bundle) {
        super.restoreFromBundle(bundle)
        exp = bundle.getInt(EXP)
    }

    companion object {
        private const val EXP = "exp"
    }
}