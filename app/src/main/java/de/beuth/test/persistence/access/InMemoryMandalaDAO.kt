package de.beuth.test.persistence.access

import android.app.Activity
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.beuth.test.persistence.entities.Mandala
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by Benjamin Rühl on 30.12.2017.
 */
class InMemoryMandalaDAO : MandalaDAO {

    private var savedMandalas: HashMap<Long, Mandala> = HashMap()

    override fun findAll(act: Activity): List<Mandala> {
        return savedMandalas.map({ entry -> entry.value })
    }

    override fun find(act: Activity, id: Long): Mandala? {
        return savedMandalas[id]
    }

    override fun save(act: Activity, mandala: Mandala): Mandala {
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