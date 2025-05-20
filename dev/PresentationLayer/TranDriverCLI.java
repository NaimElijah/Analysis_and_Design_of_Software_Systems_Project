package PresentationLayer;

//import ServiceLayer.TranEmployeeService;
import ServiceLayer.EmployeeService;
import ServiceLayer.TransportService;

import java.util.Scanner;

public class TranDriverCLI {
    private MainTranSysCLI main;
    private TransportService tran_s;
    private Scanner scanner;

    public TranDriverCLI(MainTranSysCLI m, TransportService trs, Scanner sc) {
        this.main = m;
        this.tran_s = trs;
        this.scanner = sc;
    }


    void driverMainMenu(long id){
        System.out.println("\n           --------    Driver Menu    -------");
        System.out.println("(1)  View All Transports Related to me (all Statuses)");
        System.out.println("(2)  Edit an Item's Condition in a Transport that I'm a Driver in");
        System.out.println("(3)  Disconnect");
        System.out.println();
        System.out.println("   Select Action:\n");

        String choice = scanner.nextLine();
        if(choice.equals("1")){
            System.out.println("All Transports Related to me: ");
            System.out.println(this.tran_s.showTransportsOfDriver(id));
            System.out.println();

        }else if(choice.equals("2")){
            editItemsConditionInMyTransport(id);

        } else if (choice.equals("3")) {
            System.out.println("\nGoing Back to Main Program Authentication Screen.\n");
            this.main.idAuthAccess();
        } else {
            System.out.println("\n  --->  Please enter a number between the menu's margins  <---\n");
        }
        driverMainMenu(id);
    }



    private void editItemsConditionInMyTransport(long id){
        System.out.println("Ok, let's Edit an Item's Condition in a Transport that You're a Driver in:");
        System.out.println("Enter Items Document ID, that that Item is in:");
        int itemsDoc_id = this.scanner.nextInt();
        scanner.nextLine(); // consume the leftover newline

        // check if the itemsDoc_id is really in a Transport he is involved in
        String resCheckIfHeIsInvolved = this.tran_s.checkIfDriverDrivesThisItemsDoc(id, itemsDoc_id);
        if (!resCheckIfHeIsInvolved.equals("Yes")){
            System.out.println("You are not involved in a Transport that handles the Items Document with that ID, therefore, you can't edit in that Items Document(ID).\n");
            System.out.println("Returning to Driver Menu...");
            return;
        }

        System.out.println("Enter Item Name:");
        String item_name = this.scanner.nextLine();
        System.out.println("Enter Item Weight:");
        double item_weight = this.scanner.nextDouble();
        System.out.println("Enter the amount of Items of that kind, that you'd like to change their condition:");
        int item_amount_to_change = this.scanner.nextInt();
        scanner.nextLine(); // consume the leftover newline
        System.out.println("Enter the New Item Condition you want to set: ( 'Good' / 'Bad'(or any other String) )");
        boolean new_item_condition = this.scanner.nextLine().equals("Good");

        String res = this.tran_s.setItemCond(itemsDoc_id, item_name, item_weight, item_amount_to_change, new_item_condition);
        if(res.equals("Success")){
            System.out.println("Successfully Changed Item's Condition.\n");
        } else if(res.equals("Exception")){
            System.out.println("Failed to Change Item's Condition due to technical machine error\n");
        }else { System.out.println(res + "\n"); }

        System.out.println();
    }





}
