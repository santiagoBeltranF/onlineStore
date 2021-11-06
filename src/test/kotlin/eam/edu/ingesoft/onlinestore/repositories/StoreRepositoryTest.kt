package eam.edu.ingesoft.onlinestore.repositories

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
class StoreRepositoryTest {
    @Autowired
    lateinit var storeRepository: StoreRepository

    @Autowired
    lateinit var entityManager: EntityManager

    @Test
    fun contextLoads() {
    }

    @Test
    fun testCreate() {
        val city = City(2L, "armenia")
        entityManager.persist(city)
        storeRepository.create(Store("1", "el limonar", "la tienda de don yeye", city))

        val store = entityManager.find(Store::class.java, "1")
        Assertions.assertNotNull(store)
        Assertions.assertEquals("la tienda de don yeye", store.name)
        Assertions.assertEquals("el limonar", store.address)
        Assertions.assertEquals("1", store.id)
    }

    @Test
    fun testUpdate() {
        //prerequisito
        val city = City(2L, "armenia")
        entityManager.persist(city)
        entityManager.persist(Store("1", "el limonar", "la tienda de don yeye", city))

        //ejecutando...
        val store = entityManager.find(Store::class.java, "1")
        store.name = "panaderia la 20"
        store.address = "cr 20"

        storeRepository.update(store)

        //assersiones
        val storeAssert = entityManager.find(Store::class.java, "1")
        Assertions.assertEquals("panaderia la 20", storeAssert.name)
        Assertions.assertEquals("cr 20", storeAssert.address)
    }

    @Test
    fun findTest() {
        val city = City(2L, "armenia")
        entityManager.persist(city)
        entityManager.persist(Store("1", "el limonar", "la tienda de don yeye", city))

        val user = storeRepository.find("1")

        Assertions.assertNotNull(user)
        Assertions.assertEquals("la tienda de don yeye", user?.name)
        Assertions.assertEquals("el limonar", user?.address)
    }

    @Test
    fun testDelete() {
        val city = City(2L, "armenia")
        entityManager.persist(city)
        entityManager.persist(Store("1", "el limonar", "la tienda de don yeye", city))
        storeRepository.delete("1")

        val category = entityManager.find(Store::class.java, "1")
        Assertions.assertNull(category)
    }


    @Test
    fun findByCity(){
        val city = City(2L, "armenia")
        entityManager.persist(city)
        entityManager.persist(Store("1", "el limonar", "la tienda de don yeye", city))
        entityManager.persist(Store("2", "boloclub", "la tienda de yogurts", city))
        entityManager.persist(Store("3", "zuldemaida", "la tienda de apples", city))
        entityManager.persist(Store("4", "ciudad dorada", "la tienda de ingenieros", city))

        //ejecutando pruebas
        val citys = storeRepository.findByCity(2L)

        Assertions.assertEquals(4,citys.size)

    }

}