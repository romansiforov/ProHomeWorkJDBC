package sample;

import java.sql.*;
import java.util.Scanner;

public class Main {
    // CREATE DATABASE mydb;
    static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/realty?serverTimezone=Europe/Kiev";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "";

    static Connection conn;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            try {
                // create connection
                conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
                initDB();

                while (true) {
                    System.out.println("1: add apartment");
                    System.out.println("2: delete apartment");
                    System.out.println("3: search apartment by square");
                    System.out.println("4: search by rooms amount");
                    System.out.println("5: search by price less THAN");
                    System.out.println("6: search by district");
                    System.out.println("7: get all apartments");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            addApartment(sc);
                            break;
                        case "2":
                            deleteApartment(sc);
                            break;
                        case "3":
                            viewApartmentsBySquare(sc);
                            break;
                        case "4":
                            viewApartmentsByRoomAmount(sc);
                            break;
                        case "5":
                            viewApartmentsByPriceLessThan(sc);
                            break;
                        case "6":
                            viewApartmentsByDistrict(sc);
                            break;
                        case "7":
                            viewApartments();
                            break;
                        default:
                            return;
                    }
                }
            } finally {
                sc.close();
                if (conn != null) conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return;
        }
    }

    private static void initDB() throws SQLException {
        Statement st = conn.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS apartments");
            st.execute("CREATE TABLE apartments " +
                    "(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "district VARCHAR(255) NOT NULL, " +
                    "address VARCHAR(255) DEFAULT NULL, " +
                    "square DOUBLE NOT NULL, " +
                    "room_amount INT NOT NULL, " +
                    "price INT NOT NULL, " +
                    "currency VARCHAR(255) DEFAULT NULL)");
        } finally {
            st.close();
        }
    }

    private static void addApartment(Scanner sc) throws SQLException {
        System.out.print("Enter district name: ");
        String district = sc.nextLine();
        System.out.print("Enter address: ");
        String address = sc.nextLine();
        System.out.print("Enter apartment square: ");
        Double square = Double.parseDouble(sc.nextLine());;
        System.out.print("Enter rooms amount: ");
        Integer room_amount = Integer.parseInt(sc.nextLine());
        System.out.print("Enter price: ");
        Integer price = Integer.parseInt(sc.nextLine());;

        System.out.print("Enter currency symbol or name : ");
        String currency = sc.nextLine();

        PreparedStatement ps = conn.prepareStatement("INSERT INTO apartments (district, address, square, room_amount, price, currency) VALUES(?, ?, ?, ?, ?, ?)");
        try {
            ps.setString(1, district);
            ps.setString(2, address);
            ps.setDouble(3, square);
            ps.setInt(4, room_amount);
            ps.setInt(5, price);
            ps.setString(6, currency);
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE
        } finally {
            ps.close();
        }
    }

    private static void deleteApartment(Scanner sc) throws SQLException {
        System.out.print("Enter apartment id: ");
        Integer id = Integer.parseInt(sc.nextLine());

        PreparedStatement ps = conn.prepareStatement("DELETE FROM apartments WHERE id = ?");
        try {
            ps.setInt(1, id);
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE
        } finally {
            ps.close();
        }
    }

    /*

   -> 1 2 3
      -----
      -----
      -----
      !----
     */

    private static void viewApartments() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM apartments");
        try {
            // table of data representing a database result set,
            // ps.setFetchSize(100);
            ResultSet rs = ps.executeQuery();

            try {
                // can be used to get information about the types and properties of the columns in a ResultSet object
                ResultSetMetaData md = rs.getMetaData();

                for (int i = 1; i <= md.getColumnCount(); i++)
                    System.out.print(md.getColumnName(i) + "\t\t");
                System.out.println();

                while (rs.next()) {
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        System.out.print(rs.getString(i) + "\t\t");
                    }
                    System.out.println();
                }
            } finally {
                rs.close(); // rs can't be null according to the docs
            }
        } finally {
            ps.close();
        }
    }

    private static void viewApartmentsByRoomAmount(Scanner sc) throws SQLException {
        System.out.print("Enter rooms amount: ");
        Integer id = Integer.parseInt(sc.nextLine());
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM apartments where room_amount = ?");

        try {
            // table of data representing a database result set,
            // ps.setFetchSize(100);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            try {
                // can be used to get information about the types and properties of the columns in a ResultSet object
                ResultSetMetaData md = rs.getMetaData();

                for (int i = 1; i <= md.getColumnCount(); i++)
                    System.out.print(md.getColumnName(i) + "\t\t");
                System.out.println();

                while (rs.next()) {
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        System.out.print(rs.getString(i) + "\t\t");
                    }
                    System.out.println();
                }
            } finally {
                rs.close(); // rs can't be null according to the docs
            }
        } finally {
            ps.close();
        }
    }

    private static void viewApartmentsBySquare(Scanner sc) throws SQLException {
        System.out.print("Enter square FROM : ");
        Double from = Double.parseDouble(sc.nextLine());

        System.out.print("Enter square TO : ");
        Double to = Double.parseDouble(sc.nextLine());
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM apartments where square between ? and ?");

        try {
            // table of data representing a database result set,
            // ps.setFetchSize(100);
            ps.setDouble(1, from);
            ps.setDouble(2, to);
            ResultSet rs = ps.executeQuery();

            try {
                // can be used to get information about the types and properties of the columns in a ResultSet object
                ResultSetMetaData md = rs.getMetaData();

                for (int i = 1; i <= md.getColumnCount(); i++)
                    System.out.print(md.getColumnName(i) + "\t\t");
                System.out.println();

                while (rs.next()) {
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        System.out.print(rs.getString(i) + "\t\t");
                    }
                    System.out.println();
                }
            } finally {
                rs.close(); // rs can't be null according to the docs
            }
        } finally {
            ps.close();
        }
    }

    private static void viewApartmentsByPriceLessThan(Scanner sc) throws SQLException {
        System.out.print("Enter apartment price less THAN : ");
        Integer lessThan = Integer.parseInt(sc.nextLine());

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM apartments where price < ?");

        try {
            // table of data representing a database result set,
            // ps.setFetchSize(100);
            ps.setDouble(1, lessThan);
            ResultSet rs = ps.executeQuery();

            try {
                // can be used to get information about the types and properties of the columns in a ResultSet object
                ResultSetMetaData md = rs.getMetaData();

                for (int i = 1; i <= md.getColumnCount(); i++)
                    System.out.print(md.getColumnName(i) + "\t\t");
                System.out.println();

                while (rs.next()) {
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        System.out.print(rs.getString(i) + "\t\t");
                    }
                    System.out.println();
                }
            } finally {
                rs.close(); // rs can't be null according to the docs
            }
        } finally {
            ps.close();
        }
    }

    private static void viewApartmentsByDistrict(Scanner sc) throws SQLException {
        System.out.print("Enter district where you want to look for apartment : ");
        String district = sc.nextLine();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM apartments where district = ?");

        try {
            // table of data representing a database result set,
            // ps.setFetchSize(100);
            ps.setString(1, district);
            ResultSet rs = ps.executeQuery();

            try {
                // can be used to get information about the types and properties of the columns in a ResultSet object
                ResultSetMetaData md = rs.getMetaData();

                for (int i = 1; i <= md.getColumnCount(); i++)
                    System.out.print(md.getColumnName(i) + "\t\t");
                System.out.println();

                while (rs.next()) {
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        System.out.print(rs.getString(i) + "\t\t");
                    }
                    System.out.println();
                }
            } finally {
                rs.close(); // rs can't be null according to the docs
            }
        } finally {
            ps.close();
        }
    }
}
