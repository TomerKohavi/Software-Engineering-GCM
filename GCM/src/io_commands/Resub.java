package io_commands;

import objectClasses.Subscription;

/**
 * Treat resubscribe request
 * @author Israel_Cohen
 *
 */
public class Resub 
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

}
