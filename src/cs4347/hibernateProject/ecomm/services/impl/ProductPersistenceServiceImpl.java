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

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cs4347.hibernateProject.ecomm.entity.Customer;
import cs4347.hibernateProject.ecomm.entity.Product;
import cs4347.hibernateProject.ecomm.services.ProductPersistenceService;
import cs4347.hibernateProject.ecomm.util.DAOException;

public class ProductPersistenceServiceImpl implements ProductPersistenceService
{
	@PersistenceContext 
	private EntityManager em; 
	
	public ProductPersistenceServiceImpl(EntityManager em) {
		this.em = em;
	}
	
	@Override
	public void create(Product product) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			em.persist(product);
			em.getTransaction().commit();
		}
		catch (Exception e) {
			em.getTransaction().rollback();
			throw e;
		}
	}

	

	@Override
	public Product retrieve(Long id) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			Product product = em.find(Product.class, id);
			em.getTransaction().commit();
			return product;
		}
		catch (Exception e) {
			em.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public void update(Product product) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			
			Product prod = em.find(Product.class, product.getId());
			prod.setId(product.getId());
			prod.setProdCategory(product.getProdCategory());
			prod.setProdDescription(product.getProdDescription());
			prod.setProdName(product.getProdName());
			prod.setProdUPC(product.getProdUPC());
			
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
			Product product = em.find(Product.class, id);
			em.remove(product);
			em.getTransaction().commit();
		}
		catch (Exception e) {
			em.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public Product retrieveByUPC(String upc) throws SQLException, DAOException
	{
		em.getTransaction().begin();
		
		Product prod = (Product)em.createQuery("from Product as p where p.prodUPC = :prodUPC")
				.setParameter("prodUPC", upc)
				.getSingleResult();
		
		em.getTransaction().commit();
		
		if(prod == null) 
			throw new DAOException("Product upc not found " + upc);
		
		return prod;
	}


	@Override
	public List<Product> retrieveByCategory(int category) throws SQLException, DAOException
	{
		em.getTransaction().begin();
		
		@SuppressWarnings("unchecked")
		List<Product> prod = (List<Product>)em.createQuery("from Product as p where p.prodCategory = :prodCategory")
				.setParameter("prodCategory", category)
				.getResultList();
		
		em.getTransaction().commit();
		
		if(prod == null) 
			throw new DAOException("Product category not found " + category);
		
		return prod;
	}

}