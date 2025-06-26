using ListasAPI.Src.Data;
using ListasAPI.Src.Model;
using ListasAPI.Src.Services;
using ListasAPI.Src.Utils;
using Microsoft.AspNetCore.Identity.Data;
using Microsoft.AspNetCore.Mvc;
using System.Data.SQLite;

namespace ListasAPI.Src.Controllers
{
    [ApiController]
    [Route("api/auth")]
    public class AuthController : ControllerBase
    {
        public AuthController()
        {
            DbInitializer.Initialize(); // Garante criação do banco e tabelas
        }

        [HttpPost("register")]
        public IActionResult Register([FromBody] RegisterRequest request)
        {
            if (request.version != Global.serverVersion)
            {
                return BadRequest(new { status = false, message = "[ VERSAO ERRO ] Versão do cliente não compatível com o servidor." });
            }

            try
            {
                string dataHoraAtual = DateTime.Now.ToString("dd/MM/yyyy HH:mm:ss");
                string passwordHash = BCrypt.Net.BCrypt.HashPassword(request.password);

                using (var connection = new SQLiteConnection(DbInitializer.UsuariosConnectionString))
                {
                    connection.Open();
                    var cmd = new SQLiteCommand("INSERT INTO Users (Nick, Password, Email, Stars, Coins) VALUES (@user, @pass, @email, 0, 0)", connection);
                    cmd.Parameters.AddWithValue("@user", request.nick);
                    cmd.Parameters.AddWithValue("@email", request.email);
                    cmd.Parameters.AddWithValue("@pass", passwordHash);
                    cmd.ExecuteNonQuery();
                }

                string log = $"[ REGISTRO ] Registro realizado com sucesso! Nick: '{request.nick}' | Email: '{request.email}' | Horario: '{dataHoraAtual}'";
                Console.WriteLine(log);

                return Ok(new ApiResponse { status = true, message = "Registrado com sucesso!" });
            }
            catch (SQLiteException ex)
            {
                if (ex.ResultCode == SQLiteErrorCode.Constraint || ex.ResultCode == SQLiteErrorCode.Constraint_Unique)
                {
                    return BadRequest(new ApiResponse { status = false, message = "Nick ou Email já cadastrado." });
                }
                return StatusCode(500, new ApiResponse { status = false, message = "Erro interno no servidor." });
            }
        }

        [HttpPost("login")]
        public IActionResult Login([FromBody] LoginRequest request)
        {
            if (request.version != Global.serverVersion)
            {
                return BadRequest(new { status = false, message = "Versão do cliente não compatível com o servidor." });
            }

            string dataHoraAtual = DateTime.Now.ToString("dd/MM/yyyy HH:mm:ss");

            using (var connection = new SQLiteConnection(DbInitializer.UsuariosConnectionString))
            {
                connection.Open();
                var cmd = new SQLiteCommand("SELECT Password FROM Users WHERE Nick = @user", connection);
                cmd.Parameters.AddWithValue("@user", request.nick);

                using var reader = cmd.ExecuteReader();
                if (reader.Read())
                {
                    string hashSalvo = reader.GetString(0);
                    if (BCrypt.Net.BCrypt.Verify(request.password, hashSalvo))
                    {
                        string accessToken = TokenService.GenerateAccessToken(request.nick);
                        string refreshToken = TokenService.GenerateRefreshToken();
                        DateTime refreshExpiry = DateTime.UtcNow.AddDays(7); // validade do refresh token

                        // Salvar o refresh token no banco
                        var insertRefreshCmd = new SQLiteCommand("INSERT INTO RefreshTokens (UserNick, Token, ExpiresAt) VALUES (@nick, @token, @expires)", connection);
                        insertRefreshCmd.Parameters.AddWithValue("@nick", request.nick);
                        insertRefreshCmd.Parameters.AddWithValue("@token", refreshToken);
                        insertRefreshCmd.Parameters.AddWithValue("@expires", refreshExpiry.ToString("o")); // ISO 8601
                        insertRefreshCmd.ExecuteNonQuery();

                        string log = $"[LOGIN] Login realizado com sucesso! Nick: '{request.nick}' | Horario: '{dataHoraAtual}'";
                        Console.WriteLine(log);

                        return Ok(new LoginResponse
                        {
                            status = true,
                            message = "Login realizado com sucesso!",
                            token = accessToken,
                            refresh_token = refreshToken,
                            nick = request.nick
                        });
                    }
                }
            }

            return Unauthorized(new LoginResponse
            {
                status = false,
                message = "Login inválido!",
                token = string.Empty,
                refresh_token = string.Empty,
                nick = string.Empty
            });
        }

        [HttpPost("refresh")]
        public IActionResult Refresh([FromBody] RefreshRequest request)
        {
            using (var connection = new SQLiteConnection(DbInitializer.UsuariosConnectionString))
            {
                connection.Open();

                var cmd = new SQLiteCommand("SELECT UserNick, ExpiresAt, IsRevoked FROM RefreshTokens WHERE Token = @token", connection);
                cmd.Parameters.AddWithValue("@token", request.refreshToken);

                using var reader = cmd.ExecuteReader();
                if (!reader.Read())
                {
                    return Unauthorized(new { status = false, message = "Refresh token inválido." });
                }

                string nick = reader.GetString(0);
                DateTime expiresAt = DateTime.Parse(reader.GetString(1));
                bool isRevoked = reader.GetInt32(2) == 1;

                if (isRevoked || expiresAt < DateTime.UtcNow)
                {
                    return Unauthorized(new { status = false, message = "Refresh token expirado ou revogado." });
                }

                // Gera novos tokens
                string newAccessToken = TokenService.GenerateAccessToken(nick);
                string newRefreshToken = TokenService.GenerateRefreshToken();
                DateTime newExpiry = DateTime.UtcNow.AddDays(7);

                // Revoga o antigo
                var revokeCmd = new SQLiteCommand("UPDATE RefreshTokens SET IsRevoked = 1 WHERE Token = @old", connection);
                revokeCmd.Parameters.AddWithValue("@old", request.refreshToken);
                revokeCmd.ExecuteNonQuery();

                // Salva o novo refresh token
                var insertCmd = new SQLiteCommand("INSERT INTO RefreshTokens (UserNick, Token, ExpiresAt) VALUES (@nick, @token, @expires)", connection);
                insertCmd.Parameters.AddWithValue("@nick", nick);
                insertCmd.Parameters.AddWithValue("@token", newRefreshToken);
                insertCmd.Parameters.AddWithValue("@expires", newExpiry.ToString("o"));
                insertCmd.ExecuteNonQuery();

                return Ok(new
                {
                    status = true,
                    message = "Token renovado com sucesso.",
                    token = newAccessToken,
                    refresh_token = newRefreshToken
                });
            }
        }
    }
}