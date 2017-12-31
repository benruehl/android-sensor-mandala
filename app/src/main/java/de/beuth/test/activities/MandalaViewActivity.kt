package de.beuth.test.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import de.beuth.test.R
import de.beuth.test.persistence.access.DAOFactoryService
import de.beuth.test.utils.bind
import de.beuth.test.views.MandalaView
import de.beuth.test.views.color.MandalaColorizer

/**
 * Created by Benjamin Rühl on 31.12.2017.
 */
class MandalaViewActivity : AppCompatActivity() {

    private val mandalaView: MandalaView by bind(R.id.mandalaView)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mandala_view)

        val mandalaId: Long = intent.getLongExtra(MandalaGalleryActivity.INTENT_KEY_MANDALA_ID, 0)
        initMandalaView(mandalaId)
    }

    private fun initMandalaView(mandalaId: Long) {
        val mandalaById = DAOFactoryService.daoFactory.getMandalaDAO().find(mandalaId) ?: return

        mandalaView.surfaceCount = mandalaById.surfaceCount
        mandalaView.maxDataPointCount = Int.MAX_VALUE
        mandalaView.addDataPoints(mandalaById.dataPoints)
        mandalaView.colorizer = Class.forName(mandalaById.colorizerClassFullName).getConstructor().newInstance() as MandalaColorizer
    }
}