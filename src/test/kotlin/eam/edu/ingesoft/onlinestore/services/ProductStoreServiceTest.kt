package eam.edu.ingesoft.onlinestore.services

import eam.edu.ingesoft.onlinestore.exceptions.BusinessException
import eam.edu.ingesoft.onlinestore.model.entities.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
@Transactional
class ProductStoreServiceTest {
    @Autowired
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var productStoreService: ProductStoreService

    @Test
    fun createProductStoreHappyPathTest(){
        //prerequisitos
        //el productTienda bo exista
        val category = Category(4L,"categoria uno")
        entityManager.persist(category)
        val product = Product(3L, "iphone 13","nike",category)
        entityManager.persist(product)
        val city = City(1L, "armenia")
        entityManager.persist(city)
        val store = Store("1","br antonio nariño cr44 #48-47","Tienda don yeye",city)
        productStoreService.createProductStore(ProductStore(2L, 200.000,20.0,product,store))

        val productStoretoAssert = entityManager.find(ProductStore::class.java, 2L)
        Assertions.assertNotNull(productStoretoAssert)

        Assertions.assertEquals(200.000, productStoretoAssert.price)
        Assertions.assertEquals(20.0, productStoretoAssert.stock)
        }

    @Test
    fun createStoreProductAlreadyExistsTest(){
        //prerequisitos
        val category = Category(4L,"categoria uno")
        entityManager.persist(category)
        val product = Product(3L, "iphone 13","nike",category)
        entityManager.persist(product)
        val city = City(1L, "armenia")
        entityManager.persist(city)
        val store = Store("1","br antonio nariño cr44 #48-47","Tienda don yeye",city)
        entityManager.persist(ProductStore(2L, 200.000,20.0,product,store))

        try {
            productStoreService.createProductStore(ProductStore(2L, 200.000,20.0,product,store))
            Assertions.fail()
        } catch (e: BusinessException) {
            Assertions.assertEquals("This product already exists on the store", e.message)
        }
    }

    @Test
    fun decreaseProductstoreHappyPathTest(){
        val category = Category(4L,"categoria uno")
        entityManager.persist(category)
        val product = Product(3L, "iphone 13","nike",category)
        entityManager.persist(product)
        val city = City(1L, "armenia")
        entityManager.persist(city)
        val store = Store("1","br antonio nariño cr44 #48-47","Tienda don yeye",city)
        entityManager.persist(ProductStore(2L, 200.000,30.0,product,store))

        productStoreService.decreaseStockProductStore(ProductStore(2L, 200.000,30.0,product,store),30.0)

        val productStoreAssert = entityManager.find(ProductStore::class.java, 2L)
        Assertions.assertEquals(0.0, productStoreAssert.stock)
    }

    @Test
    fun decreaseProductstoreNotExists(){
        val category = Category(4L,"categoria uno")
        entityManager.persist(category)
        val product = Product(3L, "iphone 13","nike",category)
        entityManager.persist(product)
        val city = City(1L, "armenia")
        entityManager.persist(city)
        val store = Store("1","br antonio nariño cr44 #48-47","Tienda don yeye",city)

        val exception = Assertions.assertThrows(
            BusinessException::class.java,
            { productStoreService.decreaseStockProductStore(ProductStore(2L, 200.000,30.0,product,store),30.0) }
        )

        Assertions.assertEquals("This product in this store does not exists", exception.message)
    }

    @Test
    fun decreaseProductstoreNotStock(){
        val category = Category(4L,"categoria uno")
        entityManager.persist(category)
        val product = Product(3L, "iphone 13","nike",category)
        entityManager.persist(product)
        val city = City(1L, "armenia")
        entityManager.persist(city)
        val store = Store("1","br antonio nariño cr44 #48-47","Tienda don yeye",city)
        entityManager.persist(ProductStore(2L, 200.000,30.0,product,store))

        val exception = Assertions.assertThrows(
            BusinessException::class.java,
            { productStoreService.decreaseStockProductStore(ProductStore(2L, 200.000,30.0,product,store),50.0) }
        )

        Assertions.assertEquals("There is not stock", exception.message)
    }

    @Test
    fun increaseProductstoreHappyPathTest(){
        val category = Category(4L,"categoria uno")
        entityManager.persist(category)
        val product = Product(3L, "iphone 13","nike",category)
        entityManager.persist(product)
        val city = City(1L, "armenia")
        entityManager.persist(city)
        val store = Store("1","br antonio nariño cr44 #48-47","Tienda don yeye",city)
        entityManager.persist(ProductStore(2L, 200.000,30.0,product,store))

        productStoreService.increaseStockProductStore(ProductStore(2L, 200.000,30.0,product,store),30.0)

        val productStoreAssert = entityManager.find(ProductStore::class.java, 2L)
        Assertions.assertEquals(60.0, productStoreAssert.stock)
    }

    @Test
    fun increaseProductstoreNotExists(){
        val category = Category(4L,"categoria uno")
        entityManager.persist(category)
        val product = Product(3L, "iphone 13","nike",category)
        entityManager.persist(product)
        val city = City(1L, "armenia")
        entityManager.persist(city)
        val store = Store("1","br antonio nariño cr44 #48-47","Tienda don yeye",city)

        val exception = Assertions.assertThrows(
            BusinessException::class.java,
            { productStoreService.increaseStockProductStore(ProductStore(2L, 200.000,30.0,product,store),30.0) }
        )

        Assertions.assertEquals("This product in this store does not exists", exception.message)
    }

    @Test
    fun listProductsByProductStore(){
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

        val products = productStoreService.listProductsByProductStore(3L)

        Assertions.assertEquals(4,products.size)
    }

    @Test
    fun listStoreByProductStore(){
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

        val products = productStoreService.listStoreByProductStore("1")

        Assertions.assertEquals(4,products.size)
    }

}
