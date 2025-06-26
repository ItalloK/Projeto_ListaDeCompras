using System.Data.SQLite;
using System.IO;

namespace ListasAPI.Src.Data
{
    public class DbInitializer
    {
        private static readonly string dataDirectory = Path.Combine(AppContext.BaseDirectory, "data");

        public static string UsuariosDbPath => Path.Combine(dataDirectory, "users.db");
        public static string UsuariosConnectionString => $"Data Source={UsuariosDbPath}";

        public static void Initialize()
        {
            try
            {
                if (!Directory.Exists(dataDirectory))
                    Directory.CreateDirectory(dataDirectory); // Cria a pasta /data se ainda não existir

                InitUsersDb();
            }
            catch (Exception ex)
            {
                Console.WriteLine($"[DB INIT ERROR] Failed to initialize databases: {ex.Message}");
                return;
            }
        }

        private static void InitUsersDb()
        {
            if (!File.Exists(UsuariosDbPath))
            {
                SQLiteConnection.CreateFile(UsuariosDbPath);
                using var connection = new SQLiteConnection(UsuariosConnectionString);
                connection.Open();
                var createUsuarios = new SQLiteCommand("CREATE TABLE Users (Id INTEGER PRIMARY KEY AUTOINCREMENT, Nick TEXT UNIQUE, Email TEXT UNIQUE, Password TEXT NOT NULL, Stars INTEGER DEFAULT 0, Coins INTEGER DEFAULT 0)", connection);
                createUsuarios.ExecuteNonQuery();

                var createRefreshTokens = new SQLiteCommand(@"
                                                            CREATE TABLE IF NOT EXISTS RefreshTokens (
                                                                Id INTEGER PRIMARY KEY AUTOINCREMENT,
                                                                UserNick TEXT NOT NULL,
                                                                Token TEXT NOT NULL,
                                                                ExpiresAt TEXT NOT NULL,
                                                                IsRevoked INTEGER DEFAULT 0,
                                                                FOREIGN KEY(UserNick) REFERENCES Users(Nick)
                                                        )", connection);
                createRefreshTokens.ExecuteNonQuery();
                Console.WriteLine("[DB INIT] Users table created sucessfull.");
            }
        }
    }
}
