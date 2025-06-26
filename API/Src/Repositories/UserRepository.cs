using ListasAPI.Src.Data;
using ListasAPI.Src.DTOs;
using System.Data.SQLite;

namespace ListasAPI.Src.Repositories
{
    public class UserRepository : IUserRepository
    {
        public async Task<bool> UserExists(string email)
        {
            using var conn = new SQLiteConnection(DbInitializer.UsuariosConnectionString);
            await conn.OpenAsync();

            var cmd = new SQLiteCommand("SELECT 1 FROM Users WHERE Email = @mail", conn);
            cmd.Parameters.AddWithValue("@mail", email);

            using var reader = await cmd.ExecuteReaderAsync();
            return reader.Read();
        }

        public async Task CreateUser(string nick, string email, string passwordHash)
        {
            using var conn = new SQLiteConnection(DbInitializer.UsuariosConnectionString);
            await conn.OpenAsync();

            var cmd = new SQLiteCommand("INSERT INTO Users (Name, Password, Email, Role) VALUES (@n, @p, @e, 'Comum')", conn);
            cmd.Parameters.AddWithValue("@n", nick);
            cmd.Parameters.AddWithValue("@e", email);
            cmd.Parameters.AddWithValue("@p", passwordHash);
            await cmd.ExecuteNonQueryAsync();
        }

        public async Task<string?> GetPasswordHashByEmail(string email)
        {
            using var conn = new SQLiteConnection(DbInitializer.UsuariosConnectionString);
            await conn.OpenAsync();

            var cmd = new SQLiteCommand("SELECT Password FROM Users WHERE Email = @email", conn);
            cmd.Parameters.AddWithValue("@email", email);

            using var reader = await cmd.ExecuteReaderAsync();
            return reader.Read() ? reader.GetString(0) : null;
        }

        public async Task SaveRefreshToken(string email, string token, DateTime expiresAt)
        {
            using var conn = new SQLiteConnection(DbInitializer.UsuariosConnectionString);
            await conn.OpenAsync();

            var cmd = new SQLiteCommand("INSERT INTO RefreshTokens (UserEmail, Token, ExpiresAt) VALUES (@n, @t, @e)", conn);
            cmd.Parameters.AddWithValue("@n", email);
            cmd.Parameters.AddWithValue("@t", token);
            cmd.Parameters.AddWithValue("@e", expiresAt.ToString("o"));
            await cmd.ExecuteNonQueryAsync();
        }

        public async Task<(string Email, DateTime ExpiresAt, bool IsRevoked)?> GetRefreshToken(string token)
        {
            using var conn = new SQLiteConnection(DbInitializer.UsuariosConnectionString);
            await conn.OpenAsync();

            var cmd = new SQLiteCommand("SELECT UserEmail, ExpiresAt, IsRevoked FROM RefreshTokens WHERE Token = @t", conn);
            cmd.Parameters.AddWithValue("@t", token);

            using var reader = await cmd.ExecuteReaderAsync();
            if (!reader.Read()) return null;

            return (
                reader.GetString(0),
                DateTime.Parse(reader.GetString(1)),
                reader.GetInt32(2) == 1
            );
        }

        public async Task RevokeRefreshToken(string token)
        {
            using var conn = new SQLiteConnection(DbInitializer.UsuariosConnectionString);
            await conn.OpenAsync();

            var cmd = new SQLiteCommand("UPDATE RefreshTokens SET IsRevoked = 1 WHERE Token = @t", conn);
            cmd.Parameters.AddWithValue("@t", token);
            await cmd.ExecuteNonQueryAsync();
        }

        public async Task<UserAuthData?> GetUserByEmail(string email)
        {
            using var conn = new SQLiteConnection(DbInitializer.UsuariosConnectionString);
            await conn.OpenAsync();

            var cmd = new SQLiteCommand("SELECT Name, Password, Role FROM Users WHERE Email = @e", conn);
            cmd.Parameters.AddWithValue("@e", email);

            using var reader = await cmd.ExecuteReaderAsync();
            if (!reader.Read()) return null;

            return new UserAuthData
            {
                name = reader.GetString(0),
                PasswordHash = reader.GetString(1),
                role = reader.GetString(2)
            };
        }
    }
}
