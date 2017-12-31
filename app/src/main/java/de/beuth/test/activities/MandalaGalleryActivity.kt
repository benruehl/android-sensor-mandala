package de.beuth.test.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.GONE
import android.widget.ArrayAdapter
import android.widget.GridView
import de.beuth.test.R
import de.beuth.test.adapters.MandalaGalleryGridAdapter
import de.beuth.test.persistence.access.DAOFactoryService
import de.beuth.test.utils.bind

/**
 * Created by Benjamin Rühl on 31.12.2017.
 */
class MandalaGalleryActivity : AppCompatActivity() {

    private val galleryGrid: GridView by bind(R.id.mandalaGalleryGrid)

    private val galleryEmptyPlaceholder: View by bind(R.id.mandalaGalleryEmptyPlaceholder)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mandala_gallery)

        initEmptyGalleryPlaceholder()
        initGalleryGrid()
    }

    private fun initGalleryGrid() {
        val persistedMandalas = DAOFactoryService.daoFactory.getMandalaDAO().findAll().sortedByDescending { m -> m.creationDate }

        galleryGrid.adapter = MandalaGalleryGridAdapter(this, android.R.layout.simple_gallery_item, persistedMandalas)
    }

    private fun initEmptyGalleryPlaceholder() {
        if (DAOFactoryService.daoFactory.getMandalaDAO().findAll().isNotEmpty())
            galleryEmptyPlaceholder.visibility = GONE
    }
}