package eam.edu.ingesoft.onlinestore.repositories

import eam.edu.ingesoft.onlinestore.model.entities.City
import eam.edu.ingesoft.onlinestore.model.entities.User
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
@Transactional
class UserRepositoryTest {
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var entityManager: EntityManager

    @Test
    fun contextLoads() {
    }

    @Test
    fun testCreate() {
        val city = City(2L, "armenia")
        entityManager.persist(city)
        userRepository.create(User("1", "el limonar", "Javier","Rodriguez Marulanda", city))

        val user = entityManager.find(User::class.java, "1")
        Assertions.assertNotNull(user)
        Assertions.assertEquals("Javier", user.name)
        Assertions.assertEquals("Rodriguez Marulanda", user.lastName)
        Assertions.assertEquals("el limonar", user.address)
        Assertions.assertEquals("1", user.id)
    }

    @Test
    fun testUpdate() {
        //prerequisito
        val city = City(2L, "armenia")
        entityManager.persist(city)
        userRepository.create(User("1", "el limonar", "Javier","Rodriguez Marulanda", city))

        //ejecutando...
        val user = entityManager.find(User::class.java, "1")
        user.name = "gladys"
        user.lastName = "bernal"
        user.address = "cr 20"

        userRepository.update(user)

        //assersiones
        val userAssert = entityManager.find(User::class.java, "1")
        Assertions.assertEquals("gladys", userAssert.name)
        Assertions.assertEquals("bernal", userAssert.lastName)
        Assertions.assertEquals("cr 20", userAssert.address)
    }

    @Test
    fun findTest() {
        val city = City(2L, "armenia")
        entityManager.persist(city)
        userRepository.create(User("1", "el limonar", "Javier","Rodriguez Marulanda", city))

        val user = userRepository.find("1")

        Assertions.assertNotNull(user)
        Assertions.assertEquals("Javier", user?.name)
        Assertions.assertEquals("Rodriguez Marulanda", user?.lastName)
        Assertions.assertEquals("el limonar", user?.address)
    }

    @Test
    fun testDelete() {
        val city = City(2L, "armenia")
        entityManager.persist(city)
        userRepository.create(User("1", "el limonar", "Javier","Rodriguez Marulanda", city))
        userRepository.delete("1")

        val user = entityManager.find(User::class.java, "1")
        Assertions.assertNull(user)
    }

    @Test
    fun findByCity(){
        val city = City(2L, "armenia")
        entityManager.persist(city)
        userRepository.create(User("1", "el limonar", "Javier","Rodriguez Marulanda", city))
        userRepository.create(User("2", "zuldemaida", "johan","morales ortegon", city))
        userRepository.create(User("3", "calarca", "santiago","beltran", city))

        //ejecutando pruebas
        val citys = userRepository.findByCity(2L)

        Assertions.assertEquals(3,citys.size)

    }
}