package eam.edu.ingesoft.onlinestore.services

import eam.edu.ingesoft.onlinestore.exceptions.BusinessException
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
class UserServiceTest {

    @Autowired
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var userService: UserService

    @Test
    fun createUserHappyPathTest(){
        //prerequisitos
        //el Tienda no exista
        val city = City(2L, "armenia")
        entityManager.persist(city)
        userService.createUser(User("1", "el limonar", "Javier","Rodriguez Marulanda", city))

        val userToAssert = entityManager.find(User::class.java, "1")
        Assertions.assertNotNull(userToAssert)
        Assertions.assertEquals("Javier", userToAssert.name)
        Assertions.assertEquals("Rodriguez Marulanda", userToAssert.lastName)
    }

    @Test
    fun createUserAlreadyExistsTest(){
        //Prereqquisitos
        val city = City(2L, "armenia")
        entityManager.persist(city)
        entityManager.persist(User("1", "el limonar", "Javier","Rodriguez Marulanda", city))

        try {
            userService.createUser(User("1", "el limonar", "Javier","Rodriguez Marulanda", city))
            Assertions.fail()
        } catch (e: BusinessException) {
            Assertions.assertEquals("This person already exists", e.message)
        }
    }

    @Test
    fun editUserHappyPathTest(){
        val city = City(2L, "armenia")
        entityManager.persist(city)
        entityManager.persist(User("1", "el limonar", "Javier","Rodriguez Marulanda", city))

        val user = entityManager.find(User::class.java, "1")
        user.name = "Gladys"
        user.lastName = "Garcia"

        userService.editUser(user)

        val userToAssert = entityManager.find(User::class.java, "1")
        Assertions.assertEquals("Gladys", userToAssert.name)
        Assertions.assertEquals("Garcia", userToAssert.lastName)
    }

    @Test
    fun editStoreNotExistsTest(){
        val city = City(2L, "armenia")
        entityManager.persist(city)

        val exception = Assertions.assertThrows(
            BusinessException::class.java,
            { userService.editUser(User("2", "el limonar", "Javier","Rodriguez Marulanda", city))}
        )

        Assertions.assertEquals("This user does not exists", exception.message)
    }


}