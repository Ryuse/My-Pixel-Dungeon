package com.shatteredpixel.shatteredpixeldungeon.items.inscriptions.good

import com.shatteredpixel.shatteredpixeldungeon.actors.Damage
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem
import com.shatteredpixel.shatteredpixeldungeon.items.inscriptions.Inscription
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite

class AntiMagic : Inscription() {
    override fun procTakenDamage(equipment: EquipableItem, dmg: Damage) {
        if (dmg.type == Damage.Type.MAGICAL)
            dmg.value = (dmg.value.toFloat() * 0.75f).toInt()
    }
    
    override fun glowing(): ItemSprite.Glowing = ItemSprite.Glowing(0x88eeff) // teal
}