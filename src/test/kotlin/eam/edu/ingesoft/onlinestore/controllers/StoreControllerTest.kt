package eam.edu.ingesoft.onlinestore.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import eam.edu.ingesoft.onlinestore.model.entities.Category
import eam.edu.ingesoft.onlinestore.model.entities.City
import eam.edu.ingesoft.onlinestore.model.entities.Store
import eam.edu.ingesoft.onlinestore.model.entities.User
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
@Transactional
//arrancar el servidor web
@AutoConfigureMockMvc
class StoreControllerTest {
    @Autowired
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var mocMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun createStoreHappyPathTest() {
        //prerequisitos..
        entityManager.persist(City(1L, "Armenia"))

        val body = """
           {
            "id": "4",
            "address": "El cortijo",
            "name": "La tienda de los neas",
            "city":{
                "id": 1,
                "name": "Armenia"
            }
            }
        """.trimIndent()

        val request = MockMvcRequestBuilders
            .post("/stores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)

        val response = mocMvc.perform(request)
        val resp = response.andReturn().response
        //println(resp.contentAsString)
        Assertions.assertEquals(200, resp.status)
    }

    @Test
    fun createStoreNotFoundTest() {
        val city = City(1L, "Armenia")
        entityManager.persist(city)
        entityManager.persist(Store("1","br la clarita 40-15","La tienda de los neas", city))
        val body = """
           {
            "id": "1",
            "address": "br la clarita 40-15",
            "name": "La tienda de los neas",
            "city":{
                "id": 1,
                "name": "Armenia"
            }
            }
        """.trimIndent()

        val request = MockMvcRequestBuilders
            .post("/stores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)

        val response = mocMvc.perform(request)
        val resp = response.andReturn().response
        //println(resp.contentAsString)
        Assertions.assertEquals(412, resp.status)
        Assertions.assertEquals("{\"message\":\"This store already exists\",\"code\":412}".trimIndent(),
            resp.contentAsString)
    }

    @Test
    fun editUserHappyPathTest() {
        //prerequisitos..
        val city = City(1L, "Armenia")
        entityManager.persist(city)
        entityManager.persist(Store("1","br la clarita 40-15","La tienda de los neas", city))
        val body = """
            {
            "id": "4",
            "address": "El cortijo",
            "name": "La tienda de los neas",
            "city":{
                "id": 1,
                "name": "Armenia"
            }
            }
        """.trimIndent()

        val request = MockMvcRequestBuilders
            .put("/stores/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)

        val response = mocMvc.perform(request)
        val resp = response.andReturn().response
        //println(resp.contentAsString)
        Assertions.assertEquals(200, resp.status)
    }
}