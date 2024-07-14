package bankingManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
  private Connection connection;

  private Scanner scanner;

  public User(Connection connection, Scanner scanner){
    this.connection = connection;
    this.scanner = scanner;
  }

  public void register(){
    scanner.nextLine();
    System.out.print("Full Name: ");
    String full_name = scanner.nextLine();
    System.out.print("Email: ");
    String email = scanner.nextLine();
    System.out.print("Password: ");
    String password = scanner.nextLine();

    if(user_exist(email)){
      System.out.println("User already exists for this email address!");
      return;
    }
    String register_query = "INSERT INTO User(full_name, email, password) VALUES(?, ?, ?);";
     try{
      PreparedStatement ps = connection.prepareStatement(register_query);
      ps.setString(1, full_name);
      ps.setString(2, email);
      ps.setString(3, password);
      int rowsaffected = ps.executeUpdate();
      if(rowsaffected > 0){
        System.out.println("Registration Successfully!!!");
      }
      else{
        System.out.println("Registration failed!!!");
      }
     }catch(SQLException e){
      e.printStackTrace();
     }
  }

  public String login(){
    scanner.nextLine();
    System.out.print("Email: ");
    String email = scanner.nextLine();
    System.out.print("Password: ");
    String password = scanner.nextLine();
    String login_query = "SELECT * FROM User WHERE email = ? AND password = ?;";
    try{
      PreparedStatement ps = connection.prepareStatement(login_query);
      ps.setString(1, email);
      ps.setString(2, password);
      ResultSet rs = ps.executeQuery();
      if(rs.next()){
        return email;
      }
      else{
        return null;
      }
    }catch(SQLException e){
      e.printStackTrace();
    }
    return null;
  }
  public boolean user_exist(String email){
    String query = "SELECT * FROM User WHERE email = ?;";
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