package vending_machine;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class Machine {

		
	public static void main(String[] args) {
		
		ArrayList<Drink> items = new ArrayList<Drink>();
		Map<String, Drink> itemMap = new HashMap<String, Drink>();
		Map<String, Integer> acceptedCoins = new HashMap<String, Integer>();
		Map<String, Integer> machineCase = new HashMap<String, Integer>();
		Map<String, Integer> sales = new HashMap<String, Integer>();
		String password = "vendmach";
		
		try {
			
			Scanner prices = new Scanner(new File("Prices.txt"));
			
			while(prices.hasNextLine()){
				
				String line = prices.nextLine();
				String product = line.split(",")[0];
				int count = Integer.parseInt(line.split(",")[1]);
				String cost = line.split(",")[2];
				Drink d = new Drink(product, cost, count);
				items.add(d);
				itemMap.put(product, d);
			}
			
			prices.close();
			
			Scanner coins = new Scanner(new File("Coins.txt"));
			String coin = coins.nextLine();
			coins.close();
			
			for(String c : coin.split(",")){
				
				if (c.split("\\$")[0].equals("2")){
					acceptedCoins.put(c, 200);
					machineCase.put(c, 10);
				}
				else if (c.split("\\$")[0].equals("1")){
					acceptedCoins.put(c, 100);
					machineCase.put(c, 10);
				}
				else{
					acceptedCoins.put(c, Integer.parseInt(c.split("c")[0]));
					machineCase.put(c, 10);
				}
			}
			
			Scanner input = new Scanner(System.in);
			Scanner optionScan = new Scanner(System.in);
			Scanner coinScan = new Scanner(System.in);
			
			System.out.println("Welcome !");
			String menu = "\n**************************\n1) Get Drink \n2) Maintenance \n3) Exit";

			double current = 0;
			boolean machine = true;
			
			while (machine) {
				
				System.out.println(menu);
				System.out.print("\nPlease choose an option ... ");
				String option = input.nextLine();
				
				if (option.equals("1")){
					
					System.out.println("\nDrink" + "\t\t" + "Price" + "\t\t"+ "Stock");
					System.out.println("--------------------------------------");
					
					for(Drink d : items){
						if (d.getCount() != 0)
							System.out.println(d.getName() + "\t\t" + d.getCost() + "\t\t" + d.getCount());
					}
					
					boolean finish = false;
					
					if (current == 0){
						System.out.print("\nPlease insert coin ..	");
					}
					
					else{
						finish = true;
					}
						
					while (!finish){	// inserting
						
						String coinInput = coinScan.nextLine();
						current += (double) acceptedCoins.get(coinInput) / 100;
						machineCase.put(coinInput, machineCase.get(coinInput) + 1);
						
						System.out.println("------------\nBalance : " + current + "$");
						System.out.println("\n1) Insert more\n2) OK");
						
						System.out.print(">> ");
						String drinkOption = optionScan.nextLine();
						
						if (drinkOption.equals("1")){
							System.out.print("\nPlease insert coin ..	");
						}
						else if (drinkOption.equals("2")){
							finish = true;
						}
					}
					
					System.out.print("\nPlease choose a drink .. ");
					Scanner drinkScan = new Scanner(System.in);
					String selected = drinkScan.nextLine();
					
					Drink d = itemMap.get(selected);
					
					System.out.println("------------\nBalance : " + current + "$" + 
							" | Drink : " + d.getName() + " | Cost : " + d.getCost());
					
					double itemCost = 0;
					
					if (d.getCost().contains("$"))
						itemCost = Double.parseDouble(d.getCost().split("\\$")[0]);
					else
						itemCost = (double) Double.parseDouble(d.getCost().split("c")[0]) / 100;

					System.out.println(d.getName() + " is vending..\n------------");
					d.setCount(d.getCount() - 1);
					
					if (sales.containsKey(d.getName()))
						sales.put(d.getName(), sales.get(d.getName()) + 1);
					else
						sales.put(d.getName(), 1);
					
					System.out.print("\nWould you like to buy something else? (y/n): ");
					Scanner cscan = new Scanner(System.in);
					String choice = cscan.nextLine();
					
					if (choice.equals("n")){
						current = calculateRefund(current, itemCost);
					}
					else {
						current -= itemCost;
					}
					
				}
				
				else if (option.equals("2")){
					
					System.out.print("Please enter your password >> ");
					Scanner scan = new Scanner(System.in);
					String pass = scan.nextLine();
					while (!pass.equals(password)){
						System.out.println("Invalid Password !");
						System.out.print(">> ");
						pass = scan.nextLine();
					}
					
					boolean mt = true;
					double totalCashWithdraw = 0;
					
					while (mt){
						
						System.out.println("\n1) Open the Case \n2) Close the Case \n3) Sales");
						System.out.print("\nPlease choose an option >> ");
						String maintenanceOpt = scan.nextLine();
						
						if (maintenanceOpt.equals("1")) {
							// cash
							System.out.print("Please enter the amount .. ");
							String amount = scan.nextLine();
							
							System.out.println("\nTotal Balance of Case :: " + 
									Double.toString(findCaseBalance(machineCase, acceptedCoins)) + "$");
						
							// check case
							for (String m : amount.split("\\+")){
								if (m.contains("$")){
									
									String cash = m.split("\\$")[0] + "$";
									int num = Integer.parseInt(m.split("\\$")[1]);
									
									if (machineCase.get(cash) == 0 || num > machineCase.get(cash)){
										System.out.println("Not available :(");
									}
									else {
										machineCase.put(cash, machineCase.get(cash) - 1);
										totalCashWithdraw += (num * acceptedCoins.get(cash));
									}
								}
								else if (m.contains("c")){
									
									String cash = m.split("c")[0] + "c";
									int num = Integer.parseInt(m.split("c")[1]);
									
									if (machineCase.get(cash) == 0 || num > machineCase.get(cash)){
										System.out.println("Not available :(");
									}
									else {
										machineCase.put(cash, machineCase.get(cash) - 1);
										totalCashWithdraw += (num * acceptedCoins.get(cash));
									}
								}
							}
							
							System.out.println("-" + Double.toString(totalCashWithdraw / 100) + "$");
							System.out.println("------------");
						}
						
						else if (maintenanceOpt.equals("2")){
							
							System.out.println("\nTotal Balance of Case :: " + 
									Double.toString(findCaseBalance(machineCase, acceptedCoins)) + "$");
							
							System.out.println("Total withdraw : " + Double.toString(totalCashWithdraw / 100) + "$");
						}
						
						else if (maintenanceOpt.equals("3")) {
							
							System.out.println("\nDrink" + "\t\t" + "Sales");
							System.out.println("-----------------------");
							
							for (String item : sales.keySet()){
								System.out.println(item + "\t\t" + sales.get(item));
							}
						}
						
						else 
							mt = false;
						
					}
					
				}
				
				else {
					machine = false;
					System.out.println("Thank you, Have a nice day !");
					System.exit(0);
				}
				
			}
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}


	}
	
	public static double findCaseBalance(Map<String, Integer> mCase, Map<String, Integer> acc){
		
		int total = 0;
		
		for (String c : mCase.keySet())
			total += mCase.get(c) * acc.get(c);
		
		return (double) total / 100;
		
	}
	
	public static double calculateRefund(double balance, double itemCost){
		
		System.out.println("\nRefund >> " + (double) ((int) (balance * 100) - (int) (itemCost * 100)) / 100 + "$\n");
		
		int change = ((int) (balance *= 100)) - (int) (itemCost *= 100);
		
		Map<String, Integer> changeCoins = new HashMap<String, Integer>();
		
		if(change > 0) {
			int twoDollar = (int)Math.floor(change / 200);
			changeCoins.put("2$", twoDollar);
	        change %= 200;
	    }
		if(change > 0) {
			int oneDollar = (int)Math.floor(change / 100);
			changeCoins.put("1$", oneDollar);
	        change %= 100;
	    }
		if(change > 0) {
			int fifty = (int)Math.floor(change / 50);
			changeCoins.put("50c", fifty);
	        change %= 50;
	    }
		if(change > 0) {
			int twentyfive = (int)Math.floor(change / 25);
			changeCoins.put("25c", twentyfive);
	        change %= 25;
	    }
		if(change > 0) {
			int ten = (int)Math.floor(change / 10);
			changeCoins.put("10c", ten);
	        change %= 10;
	    }
		if(change > 0) {
			int five = (int)Math.floor(change / 5);
			changeCoins.put("5c", five);
	        change %= 5;
	    }
		if(change > 0) {
			int one = (int)Math.floor(change / 1);
			changeCoins.put("1c", one);
	    }
		
		for (String s : changeCoins.keySet()){
			if (changeCoins.get(s) != 0)
				System.out.print(changeCoins.get(s) + " x " + s + ", ");
		}
		System.out.println();
		
		return balance - itemCost;
	}

}
