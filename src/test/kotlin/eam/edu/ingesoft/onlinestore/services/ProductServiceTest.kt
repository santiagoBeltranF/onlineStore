package eam.edu.ingesoft.onlinestore.services

import eam.edu.ingesoft.onlinestore.exceptions.BusinessException
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
class ProductServiceTest {

    @Autowired
    lateinit var productService: ProductService

    @Autowired
    lateinit var entityManager: EntityManager

    @Test
    fun createProductHappyPathTest() {
        //prerequisitos
        //Que el producto con ese nombre no este previamente registrado

        //ejecucion de la pruba
        val category = Category(1L, "bebidas gaseosas")
        entityManager.persist(category)
        productService.createProduct(Product(2L, "coca-cola yeye", "coca-cola", category))

        //verificaciones

        val productToAssert = entityManager.find(Product::class.java, 2L)
        Assertions.assertNotNull(productToAssert)
        Assertions.assertEquals("coca-cola yeye", productToAssert.name)
    }

    @Test
    fun createProductRepeatedNameTest() {
        //prerequisitos
        val category = Category(1L, "bebidas gaseosas")
        entityManager.persist(category)
        entityManager.persist(Product(1L, "coca-cola yeye", "coca-cola", category))

        val exception = Assertions.assertThrows(
            BusinessException::class.java,
            { productService.createProduct(Product(1L, "coca-cola yeye", "coca-cola", category)) }
        )


        Assertions.assertEquals("This product whit this name already exists", exception.message)
    }

    @Test
    fun editPersonHappyPath(){
        val category = Category(1L, "bebidas gaseosas")
        entityManager.persist(category)
        entityManager.persist(Product(1L, "coca-cola yeye", "coca-cola", category))

        val product = entityManager.find(Product::class.java, 1L)
        product.name = "coca-cola yoyans"

        productService.editProduct(product)

        val productAssert = entityManager.find(Product::class.java, 1L)
        Assertions.assertEquals("coca-cola yoyans", productAssert.name)
    }

    @Test
    fun productNotExistsTest(){
        val category = Category(1L, "bebidas gaseosas")
        entityManager.persist(category)

        val exception = Assertions.assertThrows(
            BusinessException::class.java,
            { productService.editProduct(Product(1L, "coca-cola yeye", "coca-cola", category)) }
        )

        Assertions.assertEquals("This product whit this name does not exists", exception.message)
    }

    @Test
    fun listCategoryByProduct(){
        val category = Category(2L,"categoria uno")
        entityManager.persist(category)
        entityManager.persist(Product(2L, "iphone 13","nike",category))
        entityManager.persist(Product(3L, "iphone 12","apple",category))
        entityManager.persist(Product(4L, "iphone 14","applewatch",category))
        entityManager.persist(Product(5L, "iphone 15","disney",category))

        val categorys = productService.listCategoryByProduct(2L)

        Assertions.assertEquals(4,categorys.size)
    }


}