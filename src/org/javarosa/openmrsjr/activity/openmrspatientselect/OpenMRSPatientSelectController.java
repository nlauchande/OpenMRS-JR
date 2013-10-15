package org.javarosa.openmrsjr.activity.openmrspatientselect;

import java.util.Vector;

import org.javarosa.core.services.storage.EntityFilter;
import org.javarosa.core.services.storage.IStorageIterator;
import org.javarosa.core.services.storage.IStorageUtility;
import org.javarosa.entity.api.EntitySelectController;
import org.javarosa.entity.api.transitions.EntitySelectTransitions;
import org.javarosa.entity.model.Entity;
import org.javarosa.entity.model.view.EntitySelectDetailPopup;
import org.javarosa.openmrsjr.openmrspatient.OpenMRSPatient;
import org.javarosa.openmrsjr.openmrspatient.OpenMRSPatientEntity;
import org.javarosa.openmrsjr.util.OpenMRSJRUtil;

public class OpenMRSPatientSelectController extends EntitySelectController<OpenMRSPatient> {
	
	protected OpenMRSPatientSelectView selView;
	protected OpenMRSPatientSelectTransitions transitions;
	protected OpenMRSPatientEntity entityPrototype;
	protected Vector<Entity<OpenMRSPatient>> entities;

	public OpenMRSPatientSelectController(String title,
			IStorageUtility entityStorage,
			OpenMRSPatientEntity entityPrototype, int newMode,
			boolean immediatelySelectNewlyCreated, boolean bailOnEmpty) {
		super(title, entityStorage, entityPrototype, newMode,
				immediatelySelectNewlyCreated, bailOnEmpty);
		this.entityStorage = entityStorage;
		
		this.immediatelySelectNewlyCreated = immediatelySelectNewlyCreated;
		this.bailOnEmpty = bailOnEmpty;
		
		this.entityPrototype = entityPrototype;
		this.selView = new OpenMRSPatientSelectView(this, entityPrototype, title, newMode);
	}
	public Vector<Integer> search (String key) {
		Vector<Integer> matches = new Vector<Integer>();
		
		if (key == null || key.equals("")) {
			for (int i = 0; i < entities.size(); i++)
				matches.addElement(new Integer(i));
		} else {
			for (int i = 0; i < entities.size(); i++) {
				Entity<OpenMRSPatient> entity = entities.elementAt(i);
				if (entity.match(key)) {
					matches.addElement(new Integer(i));
				}
			}
		}
		
		return matches;
	}
	
	public Entity<OpenMRSPatient> getEntity (int i) {
		return entities.elementAt(i) ;
	}	
	
	public void newEntity () {
		transitions.newEntity();
	}
	
	
	public void itemSelected (int i) {
		Entity<OpenMRSPatient> entity = entities.elementAt(i);
		EntitySelectDetailPopup<OpenMRSPatient> psdp = new EntitySelectDetailPopup<OpenMRSPatient>(this, entity, entityStorage);
		psdp.show();
	}
	public void entityChosen (int entityID) {
		transitions.entitySelected(entityID);
	}
	
	protected void loadEntities () {
		entities = new Vector<Entity<OpenMRSPatient>>();
		EntityFilter<? super OpenMRSPatient> filter = entityPrototype.getFilter();
		
		IStorageIterator ei = entityStorage.iterate();
		while (ei.hasMore()) {
			OpenMRSPatient obj = null;
			if (filter == null) {
				obj = (OpenMRSPatient)ei.nextRecord();
			} else {
				int id = ei.nextID();
				int preFilt = filter.preFilter(id, null);
				
				if (preFilt != EntityFilter.PREFILTER_EXCLUDE) {
					OpenMRSPatient candidateObj = (OpenMRSPatient)entityStorage.read(id);
					if (preFilt == EntityFilter.PREFILTER_INCLUDE || filter.matches(candidateObj)) {
						obj = candidateObj;
					}
				}
			}
			
			if (obj != null) {
				loadEntity(obj);
			}			
		}
	}
	public String[] getDataFields (int i) {
		return entities.elementAt(i).getShortFields();
	}
	
	public void start () {
		loadEntities();
		if(entities.isEmpty() && bailOnEmpty) {
			transitions.empty();
			return;
		} 
		selView.init();
		showList();
	}
	
	public void newEntity (int newEntityID) {
		//note: it is assumed that the newly created entity satisfies any filters in effect
		if (immediatelySelectNewlyCreated) {
			entityChosen(newEntityID);
		} else {
			OpenMRSPatient obj = (OpenMRSPatient) entityStorage.read(newEntityID);
			loadEntity(obj);
			selView.refresh(newEntityID);
			showList();
		}
	}
	
	protected void loadEntity (OpenMRSPatient obj) {
		Entity<OpenMRSPatient> entity = entityPrototype.factory();
		entity.readEntity(obj);
		entities.addElement(entity);
	}
	
	public void showList () {
		selView.show();
	}
	
	public void setTransitions (EntitySelectTransitions transitions) {
		this.transitions = (OpenMRSPatientSelectTransitions) transitions;
	}

	public void goFormList() {
		transitions.formList();
	}

	public void goUnsentEncounters() {
		transitions.unsentEncounters();
	}

	public void goCohortList() {
		transitions.cohortList();		
	}
	public void selectPatient() {
		try{
		int i = selView.getSelectedEntity();
		
		Entity<OpenMRSPatient> entity = entities.elementAt(i);
		EntitySelectDetailPopup<OpenMRSPatient> psdp = new EntitySelectDetailPopup<OpenMRSPatient>(this, entity, entityStorage);
		psdp.show();
		}catch(Exception E){
			E.printStackTrace();
		}
	}
	
	public void exit()
	{
		OpenMRSJRUtil.exit();
	}

}
