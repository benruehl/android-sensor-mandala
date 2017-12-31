package de.beuth.test.persistence.access

import de.beuth.test.persistence.entities.Mandala

/**
 * Created by Benjamin Rühl on 30.12.2017.
 */
interface MandalaDAO {
    fun findAll(): List<Mandala>
    fun save(mandala: Mandala): Mandala
}