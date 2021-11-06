package eam.edu.ingesoft.onlinestore.repositories

import eam.edu.ingesoft.onlinestore.model.entities.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
@Transactional
class ProductStoreRepositoryTest {

    @Autowired
    lateinit var productStoreRepository: ProductStoreRepository

    @Autowired
    lateinit var entityManager: EntityManager

    @Test
    fun contextLoads() {
    }

    @Test
    fun testCreate() {
        val category = Category(4L,"categoria uno")
        entityManager.persist(category)
        val product = Product(3L, "iphone 13","nike",category)
        entityManager.persist(product)
        val city = City(1L, "armenia")
        entityManager.persist(city)
        val store = Store("1","br antonio nariño cr44 #48-47","Tienda don yeye",city)
        productStoreRepository.create(ProductStore(2L, 200.000,20.0,product,store))

        val productStore = entityManager.find(ProductStore::class.java,2L)
        Assertions.assertNotNull(productStore)
        Assertions.assertEquals(200.000, productStore.price)
        Assertions.assertEquals(2L, productStore.id)
        Assertions.assertEquals(20.0, productStore.stock)
    }

    @Test
    fun testUpdate() {
        val category = Category(4L,"categoria uno")
        entityManager.persist(category)
        val product = Product(3L, "iphone 13","nike",category)
        entityManager.persist(product)
        val city = City(1L, "armenia")
        entityManager.persist(city)
        val store = Store("1","br antonio nariño cr44 #48-47","Tienda don yeye",city)
        productStoreRepository.create(ProductStore(2L, 200.000,20.0,product,store))

        //ejecutando...
        val productStore = entityManager.find(ProductStore::class.java, 2L)
        productStore.price = 300.000
        productStore.stock = 900.0

        productStoreRepository.update(productStore)

        //assersiones
        val productStoreAssert = entityManager.find(ProductStore::class.java, 2L)
        Assertions.assertEquals(300.000, productStoreAssert.price)
        Assertions.assertEquals(900.0, productStoreAssert.stock)
    }

    @Test
    fun findTest() {
        val category = Category(4L,"categoria uno")
        entityManager.persist(category)
        val product = Product(3L, "iphone 13","nike",category)
        entityManager.persist(product)
        val city = City(1L, "armenia")
        entityManager.persist(city)
        val store = Store("1","br antonio nariño cr44 #48-47","Tienda don yeye",city)
        productStoreRepository.create(ProductStore(2L, 200.000,20.0,product,store))

        val productStore = productStoreRepository.find(2L)

        Assertions.assertNotNull(productStore)
        Assertions.assertEquals(200.000, productStore?.price)
        Assertions.assertEquals(20.0, productStore?.stock)
    }

    @Test
    fun testDelete() {
        val category = Category(4L,"categoria uno")
        entityManager.persist(category)
        val product = Product(3L, "iphone 13","nike",category)
        entityManager.persist(product)
        val city = City(1L, "armenia")
        entityManager.persist(city)
        val store = Store("1","br antonio nariño cr44 #48-47","Tienda don yeye",city)
        productStoreRepository.create(ProductStore(2L, 200.000,20.0,product,store))
        productStoreRepository.delete(2L)

        val productStore = entityManager.find(ProductStore::class.java, 2L)
        Assertions.assertNull(productStore)
    }

    @Test
    fun findByProduct() {
        val category = Category(1L, "categoria uno")
        entityManager.persist(category)
        val product = Product(3L, "iphone 13", "nike", category)
        entityManager.persist(product)
        val city = City(1L, "armenia")
        entityManager.persist(city)
        val store = Store("1","br antonio nariño cr44 #48-47","Tienda don yeye",city)
        entityManager.persist(store)
        entityManager.persist(ProductStore(1L, 200.000,20.0,product,store))
        entityManager.persist(ProductStore(2L, 300.000,30.0,product,store))
        entityManager.persist(ProductStore(3L, 400.000,40.0,product,store))
        entityManager.persist(ProductStore(4L, 500.000,50.0,product,store))

        //ejecutando pruebas
        val products = productStoreRepository.findByProduct(3L)

        Assertions.assertEquals(4,products.size)
    }

    @Test
    fun findByStore() {
        val category = Category(1L, "categoria uno")
        entityManager.persist(category)
        val product = Product(3L, "iphone 13", "nike", category)
        entityManager.persist(product)
        val city = City(1L, "armenia")
        entityManager.persist(city)
        val store = Store("1","br antonio nariño cr44 #48-47","Tienda don yeye",city)
        entityManager.persist(store)
        entityManager.persist(ProductStore(1L, 200.000,20.0,product,store))
        entityManager.persist(ProductStore(2L, 300.000,30.0,product,store))
        entityManager.persist(ProductStore(3L, 400.000,40.0,product,store))
        entityManager.persist(ProductStore(4L, 500.000,50.0,product,store))

        //ejecutando pruebas
        val stores = productStoreRepository.findByStore("1")

        Assertions.assertEquals(4,stores.size)
    }
}