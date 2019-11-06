package edu.ucsd.coeus.personalization;


public interface CObservable {
	public void registerObserver(CObserver o);
	public void removeObserver(CObserver o);
	public void notifyObservers();
}
