package de.beuth.test.persistence.access

import android.app.Activity
import de.beuth.test.persistence.entities.Mandala

/**
 * Created by Benjamin Rühl on 30.12.2017.
 */
interface MandalaDAO {
    fun findAll(act: Activity): List<Mandala>
    fun find(act: Activity, id: Long): Mandala?
    fun save(act: Activity, mandala: Mandala): Mandala
}