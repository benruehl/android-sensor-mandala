package de.beuth.test.persistence.access

/**
 * Created by Benjamin Rühl on 30.12.2017.
 */
class InMemoryDAOFactory : DAOFactory {

    private var mandalaDAO: MandalaDAO = InMemoryMandalaDAO()

    override fun getMandalaDAO(): MandalaDAO {
        return mandalaDAO
    }
}