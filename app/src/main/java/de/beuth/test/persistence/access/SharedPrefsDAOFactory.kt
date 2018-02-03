package de.beuth.test.persistence.access

/**
 * Created by Benjamin Rühl on 30.12.2017.
 */
class SharedPrefsDAOFactory : DAOFactory {

    private var mandalaDAO: MandalaDAO = SharedPrefsMandalaDAO()

    override fun getMandalaDAO(): MandalaDAO {
        return mandalaDAO
    }
}