/* NOTICE: All materials provided by this project, and materials derived 
 * from the project, are the property of the University of Texas. 
 * Project materials, or those derived from the materials, cannot be placed 
 * into publicly accessible locations on the web. Project materials cannot 
 * be shared with other project teams. Making project materials publicly 
 * accessible, or sharing with other project teams will result in the 
 * failure of the team responsible and any team that uses the shared materials. 
 * Sharing project materials or using shared materials will also result 
 * in the reporting of all team members for academic dishonesty. 
 */ 
 
package cs4347.hibernateProject.ecomm.services.impl;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cs4347.hibernateProject.ecomm.entity.Customer;
import cs4347.hibernateProject.ecomm.services.CustomerPersistenceService;
import cs4347.hibernateProject.ecomm.util.DAOException;

public class CustomerPersistenceServiceImpl implements CustomerPersistenceService
{
	@PersistenceContext 
	private EntityManager em; 
	
	public CustomerPersistenceServiceImpl(EntityManager em) {
		this.em = em;
	}
	
	@Override
	public void create(Customer customer) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			em.persist(customer);
			em.getTransaction().commit();
		}
		catch (Exception e) {
			em.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public Customer retrieve(Long id) 
	{
		try {
			em.getTransaction().begin();
			Customer cust1 = em.find(Customer.class, id);
			em.getTransaction().commit();
			return cust1;
		}
		catch (Exception e) {
			em.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public void update(Customer cust1) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			Customer cust2 = em.find(Customer.class, cust1.getId());
			cust2.setId(cust1.getId());
			cust2.setFirstName(cust1.getFirstName());
			cust2.setLastName(cust1.getLastName());
			cust2.setDob(cust1.getDob());
			cust2.setGender(cust1.getGender());
			cust2.setEmail(cust1.getEmail());
			cust2.setAddress(cust1.getAddress());
			cust2.setCreditCard(cust1.getCreditCard());
			em.getTransaction().commit();
			
		}
		catch(Exception e) {
			em.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public void delete(Long id) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			Customer cust1 = (Customer)em.find(Customer.class, id);
			em.remove(cust1);
			em.getTransaction().commit();
		}
		catch (Exception e) {
			em.getTransaction().rollback();
			throw e;
		}
	}

	
	@Override
	public List<Customer> retrieveByZipCode(String zipCode) throws SQLException, DAOException
	{
		em.getTransaction().begin();
		
		@SuppressWarnings("unchecked")
		List<Customer> cust1 = (List<Customer>)em.createQuery("from Customer as cust where cust.address.zipcode = :zipcode")
		.setParameter("zipcode", zipCode)
		.getResultList();
		
		em.getTransaction().commit();
		return cust1;
	}


	@Override
	public List<Customer> retrieveByDOB(Date startDate, Date endDate) throws SQLException, DAOException
	{
		em.getTransaction().begin();
		
		@SuppressWarnings("unchecked")
		List<Customer> cust1 = (List<Customer>)em.createQuery("from Customer as cust where cust.dob between :startDate and :endDate")
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getResultList();
		em.getTransaction().commit();
		return cust1;
	}
}