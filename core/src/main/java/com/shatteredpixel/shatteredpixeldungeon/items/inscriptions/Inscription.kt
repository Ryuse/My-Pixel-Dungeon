package com.shatteredpixel.shatteredpixeldungeon.items.inscriptions

import com.shatteredpixel.shatteredpixeldungeon.Dungeon
import com.shatteredpixel.shatteredpixeldungeon.actors.Char
import com.shatteredpixel.shatteredpixeldungeon.actors.Damage
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem
import com.shatteredpixel.shatteredpixeldungeon.items.Item
import com.shatteredpixel.shatteredpixeldungeon.items.inscriptions.good.AntiMagic
import com.shatteredpixel.shatteredpixeldungeon.items.inscriptions.good.Healthy
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog
import com.watabou.utils.Bundlable
import com.watabou.utils.Bundle
import com.watabou.utils.Random

abstract class Inscription : Bundlable {

    // affection
    open fun procTakenDamage(equipment: EquipableItem, dmg: Damage) {}
    open fun procGivenDamage(dmg: Damage) {}
    open fun mhpFix(mhp: Int): Int = mhp

    abstract fun glowing(): ItemSprite.Glowing

    fun name(equipmentName: String): String = Messages.get(this, "name", equipmentName)

    fun name(): String = name(
            if (curse()) Messages.get(Item::class.java, "curse")
            else Messages.get(this, "inscription"))

    fun desc(): String = Messages.get(this, "desc")

    fun curse() = false

    fun checkIfKilledOwner(owner: Char): Boolean {
        return if (!owner.isAlive && owner is Hero) {
            Dungeon.fail(javaClass)
            GLog.n(Messages.get(this, "killed", name()))

            // Badges.validateDeathFromGlyph()
            true
        } else
            false
    }

    // todo: random

    override fun storeInBundle(bundle: Bundle) {}
    override fun restoreFromBundle(bundle: Bundle) {}

    companion object {
        fun RandomInscription(): Inscription = Random.chances(Inscriptions).newInstance()

        fun RandomCurse(): Inscription = Random.chances(Curses).newInstance()

        private val Inscriptions: HashMap<Class<out Inscription>, Float> = hashMapOf(
                AntiMagic::class.java to 1f, 
                Healthy::class.java to 1f
        )

        private val Curses: HashMap<Class<out Inscription>, Float> = hashMapOf()
    }
}

