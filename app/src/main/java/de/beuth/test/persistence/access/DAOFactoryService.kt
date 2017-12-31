package de.beuth.test.persistence.access

/**
 * Created by Benjamin Rühl on 30.12.2017.
 */
class DAOFactoryService {

    companion object {
        var daoFactory: DAOFactory = InMemoryDAOFactory()
            private set
    }
}