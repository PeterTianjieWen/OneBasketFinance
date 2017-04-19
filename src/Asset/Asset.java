package Asset;

import Info.DatabaseInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 2/1/17.
 */
public abstract class Asset{

    private String code;
    private char type;
    private String label;

    public Asset(String code, char type, String label) {
        this.code = code;
        this.type = type;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public char getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public abstract double getRisk();

    public abstract double getReturn(double value);

    public abstract double getValue(double value);

    public static List<Asset> getAssetList() {
        List<Asset> a = new ArrayList<Asset>();

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DatabaseInfo.url, DatabaseInfo.username, DatabaseInfo.password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        String query1 = "SELECT assetId, assetCode, assetType, label FROM Asset;";

        try {
            ps = conn.prepareStatement(query1);
            rs = ps.executeQuery();


            while (rs.next()) {
                Integer assetId = rs.getInt("assetId");
                String assetCode = rs.getString("assetCode");
                char assetType = (rs.getString("assetType").toCharArray())[0];
                String label = rs.getString("label");

                switch (assetType) {
                    case ('D'): {
                        ps2 = conn.prepareStatement("SELECT * FROM Deposit WHERE assetId = ?");
                        ps2.setInt(1, assetId);
                        rs2 = ps2.executeQuery();
                        double apr;
                        if (rs2.next()) {
                            apr = rs2.getDouble("apr");
                        } else {
                            throw new RuntimeException("Wrong data in database");
                        }
                        Deposit deposit = new Deposit(assetCode, assetType, label, apr);
                        a.add(deposit);
                        break;
                    }
                    case ('S'): {
                        ps2 = conn.prepareStatement("SELECT * FROM Stock WHERE assetId = ?");
                        ps2.setInt(1, assetId);
                        rs2 = ps2.executeQuery();
                        double betaMeasure;
                        String stockSymbol;
                        double sharePrice;
                        double quarterlyDividend;
                        double baseRateOfReturn;
                        if (rs2.next()) {
                            betaMeasure = rs2.getDouble("betaMeasure");
                            stockSymbol = rs2.getString("stockSymbol");
                            sharePrice = rs2.getDouble("sharePrice");
                            quarterlyDividend = rs2.getDouble("quarterlyDividend");
                            baseRateOfReturn = rs2.getDouble("baseRateOfReturn");
                        } else {
                            throw new RuntimeException("Wrong data of " + assetId);
                        }
                        Stock s = new Stock(assetCode, assetType, label, quarterlyDividend, baseRateOfReturn,
                                betaMeasure, stockSymbol, sharePrice);
                        a.add(s);
                        break;
                    }
                    case ('P'): {
                        ps2 = conn.prepareStatement("SELECT * FROM PrivateInvest WHERE assetId = ?");
                        ps2.setInt(1, assetId);
                        rs2 = ps2.executeQuery();
                        double omegaMeasure;
                        double totalValue;
                        double quarterlyDividend;
                        double baseRateOfReturn;
                        if (rs2.next()) {
                            omegaMeasure = rs2.getDouble("omegaMeasure");
                            totalValue = rs2.getDouble("totalValue");
                            quarterlyDividend = rs2.getDouble("quarterlyDividend");
                            baseRateOfReturn = rs2.getDouble("baseRateOfReturn");
                        } else {
                            throw new RuntimeException("Wrong data of " + assetId);
                        }
                        PrivateInvest pi = new PrivateInvest(assetCode, assetType, label, quarterlyDividend,
                                baseRateOfReturn, omegaMeasure, totalValue);
                        a.add(pi);
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            if (rs != null && !rs.isClosed())
                rs.close();
            if (ps != null && !ps.isClosed())
                ps.close();
            if (conn != null && !conn.isClosed())
                conn.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: fail in portfolio parse");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
            return a;


    }



    public static void main(String[] args){
        List<Asset> forTest = Asset.getAssetList();
        for (Asset a: forTest){
            System.out.println(a.toString());
        }
    }
}
