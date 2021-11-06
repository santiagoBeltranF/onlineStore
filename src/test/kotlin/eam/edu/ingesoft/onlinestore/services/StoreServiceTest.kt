package eam.edu.ingesoft.onlinestore.services

import eam.edu.ingesoft.onlinestore.exceptions.BusinessException
import eam.edu.ingesoft.onlinestore.model.entities.City
import eam.edu.ingesoft.onlinestore.model.entities.Store
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
@Transactional
class StoreServiceTest {
    @Autowired
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var storeService: StoreService

    @Test
    fun createStoreHappyPathTest(){
        //prerequisitos
        //el Tienda no exista
        val city = City(2L, "armenia")
        entityManager.persist(city)
        storeService.createStore(Store("1", "el limonar", "la tienda de don yeye", city))

        val storeToAssert = entityManager.find(Store::class.java, "1")
        Assertions.assertNotNull(storeToAssert)
        Assertions.assertEquals("la tienda de don yeye", storeToAssert.name)
    }

    @Test
    fun createStoreAlreadyExistsTest(){
        //Prereqquisitos
        val city = City(2L, "armenia")
        entityManager.persist(city)
        entityManager.persist(Store("1", "el limonar", "la tienda de don yeye", city))

        try {
            storeService.createStore(Store("1", "el limonar", "la tienda de don yeye", city))
            Assertions.fail()
        } catch (e: BusinessException) {
            Assertions.assertEquals("This store already exists", e.message)
        }
    }

    @Test
    fun editStoreHappyPathTest(){
        val city = City(2L, "armenia")
        entityManager.persist(city)
        entityManager.persist(Store("1", "el limonar", "la tienda de don yeye", city))

        val store = entityManager.find(Store::class.java, "1")
        store.name = "La tienda de don yoyans"
        store.address = "yoyans la 20 Armenia"

        storeService.editStore(store)

        val storeAssert = entityManager.find(Store::class.java, "1")
        Assertions.assertEquals("La tienda de don yoyans", storeAssert.name)
        Assertions.assertEquals("yoyans la 20 Armenia", storeAssert.address)
    }

    @Test
    fun editStoreNotExistsTest(){
        val city = City(2L, "armenia")
        entityManager.persist(city)

        val exception = Assertions.assertThrows(
            BusinessException::class.java,
            { storeService.editStore(Store("1", "el limonar", "la tienda de don yeye", city)) }
        )

        Assertions.assertEquals("This store does not exists", exception.message)
    }



}