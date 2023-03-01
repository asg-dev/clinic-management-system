package edu.stevens.cs548.clinic.domain;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import edu.stevens.cs548.clinic.domain.ClinicDomainProducer.ClinicDomain;
import edu.stevens.cs548.clinic.domain.IPatientDao.PatientExn;

// TODO
@Transactional
@RequestScoped
public class ProviderDao implements IProviderDao {

	// TODO
	@Inject @ClinicDomain
	private EntityManager em;
	
	// TODO
	@Inject
	private ITreatmentDao treatmentDao;

	private Logger logger = Logger.getLogger(ProviderDao.class.getCanonicalName());

	@Override
	public void addProvider(Provider provider) throws ProviderExn {
		// Add to database, and initialize the provider aggregate with a treatment DAO.
		em.persist(provider);
		provider.setTreatmentDao(this.treatmentDao);
	}

	@Override
	/*
	 * The boolean flag indicates if related treatments should be loaded eagerly.
	 */
	public Provider getProvider(UUID id, boolean includeTreatments) throws ProviderExn {
		/*
		 * TODO retrieve Provider using external key
		 */
		String queryName = "SearchProviderByProviderId";
		TypedQuery<Provider> query = em.createNamedQuery(queryName, Provider.class).setParameter("providerId",id);
		List<Provider> providers = query.getResultList();
		
		if (providers.size() > 1) {
			throw new ProviderExn("Duplicate patient records: patient id = " + id);
		} else if (providers.size() < 1) {
			throw new ProviderExn("Patient not found: patient id = " + id);
		} else {
			Provider provider = providers.get(0);
			/*
			 * Refresh from database or we will never see new treatments.
			 */
			em.refresh(provider);
			provider.setTreatmentDao(this.treatmentDao);
			return provider;
		}
	}
	
	@Override
	/*
	 * By default, we eagerly load related treatments with a provider record.
	 */
	public Provider getProvider(UUID id) throws ProviderExn {
		return getProvider(id, true);
	}
	
	@Override
	public List<Provider> getProviders() {
		/*
		 * TODO Return a list of all providers (remember to set treatmentDAO)
		 */
		TypedQuery<Provider> query = em.createNamedQuery("SearchAllProviders", Provider.class);
		List<Provider> providers = query.getResultList();
		
		for (Provider provider : providers) {
			provider.setTreatmentDao(this.treatmentDao);
		}
		
		return providers;
	}
	
	@Override
	public void deleteProviders() {
		Query update = em.createNamedQuery("RemoveAllTreatments");
		update.executeUpdate();
		update = em.createNamedQuery("RemoveAllProviders");
		update.executeUpdate();
	}

}
