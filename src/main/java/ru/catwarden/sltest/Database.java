package ru.catwarden.sltest;

import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Repository
public class Database {
    private Config config;
    private String dburl;
    private String dbuser;
    private String dbpassword;

    public Database(Config config) {
        this.config = config;
        this.dburl = config.getDatabaseUrl();
        this.dbuser = config.getDatabaseUser();
        this.dbpassword = config.getDatabasePassword();

        initializeDatabase();

    }

    public void initializeDatabase(){
        String query = "CREATE TABLE IF NOT EXISTS birthday ("+
                "id SERIAL PRIMARY KEY, " +
                "name VARCHAR(50) NOT NULL, " +
                "birthday DATE NOT NULL " +
                ");";

        try(Connection conn = connectToDatabase();
            PreparedStatement statement = conn.prepareStatement(query)){
            statement.executeUpdate();
        } catch (SQLException exception){
            exception.printStackTrace();
        }
    }

    public Connection connectToDatabase() throws SQLException {
        return DriverManager.getConnection(this.dburl, this.dbuser, this.dbpassword);

    }

    public List<Birthday> getAllBirthdays() {
        String query = "SELECT id, name, birthday FROM birthday";
        List<Birthday> list = new ArrayList<>();

        try(Connection conn = connectToDatabase();
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery()) {

            while(rs.next()){
                Birthday birthday = new Birthday();
                birthday.setId(rs.getInt("id"));
                birthday.setName(rs.getString("name"));
                birthday.setDate(rs.getDate("birthday"));
                list.add(birthday);
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return list;
    }

    public void setNewBirthday(String name, Date date){
        String query = "INSERT into birthday (name,birthday) VALUES(?,?)";


        try(Connection conn = connectToDatabase();
        PreparedStatement statement = conn.prepareStatement(query)
        ) { statement.setString(1, name);
            statement.setDate(2, date);
            statement.executeUpdate();

        } catch (SQLException exception){
            exception.printStackTrace();
        }


    }

    public void deleteBirthday(int id){
        String query = "DELETE from birthday WHERE id = ?";

        try(Connection conn = connectToDatabase();
        PreparedStatement statement = conn.prepareStatement(query)){
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch(SQLException exception){
            exception.printStackTrace();
        }

    }

    public void editBirthday(int id, Date date, String name){
        String query = "UPDATE birthday SET name = ?, birthday = ? WHERE id = ?";

        try(Connection conn = connectToDatabase();
        PreparedStatement statement = conn.prepareStatement(query)){
            statement.setString(1, name);
            statement.setDate(2, date);
            statement.setInt(3, id);
            statement.executeUpdate();
        } catch (SQLException exception){
            exception.printStackTrace();
        }
    }

    public List<Birthday> getTodayBirthdays(int month, int day){
        List<Birthday> list = new ArrayList<>();

        String query = "SELECT name, birthday FROM birthday WHERE EXTRACT(MONTH FROM birthday) = ? AND EXTRACT(DAY FROM birthday) = ?";

        try(Connection conn = connectToDatabase();
        PreparedStatement statement = conn.prepareStatement(query);){
            statement.setInt(1, month);
            statement.setInt(2, day);
            try(ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Birthday birthday = new Birthday();
                    birthday.setName(rs.getString("name"));
                    birthday.setDate(rs.getDate("birthday"));

                    list.add(birthday);
                }
            }

        } catch (SQLException exception){
            exception.printStackTrace();
        }
        return list;
    }

    public Birthday getBirthdayById(int id){
        String query = "SELECT * FROM birthday WHERE id = ?";
        Birthday birthday = null;

        try(Connection conn = connectToDatabase();
            PreparedStatement statement = conn.prepareStatement(query)){
            statement.setInt(1, id);
            try(ResultSet rs = statement.executeQuery()){
                if(rs.next()){
                    birthday = new Birthday();
                    birthday.setId(rs.getInt("id"));
                    birthday.setName(rs.getString("name"));
                    birthday.setDate(rs.getDate("birthday"));
                }
            }

        } catch (SQLException exception){
            exception.printStackTrace();
        }
        return birthday;
    }
}
