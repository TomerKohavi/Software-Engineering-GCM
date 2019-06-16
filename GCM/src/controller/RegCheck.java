package controller;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.YearMonth;

public class RegCheck {

	/**
	 * Enum that describe the problems with the registration input
	 */
	public enum Res {
		UName("Illegal username. Username should contain only letters and numbers, at least 2 chars long, and less than 32 chars long."),
		Pass("Illegal password- " + "it must be at least 8 char long, less than 32 char long " + "with lower case and upper case letters, "
				+ "at least one digit and at least one special character."),
		FName("Illegal first name. It should have at least two letters, and only letters."),
		LName("Illegal last name.  It should have at least two letters, and only letters."), 
		Email("Illegal Email"), Phone("Illegal phone number"),
		CardNum("The credit card is illegal. Please insert 16 digits, without spaces."),
		CVV("The CVV value is illegal. Please insert 3 digits."),
		AllGood("All Good"),
		Expires("The expires date of the card is illegal.");

		private final String msg;

		/**
		 * Constructor.
		 * 
		 * @param nmsg
		 */
		Res(final String nmsg) {
			msg = nmsg;
		}

		/**
		 * @return the msg
		 */
		public String getValue() {
			return msg;
		}
	}

	/**
	 * Check if an email has a valid shape.
	 * 
	 * @param email
	 * @return whether the email is legal
	 */
	private static boolean isValidEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;
		return pat.matcher(email).matches();
	}

	/**
	 * Check if the password is legal and safe. (?=.*[0-9]) a digit must occur at
	 * least once (?=.*[a-z]) a lower case letter must occur at least once
	 * (?=.*[A-Z]) an upper case letter must occur at least once (?=.*[!@#$%^&+=]) a
	 * special character must occur at least once (?=\\S+$) no whitespace allowed in
	 * the entire string .{8,} at least 8 characters.
	 * 
	 * @param pass the password we need the check.
	 * @return boolean result
	 */
	private static boolean isValidPassword(String pass) {
		String passRegex = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,32}";

		Pattern pat = Pattern.compile(passRegex);
		if (pass == null)
			return false;
		return pat.matcher(pass).matches();
	}

	/**
	 * Check if the phone number has a valid shape.
	 * 
	 * @param phone
	 * @return the result, as boolean value
	 */
	private static boolean isValidPhone(String phone) {
		phone = phone.replaceAll("-", "");
		Pattern p = Pattern.compile("(05)?[0-9]{8}");

		Matcher m = p.matcher(phone);
		return (m.find() && m.group().equals(phone));
	}

	/**
	 * Check if a name is valid- has only letters.
	 * 
	 * @param name the name needed to be checked
	 * @return the result, as boolean value.
	 */
	private static boolean isValidName(String name) {
		return name.matches("[a-zA-Z]{2,}");
	}

	/**
	 * Check if this username is a valid username- has only letters and numbers.
	 * 
	 * @param name the name needed to be checked
	 * @return The result, a boolean value.
	 */
	private static boolean isValidUsername(String name) {
		return name.matches("[a-zA-Z0-9]{2,}");
	}

	/**
	 * Check if this credit card is valid.
	 * @param cardNum the card needed to be checked.
	 * @return The result, a boolean value.
	 */
	private static boolean isValidCreditCard(String cardNum) {
		String passRegex = "[0-9]{16}";
		Pattern pat = Pattern.compile(passRegex);
		if (cardNum == null)
			return false;
		return pat.matcher(cardNum).matches();
	}

	/**
	 * Check if this CVV is valid.
	 * @param cvv the CVV needed to be checked
	 * @return The result, a boolean value.
	 */
	private static boolean isValidCVV(String cvv) {
		String passRegex = "[0-9]{3}";
		Pattern pat = Pattern.compile(passRegex);
		if (cvv == null)
			return false;
		return pat.matcher(cvv).matches();
	}
	
	/**
	 * @param year The year of the expires date.
	 * @param month THe moth of expires date.
	 * @return whether the date is legal.
	 */
	public static boolean isValidExpires(Integer year,Integer month) 
	{
		if(year==null || month==null)
			return false;
		
		LocalDate today=LocalDate.now();
		YearMonth yearMonth = YearMonth.of(year+2000, month);
		LocalDate exDate=LocalDate.of(year, month, yearMonth.lengthOfMonth());
		if(today.isAfter(exDate))
			return false;
		return true;
	}
	
	/**
	 * combine all tests to check result.
	 * 
	 * @param uname user name
	 * @param pass password
	 * @param fName first name
	 * @param lName last name
	 * @param eMail e mail
	 * @param phone phone number 
	 * @param cardNum credit card number
	 * @param year the year of expires date
	 * @param month the month of expires date
	 * @param cvv the cvv value.
	 * @return returns error type. Please use .getValue() in order to get the error massage.
	 */
	public static Res isValidCustomer(String uname, String pass, String fName, String lName, String eMail, String phone,
			String cardNum,Integer year,Integer month, String cvv) {
		if (!isValidUsername(uname))
			return Res.UName;
		if (!isValidPassword(pass))
			return Res.Pass;
		if (!isValidName(fName))
			return Res.FName;
		if (!isValidName(lName))
			return Res.LName;
		if (!isValidEmail(eMail))
			return Res.Email;
		if (!isValidPhone(phone))
			return Res.Phone;
		if (!isValidCreditCard(cardNum))
			return Res.CardNum;
		if(!isValidExpires(year,month))
			return Res.Expires;
		if (!isValidCVV(cvv))
			return Res.CVV;
		return Res.AllGood;
	}
	
	/**
	 * combine all tests to check result, without credit card checks.
	 * 
	 * @param uname user name
	 * @param pass password
	 * @param fName first name
	 * @param lName last name
	 * @param eMail e mail
	 * @param phone phone number 
	 * @return returns error type. Please use .getValue() in order to get the error massage.
	 */
	public static Res isValidUser(String uname, String pass, String fName, String lName, String eMail, String phone) {
		if (!isValidUsername(uname))
			return Res.UName;
		if (!isValidPassword(pass))
			return Res.Pass;
		if (!isValidName(fName))
			return Res.FName;
		if (!isValidName(lName))
			return Res.LName;
		if (!isValidEmail(eMail))
			return Res.Email;
		if (!isValidPhone(phone))
			return Res.Phone;
		return Res.AllGood;
	}

}
