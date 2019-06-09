package controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegCheck {

	public enum Res {
		UName("Illegal username"),
		Pass("Illegal password- " + "it must be at least 8 char long, " + "with lower case and upper case letters, "
				+ "at least one digit and at least one special character."),
		FName("Illegal first name"), LName("Illegal last name"), Email("Illegal Email"), Phone("Illegal phone number"),
		AllGood("All Good");

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
	 * @param pass
	 * @return whether the password is legal and safe
	 */
	private static boolean isValidPassword(String pass) {
		String passRegex = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
		/*
		 * (?=.*[0-9]) a digit must occur at least once (?=.*[a-z]) a lower case letter
		 * must occur at least once (?=.*[A-Z]) an upper case letter must occur at least
		 * once (?=.*[@#$%^&+=]) a special character must occur at least once (?=\\S+$)
		 * no whitespace allowed in the entire string .{8,} at least 8 characters
		 */

		Pattern pat = Pattern.compile(passRegex);
		if (pass == null)
			return false;
		return pat.matcher(pass).matches();
	}

	/**
	 * @param phone
	 * @return whether the phone number is legal
	 */
	private static boolean isValidPhone(String phone) {
		// The given argument to compile() method
		// is regular expression. With the help of
		// regular expression we can validate mobile
		// number.
		// 1) Begins with 0 or 91
		// 2) Then contains 7 or 8 or 9.
		// 3) Then contains 9 digits
		phone = phone.replaceAll("-", "");
		Pattern p = Pattern.compile("(05)?[0-9]{8}");

		// Pattern class contains matcher() method
		// to find matching between given number
		// and regular expression
		Matcher m = p.matcher(phone);
		return (m.find() && m.group().equals(phone));
	}

	/**
	 * @param name
	 * @return whether this is a valid name- only letters.
	 */
	private static boolean isValidName(String name) {
		return name.matches("[a-zA-Z]+");
	}
	
	/**
	 * @param name
	 * @return whether this is a valid username
	 */
	private static boolean isValidUsername(String name) {
		return name.matches("[a-zA-Z0-9]+");
	}

	/**
	 * combine all tests to check result. 
	 * @param uname
	 * @param pass
	 * @param fName
	 * @param lName
	 * @param eMail
	 * @param phone
	 * @return returns error type
	 */
	public static Res isValid(String uname, String pass, String fName, String lName, String eMail, String phone) {
		if(!isValidUsername(uname)) return Res.UName;
		if(!isValidPassword(pass)) return Res.Pass;
		if(!isValidName(fName)) return Res.FName;
		if(!isValidName(lName)) return Res.LName;
		if(!isValidEmail(eMail)) return Res.Email;
		if(!isValidPhone(phone)) return Res.Phone;
		return Res.AllGood;
	}

}
