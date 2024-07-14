package bankingManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Statement;

public class Accounts {
  private Connection connection;

  private Scanner scanner;

  public Accounts(Connection connection, Scanner scanner){
    this.connection = connection;
    this.scanner = scanner;
  }
  public long open_account(String email){
    if(!account_exist(email)){
      String open_account_query = "INSERT INTO accounts(account_number,full_name,email,balance,security_pin) VALUES(?,?,?,?,?);";
      scanner.nextLine();
      System.out.print("Enter Full Name: ");
      String name = scanner.nextLine();
      System.out.print("Enter Initial Amount: ");
      double balance = scanner.nextDouble();
      scanner.nextLine();
      System.out.print("Enter Security Pin: ");
      String security_pin = scanner.nextLine();
      try{
        long account_number = generateAccountNumber();
        PreparedStatement ps = connection.prepareStatement(open_account_query);
        ps.setLong(1, account_number);
        ps.setString(2, name);
        ps.setString(3,email);
        ps.setDouble(4, balance);
        ps.setString(5,security_pin);
        int rowsaffected = ps.executeUpdate();
        if(rowsaffected > 0){
          return account_number;
        }
        else{
          throw new RuntimeException("Account Creation Failed!!!");
        }
      }catch(SQLException e){
        e.printStackTrace();
      }
    }
    throw new RuntimeException("Account already exists!!!");
  }

  public long getAccount_number(String email){
    String query = "SELECT account_number FROM accounts WHERE email = ?;";
    try{
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setString(1,email);
      ResultSet rs = statement.executeQuery();
      if(rs.next()){
        return rs.getLong("account_number");
      }
    }catch(SQLException e){
      e.printStackTrace();
    }
    throw new RuntimeException("Account number doesn't exist!");
  }

  public long generateAccountNumber(){
    try{
      Statement statement = connection.createStatement();
      String query = "SELECT account_number FROM accounts ORDER BY account_number DESC Limit 1;";
      ResultSet rs = statement.executeQuery(query);
      if(rs.next()){
        long last_account_number = rs.getLong("account_number");
        return last_account_number+1;
      }
      else{
        return 1000100;
      }
    }catch(SQLException e){
      e.printStackTrace();
    }
    return 1000100;
  }

  
  public boolean account_exist(String email){
    String query = "SELECT * FROM accounts WHERE email = ?;";
    try{
      PreparedStatement ps = connection.prepareStatement(query);
      ps.setString(1, email);
      ResultSet rs = ps.executeQuery();
      if(rs.next()){
        return true;
      }
      else{
        return false;
      }
    }catch(SQLException e){
      e.printStackTrace();
  }
  return false;
  }
}