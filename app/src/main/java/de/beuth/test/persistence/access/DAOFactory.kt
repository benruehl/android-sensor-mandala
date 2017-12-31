package de.beuth.test.persistence.access

/**
 * Created by Benjamin Rühl on 30.12.2017.
 */
interface DAOFactory {
    fun getMandalaDAO(): MandalaDAO
}