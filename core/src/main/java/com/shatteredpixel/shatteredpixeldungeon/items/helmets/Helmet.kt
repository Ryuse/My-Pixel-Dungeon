package com.shatteredpixel.shatteredpixeldungeon.items.helmets

import com.shatteredpixel.shatteredpixeldungeon.actors.Damage
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem
import com.shatteredpixel.shatteredpixeldungeon.items.Item
import com.shatteredpixel.shatteredpixeldungeon.items.inscriptions.Inscription
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog
import com.watabou.utils.Bundle
import com.watabou.utils.Random


open class Helmet(private var ticksToKnow: Int = TICKS_TO_KNOW) : EquipableItem() {

    private var inscription: Inscription? = null

    override fun isUpgradable(): Boolean {
        return false
    }

    //todo:
    open fun uncurse() {}

    override fun doEquip(hero: Hero): Boolean {
        var hero_helmets = hero.belongings.helmets_list
        var helmet_slots_full = true
        for (miscs in hero_helmets){
            if(miscs == null){
                helmet_slots_full = false
            }
        }
        if (helmet_slots_full) {
            return false
        }
        else{
            for (x in 0 until hero_helmets.size){
                if(hero_helmets[x] == null){
                    hero_helmets[x] = this
                    break
                }
            }


            detach(hero.belongings.backpack)

            identify()
            if (cursed) {
                equipCursed(hero)
                GLog.n(Messages.get(Helmet::class.java, "equip_cursed"))
            }
            activate(hero)
            hero.spendAndNext(time2equip(hero))
            return true
        }
    }

    override fun time2equip(hero: Hero): Float = 2f / hero.speed()

    // when equip
//    override fun activate(ch: Char) 

    override fun doUnequip(hero: Hero, collect: Boolean, single: Boolean): Boolean {
        if (super.doUnequip(hero, collect, single)) {

            var hero_helmets = hero.belongings.helmets_list

            for (x in 0 until hero_helmets.size){
                if(hero_helmets[x] == this){
                    hero_helmets[x] = null
                }
            }

            return true
        }

        return false
    }
    override fun isEquipped(hero: Hero): Boolean {
        var bool = false
        var hero_helmets = hero.belongings.helmets_list
        for (x in 0 until hero_helmets.size){
            if(hero_helmets[x] === this){
                return true
            }
        }
        return bool
    }

    override fun random(): Item {
        cursed = Random.Float() < 0.1f

        return this
    }

    override fun value(): Int {
        var price = 75
        if (cursed && cursedKnown) {
            price /= 2
        }
        if (levelKnown) {
            if (level() > 0) {
                price *= level() + 1
            } else if (level() < 0) {
                price /= 1 - level()
            }
        }
        if (price < 1) {
            price = 1
        }
        return price
    }

    override fun name(): String = if (inscription != null && (cursedKnown || !inscription!!.curse()))
        inscription!!.name(super.name()) else super.name()

    override fun info(): String {
        var info = desc()
        val ins = inscription
        if (ins != null && (cursedKnown || !ins.curse())) {
            info += "\n\n" + Messages.get(Item::class.java, "inscribed", ins.name())
            info += "\n\n" + ins.desc()
        }

        return info
    }

    // this is what we call helmets!
    open fun procGivenDamage(dmg: Damage) {
        inscription?.procGivenDamage(dmg)
    }

    open fun procTakenDamage(dmg: Damage) {
        inscription?.procTakenDamage(this, dmg)
    }

    open fun viewAmend(): Int = 0

    fun inscribe(ins: Inscription?) {
        inscription = ins
    }

    // open fun inscribe(){}

    fun hasInscription(inscls: Class<out Inscription>): Boolean = inscription?.javaClass == inscls
    fun hasGoodInscription(): Boolean = inscription?.curse() == true
    fun hasCursedInscription(): Boolean = inscription?.curse() == false

    override fun glowing(): ItemSprite.Glowing? = if (cursedKnown || inscription?.curse() == true) inscription?.glowing() else null

    override fun storeInBundle(bundle: Bundle) {
        super.storeInBundle(bundle)
        bundle.put(UNFAMILIRIARITY, ticksToKnow)
    }

    override fun restoreFromBundle(bundle: Bundle) {
        super.restoreFromBundle(bundle)
        ticksToKnow = bundle.getInt(UNFAMILIRIARITY)
    }

    companion object {
        private const val TICKS_TO_KNOW = 150
        private const val UNFAMILIRIARITY = "unfamiliarity"
    }
}