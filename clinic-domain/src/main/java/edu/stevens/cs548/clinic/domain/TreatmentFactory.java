package edu.stevens.cs548.clinic.domain;

public class TreatmentFactory implements ITreatmentFactory {
	
	/*
	 * Patient and provider fields are set when the treatment is added (see Provider).
	 * Id field is set when the treatment entity is synced with the database (see TreatmentDAO).
	 */

	@Override
	public DrugTreatment createDrugTreatment() {
		return new DrugTreatment();
	}

	@Override
	public SurgeryTreatment createSurgeryTreatment() {
		// TODO Auto-generated method stub
		return new SurgeryTreatment();
	}

	@Override
	public RadiologyTreatment createRadiologyTreatment() {
		// TODO Auto-generated method stub
		return new RadiologyTreatment() ;
	}

	@Override
	public PhysiotherapyTreatment createPhysiotherapyTreatment() {
		// TODO Auto-generated method stub
		return new PhysiotherapyTreatment();
	}

	/*
	 * TODO define other factory methods
	 */
	

}
