import java.sql.*;

/**
 * Contoh sederhana JDBC + MySQL:
 * - Buat database 'contohdb'
 * - Buat tabel 'users'
 * - Masukkan beberapa baris
 * - Query dan cetak hasilnya
 *
 * Ganti DB_USER dan DB_PASSWORD sesuai environment Anda.
 */
public class MySQLExample {
    // Ganti sesuai environment
    private static final String DB_HOST = "localhost";
    private static final int DB_PORT = 3306;
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static void main(String[] args) {
        String serverUrl = String.format("jdbc:mysql://%s:%d/?serverTimezone=UTC&allowPublicKeyRetrieval=true", DB_HOST, DB_PORT);
        String dbName = "contohdb";
        String dbUrl = String.format("jdbc:mysql://%s:%d/%s?serverTimezone=UTC&allowPublicKeyRetrieval=true", DB_HOST, DB_PORT, dbName);

        try {
            // 1) Koneksi ke server MySQL tanpa memilih database untuk membuat database baru
            try (Connection conn = DriverManager.getConnection(serverUrl, DB_USER, DB_PASSWORD);
                 Statement stmt = conn.createStatement()) {
                // System.out.println("Terhubung ke server MySQL (tanpa DB).");

                // Buat database jika belum ada
                String createDbSql = "CREATE DATABASE IF NOT EXISTS " + dbName;
                stmt.executeUpdate(createDbSql);
                // System.out.println("Database '" + dbName + "' dibuat/terdapat.");
            }

            // 2) Koneksi ke database yang baru dibuat
            try (Connection conn = DriverManager.getConnection(dbUrl, DB_USER, DB_PASSWORD)) {
                // System.out.println("Terhubung ke database: " + dbName);

                // Matikan auto-commit kalau ingin transaksi eksplisit (opsional)
                conn.setAutoCommit(false);
                try (Statement stmt = conn.createStatement()) {
                    // Buat tabel users
                    String createTableSql = """
                        CREATE TABLE IF NOT EXISTS users (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            username VARCHAR(50) NOT NULL UNIQUE,
                            email VARCHAR(100),
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                        ) ENGINE=InnoDB;
                        """;
                    stmt.executeUpdate(createTableSql);
                    // System.out.println("Tabel 'users' dibuat/terdapat.");
                }

                // 3) Masukkan data menggunakan PreparedStatement (aman dari SQL injection)
                String insertSql = "INSERT INTO users(username, email) VALUES (?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                    // contoh baris 1
                    pstmt.setString(1, "rakasya");
                    pstmt.setString(2, "rakasya@example.com");
                    pstmt.executeUpdate();

                    // contoh baris 2
                    pstmt.setString(1, "andi");
                    pstmt.setString(2, "andi@example.com");
                    pstmt.executeUpdate();

                    // commit transaksi
                    conn.commit();
                    // System.out.println("Data berhasil dimasukkan (2 baris).");
                } catch (SQLException e) {
                    conn.rollback();
                    throw e;
                }

                // 4) Query data dan tampilkan
                String selectSql = "SELECT id, username, email, created_at FROM users ORDER BY id";
                try (PreparedStatement pstmt = conn.prepareStatement(selectSql);
                     ResultSet rs = pstmt.executeQuery()) {
                    System.out.println("\nData di tabel users:");
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String username = rs.getString("username");
                        String email = rs.getString("email");
                        Timestamp ts = rs.getTimestamp("created_at");
                        System.out.printf("id=%d, username=%s, email=%s, created_at=%s%n", id, username, email, ts);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Terjadi SQLException: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
