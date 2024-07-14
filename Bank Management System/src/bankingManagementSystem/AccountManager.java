package bankingManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
   private Connection connection;

   private Scanner scanner;

  public AccountManager(Connection connection, Scanner scanner){
    this.connection = connection;
    this.scanner = scanner;
  }

  public void credit_money(Long account_number) throws SQLException{
    scanner.nextLine();
    System.out.print("Enter Amount: ");
    double amount = scanner.nextDouble();
    scanner.nextLine();
    System.out.print("Enter Security Pin: ");
    String security_pin = scanner.nextLine();
    try{
      connection.setAutoCommit(false);
      if(account_number != 0){
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?;");
      statement.setLong(1,account_number);
      statement.setString(2,security_pin);
      ResultSet rs = statement.executeQuery();

      if(rs.next()){
          String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?;";
          PreparedStatement ps = connection.prepareStatement(credit_query);
          ps.setDouble(1,amount);
          ps.setLong(2,account_number);
          int rowsaffected = ps.executeUpdate();
          if(rowsaffected > 0){
            System.out.println("Rs. " + amount + " credited succesfully!!!");
            connection.commit();
            connection.setAutoCommit(true);
            return;
          }
          else{
            System.out.println("Transaction Failed!!!");
            connection.rollback();
            connection.setAutoCommit(true);
          }
      }else{
        System.out.println("Incorrect Pin!!!");
      }
      }
    }catch(SQLException e){
      e.printStackTrace();
    }
    connection.setAutoCommit(true);
  }

  public void debit_money(Long account_number) throws SQLException{
    scanner.nextLine();
    System.out.print("Enter Amount: ");
    double amount = scanner.nextDouble();
    scanner.nextLine();
    System.out.print("Enter Security Pin: ");
    String security_pin = scanner.nextLine();
    try{
      connection.setAutoCommit(false);
      if(account_number != 0){
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?;");
      statement.setLong(1,account_number);
      statement.setString(2,security_pin);
      ResultSet rs = statement.executeQuery();

      if(rs.next()){
        double current_balance = rs.getDouble("account_number");
        if(amount<=current_balance){
          String debit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?;";
          PreparedStatement ps = connection.prepareStatement(debit_query);
          ps.setDouble(1,amount);
          ps.setLong(2,account_number);
          int rowsaffected = ps.executeUpdate();
          if(rowsaffected > 0){
            System.out.println("Rs. " + amount + " debited succesfully!!!");
            connection.commit();
            connection.setAutoCommit(true);
            return;
          }
          else{
            System.out.println("Transaction Failed!!!");
            connection.rollback();
            connection.setAutoCommit(true);
          }
        }else{
          System.out.println("Insufficient Balance!!!");
        }
      }else{
        System.out.println("Incorrect Pin!!!");
      }
      }
    }catch(SQLException e){
      e.printStackTrace();
    }
    connection.setAutoCommit(true);
  }

  public void transfer_money(Long sender_account_number) throws SQLException{
    scanner.nextLine();
    System.out.print("Enter Receiver Account Number: ");
    Long receiver_account_number = scanner.nextLong();
    System.out.print("Enter Amount: ");
    double amount = scanner.nextDouble();
    scanner.nextLine();
    System.out.print("Enter Security Pin: ");
    String security_pin = scanner.nextLine();
    try{
      connection.setAutoCommit(false);
      if(sender_account_number != 0 && receiver_account_number != 0){
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?;");
        ps.setLong(1, sender_account_number);
        ps.setString(2,security_pin);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
          Double current_balance = rs.getDouble("balance");
          if(current_balance >= amount){
            PreparedStatement debit_statement = connection.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE account_number = ?;");
            PreparedStatement credit_statement = connection.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE account_number = ?;");
            debit_statement.setDouble(1,amount);
            debit_statement.setLong(2,sender_account_number);
            credit_statement.setDouble(1,amount);
            debit_statement.setLong(2,receiver_account_number);

            int rowsaffected1 = debit_statement.executeUpdate();
            int rowsaffected2 = credit_statement.executeUpdate();
            if(rowsaffected1 > 0 && rowsaffected2 > 0){
              System.out.println("Transaction successful!!!");
              System.out.println("Rs. "+ amount+" transferred succesfully!!!");
              connection.commit();
              connection.setAutoCommit(true);
              return;
            }else{
              System.out.println("Transaction Failed!!!");
              connection.rollback();
              connection.setAutoCommit(true);
            }
          }else{
            System.out.println("Insufficient balance!!");
          }
        }else{
          System.out.println("Invalid Pin!!");
        }
      }else{
        System.out.println("Invalid Account Number!!!");
      }
    }catch(SQLException e){
      e.printStackTrace();
    }
    connection.setAutoCommit(true);
  }
  
  public void getBalance(long account_number){
    scanner.nextLine();
    System.out.println("Enter Security Pin: ");
    String security_pin = scanner.nextLine();
    try{
      PreparedStatement ps = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?;");
      ps.setLong(1,account_number);
      ps.setString(2, security_pin);
      ResultSet rs = ps.executeQuery();
      if(rs.next()){
        System.out.println("Balance: "+ rs.getDouble("balance"));
      }
      else{
        System.out.println("Invalid Pin!!!");
      }
    }catch(SQLException e){
      e.printStackTrace();
    }
  }
}
