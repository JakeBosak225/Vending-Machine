package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**************************************************************************************************************************
*  This is your Vending Machine Command Line Interface (CLI) class
*
*  It is the main process for the Vending Machine
*
*  THIS is where most, if not all, of your Vending Machine interactions should be coded
*  
*  It is instantiated and invoked from the VendingMachineApp (main() application)
*  
*  Your code vending machine related code should be placed in here
***************************************************************************************************************************/
import com.techelevator.view.Menu; // Gain access to Menu class provided for the Capstone

public class VendingMachineCLI {

	// Main menu options defined as constants

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String MAIN_MENU_PRINT_SALES_REPORT = "Print Sales Report";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE,
			MAIN_MENU_OPTION_EXIT, MAIN_MENU_PRINT_SALES_REPORT };

	private static final String PURCHASE_MENU_OPTION_FEED_MONEY = "Feed Money";
	private static final String PURCHASE_MENU_OPTION_PURCHASE_ITEM = "Purchase Item";
	private static final String PURCHASE_MENU_OPTION_EXIT = "Finish Transaction";
	private static final String[] PURCHASE_MENU_OPTIONS = { PURCHASE_MENU_OPTION_FEED_MONEY,
			PURCHASE_MENU_OPTION_PURCHASE_ITEM, PURCHASE_MENU_OPTION_EXIT };

	private Menu vendingMenu; // Main Menu object to be used by an instance of this class
	private Menu purchaseMenu; // Purchase Menu object to be used by an instance of this class

	public VendingMachineCLI(Menu menu, Menu purchaseMenu) { // Constructor - user will pas a menu for this class to use
		this.vendingMenu = menu; // Make the Menu the user object passed, our Menu
		this.purchaseMenu = purchaseMenu;
	}

	/**************************************************************************************************************************
	 * VendingMachineCLI main processing loop
	 * 
	 * Display the main menu and process option chosen
	 *
	 * It is invoked from the VendingMachineApp program
	 *
	 * THIS is where most, if not all, of your Vending Machine objects and
	 * interactions should be coded
	 *
	 * Methods should be defined following run() method and invoked from it
	 * 
	 * @throws FileNotFoundException
	 *
	 ***************************************************************************************************************************/

	public void run() throws FileNotFoundException {
		VendingMachine vendingMachine = new VendingMachine();
		vendingMachine.setItemsInMachine(vendingMachine.setStartingInventory());
		System.out.println("Welcome to the Vend-O-Matic 8000");
		boolean shouldProcess = true; // Loop control variable

		while (shouldProcess) { // Loop until user indicates they want to exit

			String choice = (String) vendingMenu.getChoiceFromOptions(MAIN_MENU_OPTIONS); // Display menu and get choice

			switch (choice) { // Process based on user menu choice

			case MAIN_MENU_OPTION_DISPLAY_ITEMS:
				displayItems(vendingMachine); // invoke method to display items in Vending Machine
				break; // Exit switch statement

			case MAIN_MENU_OPTION_PURCHASE:
				purchaseItems(vendingMachine); // invoke method to purchase items from Vending Machine
				break; // Exit switch statement

			case MAIN_MENU_OPTION_EXIT:
				endMethodProcessing(vendingMachine); // Invoke method to perform end of method processing
				shouldProcess = false; // Set variable to end loop
				break; // Exit switch statement

			case MAIN_MENU_PRINT_SALES_REPORT:
				printSalesReport(vendingMachine);
				break;
			}
		}
		
		return; // End method and return to caller
	}

	/********************************************************************************************************
	 * Methods used to perform processing
	 ********************************************************************************************************/
	public void displayItems(VendingMachine vendingMachine) { // static attribute used as method is not associated with
																// specific object instance
		// Code to display items in Vending Machine
		for (Item item : vendingMachine.getItemsInMachine()) {
			System.out.println(item.toString());
		}
	}

	public void purchaseItems(VendingMachine vendingMachine) { // static attribute used as method is not associated with
																// specific object
		boolean continuePurchasing = true;
		while (continuePurchasing) {
			System.out.println();
			System.out.println("-----------------------------------------");
			String formatADouble = String.format("%.2f", vendingMachine.getAmountInMachine());
			System.out.println("Current Balance: $" + formatADouble);
			String choice = (String) purchaseMenu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);

			switch (choice) {
			case PURCHASE_MENU_OPTION_FEED_MONEY:
				vendingMachine.addFunds();
				break;
			case PURCHASE_MENU_OPTION_PURCHASE_ITEM:
				displayItems(vendingMachine);
				vendingMachine.handlePurchasing();
				break;
			case PURCHASE_MENU_OPTION_EXIT:
				vendingMachine.giveChange();
				continuePurchasing = false;
				break;
			default:
				System.out.println();
				System.out.println("Invalid Selection. Please choose again");
			}
		}
	}

	public void printSalesReport(VendingMachine vendingMachine) throws FileNotFoundException {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM_dd_yyyy_hh_mm_a");
		String formattedDateTime = now.format(formatter);

		File salesReport = new File("SalesReportFor" + formattedDateTime + ".txt");
		PrintWriter outFile = new PrintWriter(salesReport);
		for (Item item : vendingMachine.getItemsInMachine()) {
			int amountSold = 5 - item.getItemInventory();
			outFile.println(item.getItemName() + "| " + amountSold);
		}
		
		String fortmedAmountMade = String.format("%.2f", vendingMachine.getAmountMade());

		outFile.println();
		outFile.println("---------------------------");
		outFile.println("Total Sales: $" + fortmedAmountMade);
		outFile.close();

		System.out.println("Sales report has been printed!");
	}

	public void endMethodProcessing(VendingMachine vendingMachine) {
		vendingMachine.getOutFile().close();
	}
}
