package io_commands;

import objectClasses.Subscription;

/**
 * Treat resubscribe request
 * @author Ron Cohen
 *
 */
public class Resub extends Command
{
	public Subscription subAlmostEnd;
	public double newFullPrice;
	public double newPayedPrice;
	
	/**
	 * Constructor with the subscription that is going to end and the new prices
	 * @param subAlmostEnd the subscription that is going to end
	 * @param newFullPrice the new full price
	 * @param newPayedPrice the new payed price 
	 */
	public Resub(Subscription subAlmostEnd,double newFullPrice,double newPayedPrice) {
		this.subAlmostEnd=subAlmostEnd;
		this.newFullPrice=newFullPrice;
		this.newPayedPrice=newPayedPrice;
	}

	/**
	 * delete the request
	 */
	public void delete(){}
}
