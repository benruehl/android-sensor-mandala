package de.beuth.test.persistence.access

import de.beuth.test.persistence.entities.Mandala
import java.util.*

/**
 * Created by Benjamin Rühl on 30.12.2017.
 */
class InMemoryMandalaDAO : MandalaDAO {

    private var savedMandalas: HashMap<Long, Mandala> = HashMap()

    override fun findAll(): List<Mandala> {
        return savedMandalas.map({ entry -> entry.value })
    }

    override fun save(mandala: Mandala): Mandala {
        if (mandala.id == 0L)
            mandala.id = getNextId()

        mandala.creationDate = Calendar.getInstance().time

        savedMandalas.put(mandala.id, mandala)

        return mandala
    }

    private fun getNextId(): Long {
        return (savedMandalas.keys.max() ?: 0) + 1
    }
}