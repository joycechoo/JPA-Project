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

import cs4347.hibernateProject.ecomm.entity.Purchase;
import cs4347.hibernateProject.ecomm.services.PurchasePersistenceService;
import cs4347.hibernateProject.ecomm.services.PurchaseSummary;
import cs4347.hibernateProject.ecomm.util.DAOException;

public class PurchasePersistenceServiceImpl implements PurchasePersistenceService
{
	@PersistenceContext 
	private EntityManager em; 
	
	public PurchasePersistenceServiceImpl(EntityManager em) {
		this.em = em;
	}
	
	@Override
	public void create(Purchase purchase) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			em.persist(purchase);
			em.getTransaction().commit();
		}
		catch (Exception e) {
			em.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public Purchase retrieve(Long id) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			Purchase purchase = em.find(Purchase.class, id);
			em.getTransaction().commit();
			return purchase;
		}
		catch (Exception e) {
			em.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public void update(Purchase purchase) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			
			Purchase purc = em.find(Purchase.class, purchase.getId());
			purc.setId(purchase.getId());
			purc.setCustomer(purchase.getCustomer());
			purc.setProduct(purchase.getProduct());
			purc.setPurchaseAmount(purchase.getPurchaseAmount());
			purc.setPurchaseDate(purchase.getPurchaseDate());
			
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
			
			Purchase pur = (Purchase)em.find(Purchase.class, id);
			em.remove(pur);
			
			em.getTransaction().commit();
		}
		catch (Exception e) {
			em.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public List<Purchase> retrieveForCustomerID(Long customerID) throws SQLException, DAOException
	{
		em.getTransaction().begin();
		
		@SuppressWarnings("unchecked")
		List<Purchase> purc = (List<Purchase>)em.createQuery("from Purchase as p where p.customer.id = :customerID")
			.setParameter("customerID", customerID)
			.getResultList();
		
		em.getTransaction().commit();
		
		if(purc == null) 
			throw new DAOException("Purchase by Customer " + customerID + " not found.");
		
		return purc;
	}

	@Override
	public PurchaseSummary retrievePurchaseSummary(Long customerID) throws SQLException, DAOException
	{
		double min = -1;
		double max = -1;
		double avg = -1;
		
		em.getTransaction().begin();
		
		@SuppressWarnings("unchecked")
		List<Purchase> ps = (List<Purchase>)em.createQuery("from Purchase as p where p.customer.id = :customerID")
		.setParameter("customerID", customerID)
		.getResultList();
		
		em.getTransaction().commit();

		if(ps == null)
			throw new DAOException("Purchase by Customer " + customerID + " not found.");
		
		else
		{
			min = ps.get(0).getPurchaseAmount();
			max = ps.get(0).getPurchaseAmount();
			avg = 0;
			double sum = 0;
			for(int i = 0; i < ps.size(); i++)
			{
				if(ps.get(i).getPurchaseAmount() < min)
					min = ps.get(1).getPurchaseAmount();
				if(ps.get(i).getPurchaseAmount() > max)
					max = ps.get(1).getPurchaseAmount();
				sum+=ps.get(i).getPurchaseAmount();
			}
			avg = sum/ps.size();
		}
		
		PurchaseSummary purcsum = new PurchaseSummary();
		purcsum.avgPurchase = avg;
		purcsum.minPurchase = min;
		purcsum.maxPurchase = max;
		return purcsum;
	}

	@Override
	public List<Purchase> retrieveForProductID(Long productID) throws SQLException, DAOException
	{
		em.getTransaction().begin();
		@SuppressWarnings("unchecked")
		List<Purchase> purc = (List<Purchase>)em.createQuery("from Purchase as p where p.product.id = :productID")
			.setParameter("productID", productID)
			.getResultList();
		em.getTransaction().commit();
		
		if(purc == null) 
			throw new DAOException("Purchase with productID " + productID + " not found.");
		
		return purc;
	}
}
