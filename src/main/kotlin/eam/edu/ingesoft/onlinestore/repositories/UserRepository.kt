package eam.edu.ingesoft.onlinestore.repositories

import eam.edu.ingesoft.onlinestore.model.entities.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Component
@Transactional
class UserRepository {
    @Autowired
    lateinit var em: EntityManager

    fun create(user: User){
        em.persist(user)
    }


    fun find(id: String): User?{
        return em.find(User::class.java, id)
    }

    fun update(user: User) {
        em.merge(user)
    }

    fun delete(id: String) {
        val user = find(id)
        if (user!=null) {
            em.remove(user)
        }
    }

    fun findByCity(id: Long): List<User> {
        val query = em.createQuery("SELECT user FROM User user WHERE user.city.id =: id")
        query.setParameter("id", id)

        return query.resultList as List<User>
    }
}