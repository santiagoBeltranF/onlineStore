package eam.edu.ingesoft.onlinestore.repositories

import eam.edu.ingesoft.onlinestore.model.entities.Category
import eam.edu.ingesoft.onlinestore.model.entities.Product
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
@Transactional
class ProductRepositoryTest {

    @Autowired
    lateinit var productRepository: ProductRepository

    @Autowired
    lateinit var entityManager: EntityManager

    @Test
    fun contextLoads() {
    }

    @Test
    fun testCreate() {
        val category = Category(2L,"categoria uno")
        entityManager.persist(category)
        productRepository.create(Product(2L, "iphone 13","nike",category))

        val product = entityManager.find(Product::class.java,2L)
        Assertions.assertNotNull(product)
        Assertions.assertEquals("iphone 13", product.name)
        Assertions.assertEquals(2L, product.id)
    }

    @Test
    fun testUpdate() {
        val category = Category(2L,"categoria uno")
        entityManager.persist(category)
        entityManager.persist(Product(2L, "iphone 13","nike",category))

        //ejecutando...
        val product = entityManager.find(Product::class.java, 2L)
        product.name = "iphone 12"

        productRepository.update(product)

        //assersiones
        val productAssert = entityManager.find(Product::class.java, 2L)
        Assertions.assertEquals("iphone 12", productAssert.name)
    }

    @Test
    fun findTest() {
        val category = Category(2L,"categoria uno")
        entityManager.persist(category)
        entityManager.persist(Product(2L, "iphone 13","nike",category))

        val prodruct = productRepository.find(2L)

        Assertions.assertNotNull(prodruct)
        Assertions.assertEquals("iphone 13", prodruct?.name)
    }

    @Test
    fun testDelete() {
        val category = Category(2L,"categoria uno")
        entityManager.persist(category)
        entityManager.persist(Product(2L, "iphone 13","nike",category))
        productRepository.delete(2L)

        val product = entityManager.find(Product::class.java, 2L)
        Assertions.assertNull(product)
    }

    @Test
    fun findByCategory(){
        val category = Category(2L,"categoria uno")
        entityManager.persist(category)
        entityManager.persist(Product(2L, "iphone 13","nike",category))
        entityManager.persist(Product(3L, "iphone 12","apple",category))
        entityManager.persist(Product(4L, "iphone 14","applewatch",category))
        entityManager.persist(Product(5L, "iphone 15","disney",category))

        val categorys = productRepository.findByCategory(2L)

        Assertions.assertEquals(4,categorys.size)
    }

}