package com.techelevator;

public class Item {

	/******************************************************
	 * Variables
	 ********************************************************/

	private String slot;
	private String itemName;
	private double itemPrice;
	private int itemInventory;
	private String itemType;
	private String yumMessage;

	/*********************************************************
	 * Constructors
	 ********************************************************/

	public Item() {

	}

	public Item(String slot, String itemName, double itemPrice, int itemInventory, String itemType, String yumMessage) {
		this.slot = slot;
		this.itemName = itemName;
		this.itemPrice = itemPrice;
		this.itemInventory = itemInventory;
		this.itemType = itemType;
		this.yumMessage = yumMessage;
	}

	/*******************************************************
	 * Getters
	 *******************************************************/
	public String getSlot() {
		return slot;
	}

	public String getItemName() {
		return itemName;
	}

	public double getItemPrice() {
		return itemPrice;
	}

	public int getItemInventory() {
		return itemInventory;
	}

	public String getItemType() {
		return itemType;
	}

	public String getYumMessage() {
		return yumMessage;
	}

	/*******************************************************
	 * Setters
	 *******************************************************/
	public void setSlot(String slot) {
		this.slot = slot;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public void setItemPrice(double itemPrice) {
		this.itemPrice = itemPrice;
	}

	public void setItemInventory(int itemInventory) {
		this.itemInventory = itemInventory;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String setYumMessage(String itemType) {
		switch (itemType) {
		case "Candy":
			return "Chew Chew, Yum!";

		case "Chip":
			return "Crunch Crunch, Yum!";

		case "Drink":
			return "Glug Glug, Yum!";

		case "Gum":
			return "Munch Munch, Yum!";

		default:
			return "What is this Strange new Food?, Chomp Chmop, Yum!";
		}
	}

	/*******************************************************
	 * Overrides
	 *******************************************************/

	public String toString() {
		String formatPrice = String.format("%.2f", itemPrice);
		if (itemInventory == 0) {
			return slot + "| " + itemName + "| $" + formatPrice + "| SOLD OUT";
		}

		return slot + "| " + itemName + "| $" + formatPrice + "| " + itemInventory + " in stock";
	}
}
