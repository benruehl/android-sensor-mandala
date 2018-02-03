package de.beuth.test.persistence.access

import android.app.Activity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.beuth.test.persistence.entities.Mandala
import java.util.*

/**
 * Created by Philipp Behrendt on 14.01.2018.
 */
class SharedPrefsMandalaDAO : MandalaDAO {

    private val sharedPrefTag = "MandalaPrefs"
    private val mandalaTag = "Mandalas"

    private var savedMandalas: HashMap<Long, Mandala> = HashMap()

    override fun findAll(act: Activity): List<Mandala> {
        val sharedPrefs = act.getSharedPreferences(sharedPrefTag, 0)
        val gson = Gson()
        val json = sharedPrefs.getString(mandalaTag, "")
        if(!json.equals(""))
            savedMandalas = gson.fromJson(json, object : TypeToken<Map<Long, Mandala>>() {}.type)

        return savedMandalas.map({ entry -> entry.value })
    }

    override fun find(act: Activity, id: Long): Mandala? {
        val sharedPrefs = act.getSharedPreferences(sharedPrefTag, 0)
        val gson = Gson()
        val json = sharedPrefs.getString(mandalaTag, "")
        if(!json.equals(""))
            savedMandalas = gson.fromJson(json, object : TypeToken<Map<Long, Mandala>>() {}.type)

        return savedMandalas[id]
    }

    override fun save(act: Activity, mandala: Mandala): Mandala {
        if (mandala.id == 0L)
            mandala.id = getNextId()

        mandala.creationDate = Calendar.getInstance().time

        savedMandalas.put(mandala.id, mandala)

        val sharedPrefs = act.getSharedPreferences(sharedPrefTag, 0)
        val gson = Gson()
        val json = gson.toJson(savedMandalas)
        sharedPrefs.edit().putString(mandalaTag, json).apply()

        return mandala
    }

    private fun getNextId(): Long {
        return (savedMandalas.keys.max() ?: 0) + 1
    }
}