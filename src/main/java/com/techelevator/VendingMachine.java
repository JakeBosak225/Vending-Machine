package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class VendingMachine {

	/******************************************************
	 * Variables
	 *****************************************************/
	private double amountInMachine;
	private double amountMade;
	private List<Item> itemsInMachine;

	private Scanner theKeyboard = new Scanner(System.in);

	private File logFile;
	private PrintWriter outFile;

	/******************************************************
	 * Constructors
	 * 
	 * @throws FileNotFoundException
	 *****************************************************/
	public VendingMachine() throws FileNotFoundException {
		amountInMachine = 0.0;
		amountMade = 0.0;
		itemsInMachine = new ArrayList<>();
		logFile = new File("Log.txt");
		outFile = new PrintWriter(logFile);
	}

	/******************************************************
	 * Methods
	 *****************************************************/
	public List<Item> setStartingInventory() throws FileNotFoundException {
		List<Item> itemsToStock = new ArrayList<>();
		Item itemMethods = new Item();

		File inventoryFile = new File("vendingmachine.csv");
		Scanner fileScanner = new Scanner(inventoryFile);

		String currentLine = "";
		while (fileScanner.hasNextLine()) {
			currentLine = fileScanner.nextLine();
			String[] lineSplit = currentLine.split("\\|");

			Item itemToAdd = new Item(lineSplit[0], lineSplit[1], Double.parseDouble(lineSplit[2]), 5, lineSplit[3],
					itemMethods.setYumMessage(lineSplit[3]));

			itemsToStock.add(itemToAdd);
		}
		fileScanner.close();
		return itemsToStock;
	}

	public void addFunds() {
		System.out.println();
		System.out.println("Please enter an amount in $1, $2, $5, or $10");
		System.out.print("Enter >>> $");
		String amountToAdd = theKeyboard.nextLine();
		switch (amountToAdd) {
		case "1":
		case "2":
		case "5":
		case "10":
			double amountBeforeAdding = amountInMachine;
			amountInMachine += Double.parseDouble(amountToAdd);
			System.out.println("$" + amountToAdd + " added to machine");
			addToLogFile("FEED MONEY", amountBeforeAdding, amountInMachine);
			break;
		default:
			System.out.println();
			System.out.println("Invalid amount, Please try again.");
			break;
		}

	}

	public void giveChange() {
		double amountBeforeGivingChange = amountInMachine;
		
		Map<String, Integer> changeReturned = new HashMap<>();
		changeReturned.put("Quarter(s)", 0);
		changeReturned.put("Dime(s)", 0);
		changeReturned.put("Nickel(s)", 0);
		while (amountInMachine > 0) {
			String formatADouble = String.format("%.2f", amountInMachine);
			amountInMachine = Double.parseDouble(formatADouble);

			if (amountInMachine >= .25) {
				amountInMachine -= .25;
				changeReturned.put("Quarter(s)", changeReturned.get("Quarter(s)") + 1);
			} else if (amountInMachine >= .10) {
				amountInMachine -= .10;
				changeReturned.put("Dime(s)", changeReturned.get("Dime(s)") + 1);
			} else if (amountInMachine >= .05) {
				amountInMachine -= .05;
				changeReturned.put("Nickel(s)", changeReturned.get("Nickel(s)") + 1);
			}
		}

		Set<String> keys = changeReturned.keySet();
		for (String key : keys) {
			if (changeReturned.get(key) != 0) {
				System.out.println();
				System.out.println("-------------------------------");
				System.out.println(key + ": " + changeReturned.get(key));
				System.out.println("-------------------------------");

			}
		}
		
		addToLogFile("GIVE CHANGE", amountBeforeGivingChange, amountInMachine);

	}

	public void handlePurchasing() {
		System.out.println();
		System.out.println("-------------------------------");
		System.out.print("Enter the slot you would like to buy: ");
		String userInput = theKeyboard.nextLine();
		userInput = userInput.replaceAll("\\s", "");
		boolean isValidSlot = false;

		for (Item item : itemsInMachine) {
			if (item.getSlot().equalsIgnoreCase(userInput)) {
				isValidSlot = true;

				if (item.getItemInventory() == 0) {
					System.out.println("Sorry, that item is out of stock! Please make a new Selection");
					break;
				}

				if (amountInMachine < item.getItemPrice()) {
					System.out.println(
							"Sorry, not enough money to purchase! " + "Please make a new Selection or add more funds!");
					break;
				}

				
				item.setItemInventory(item.getItemInventory() - 1);
				double amountBeforePurchase = amountInMachine;
				amountInMachine -= item.getItemPrice();
				amountMade += item.getItemPrice();
				String formatPrice = String.format("%.2f", item.getItemPrice());
				String formatAmount = String.format("%.2f", amountInMachine);
				System.out.println();
				System.out.println("---------------------------");
				System.out.println("Dispensing your " + item.getItemName() + " for $" + formatPrice + 
						" Remaining Balance $" + formatAmount);
				System.out.println(item.getYumMessage());
				
				addToLogFile(item.getItemName() + " " + item.getSlot(), amountBeforePurchase, amountInMachine);
				break;
			}
		}
		if(!isValidSlot) {
			System.out.println("Not a valid Option. Please try again.");
		}
	}

	public void addToLogFile(String action, double startingAmount, double endingAmount) {
		String formatStartingAmount = String.format("%.2f", startingAmount);
		String formatEndingAmount = String.format("%.2f", endingAmount);
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
		String formattedDateTime = now.format(formatter);

		outFile.println(formattedDateTime + " " + action + " $" + formatStartingAmount + " $" + formatEndingAmount);
	}

	/******************************************************
	 * Getters
	 *****************************************************/
	public double getAmountInMachine() {
		return amountInMachine;
	}

	public double getAmountMade() {
		return amountMade;
	}

	public List<Item> getItemsInMachine() {
		return itemsInMachine;
	}

	public File getLogFile() {
		return logFile;
	}

	public PrintWriter getOutFile() {
		return outFile;
	}

	/******************************************************
	 * Setters
	 *****************************************************/
	public void setAmountInMachine(double amountInMachine) {
		this.amountInMachine = amountInMachine;
	}

	public void setItemsInMachine(List<Item> itemsInMachine) {
		this.itemsInMachine = itemsInMachine;
	}

}
