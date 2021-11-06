package eam.edu.ingesoft.onlinestore.repositories

import eam.edu.ingesoft.onlinestore.model.entities.Category
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
@Transactional
class CategoryRepositoryTest {
    @Autowired
    lateinit var categoryRepository: CategoryRepository

    @Autowired
    lateinit var entityManager: EntityManager

    @Test
    fun contextLoads() {
    }

    @Test
    fun testCreate() {
         categoryRepository.create(Category(2L, "categoria uno"))

        val category = entityManager.find(Category::class.java,2L)
        Assertions.assertNotNull(category)
        Assertions.assertEquals("categoria uno", category.name)
        Assertions.assertEquals(2L, category.id)
    }

    @Test
    fun testUpdate() {
        //prerequisito
        entityManager.persist(Category(2L,"categoria uno"))

        //ejecutando...
        val category = entityManager.find(Category::class.java, 2L)
        category.name = "categoria dos"

        categoryRepository.update(category)

        //assersiones
        val categoryAssert = entityManager.find(Category::class.java, 2L)
        Assertions.assertEquals("categoria dos", categoryAssert.name)
    }

    @Test
    fun findTest() {
        entityManager.persist(Category(2L,"categoria uno"))

        val category = categoryRepository.find(2L)

        Assertions.assertNotNull(category)
        Assertions.assertEquals("categoria uno", category?.name)
    }

    @Test
    fun testDelete() {
        entityManager.persist(Category(2L,"categoria uno"))
        categoryRepository.delete(2L)

        val category = entityManager.find(Category::class.java, 2L)
        Assertions.assertNull(category)
    }
}