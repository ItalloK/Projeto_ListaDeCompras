using System.Data.SQLite;
using System.IO;

namespace ListasAPI.Src.Data
{
    public class DbInitializer
    {
        private static readonly string dataDirectory = Path.Combine(AppContext.BaseDirectory, "data");

        public static string UsuariosDbPath => Path.Combine(dataDirectory, "BancoDeDados.db");
        public static string UsuariosConnectionString => $"Data Source={UsuariosDbPath}";

        public static void Initialize()
        {
            try
            {
                if (!Directory.Exists(dataDirectory))
                    Directory.CreateDirectory(dataDirectory); // Cria a pasta /data se ainda não existir

                InitUsersDb();
                InitListsDb();
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
                SQLiteConnection.CreateFile(UsuariosDbPath);

            using var connection = new SQLiteConnection(UsuariosConnectionString);
            connection.Open();

            if (!TableExists(connection, "Users"))
            {
                var createUsuarios = new SQLiteCommand(@"
                    CREATE TABLE Users (
                        Id INTEGER PRIMARY KEY AUTOINCREMENT,
                        Name TEXT,
                        Email TEXT UNIQUE,
                        Password TEXT NOT NULL,
                        Role TEXT NOT NULL DEFAULT 'Comum'
                    )", connection);
                createUsuarios.ExecuteNonQuery();
                Console.WriteLine("[DB INIT] Users table created successfully.");
            }
            else
            {
                Console.WriteLine("[DB INIT] Users table already exists.");
            }

            if (!TableExists(connection, "RefreshTokens"))
            {
                var createRefreshTokens = new SQLiteCommand(@"
                    CREATE TABLE RefreshTokens (
                        Id INTEGER PRIMARY KEY AUTOINCREMENT,
                        UserEmail TEXT NOT NULL,
                        Token TEXT NOT NULL,
                        ExpiresAt TEXT NOT NULL,
                        IsRevoked INTEGER DEFAULT 0,
                        FOREIGN KEY(UserEmail) REFERENCES Users(Email)
                    )", connection);
                createRefreshTokens.ExecuteNonQuery();
                Console.WriteLine("[DB INIT] RefreshTokens table created successfully.");
            }
            else
            {
                Console.WriteLine("[DB INIT] RefreshTokens table already exists.");
            }
        }

        private static void InitListsDb()
        {
            using var connection = new SQLiteConnection(UsuariosConnectionString);
            connection.Open();

            if (!TableExists(connection, "Lists"))
            {
                var createListsTable = new SQLiteCommand(@"
                    CREATE TABLE Lists (
                        Id INTEGER PRIMARY KEY AUTOINCREMENT,
                        Title TEXT NOT NULL,
                        Code TEXT NOT NULL UNIQUE,
                        CreatedAt TEXT NOT NULL,
                        ExpiresAt TEXT NOT NULL,
                        UserEmail TEXT NOT NULL,
                        FOREIGN KEY(UserEmail) REFERENCES Users(Email)
                    )", connection);
                createListsTable.ExecuteNonQuery();
                Console.WriteLine("[DB INIT] Lists table created successfully.");
            }
            else
            {
                Console.WriteLine("[DB INIT] Lists table already exists.");
            }

            if (!TableExists(connection, "Items"))
            {
                var createItemsTable = new SQLiteCommand(@"
                    CREATE TABLE Items (
                        Id INTEGER PRIMARY KEY AUTOINCREMENT,
                        ListId INTEGER NOT NULL,
                        Name TEXT NOT NULL,
                        Quantity INTEGER NOT NULL,
                        Value REAL NOT NULL,
                        FOREIGN KEY(ListId) REFERENCES Lists(Id) ON DELETE CASCADE
                    )", connection);
                createItemsTable.ExecuteNonQuery();
                Console.WriteLine("[DB INIT] Items table created successfully.");
            }
            else
            {
                Console.WriteLine("[DB INIT] Items table already exists.");
            }
        }

        private static bool TableExists(SQLiteConnection connection, string tableName)
        {
            using var command = new SQLiteCommand(
                "SELECT name FROM sqlite_master WHERE type='table' AND name=@tableName", connection);
            command.Parameters.AddWithValue("@tableName", tableName);
            using var reader = command.ExecuteReader();
            return reader.HasRows;
        }
    }
}
