using ListasAPI.Src.Data;
using ListasAPI.Src.Model;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Data.SQLite;

namespace ListasAPI.Src.Controllers
{
    [ApiController]
    [Route("api/user")]
    public class UserController : ControllerBase
    {
        [Authorize]
        [HttpGet("info")]
        public IActionResult GetUserInfo()
        {
            // Pega o nome do usuário do token JWT
            var nick = User.Identity?.Name;
            if (string.IsNullOrEmpty(nick))
            {
                return Unauthorized(new { status = false, message = "Usuário não autenticado." });
            }
            using var connection = new SQLiteConnection(DbInitializer.UsuariosConnectionString);
            connection.Open();
            var cmd = new SQLiteCommand("SELECT Email, Stars, Coins FROM Users WHERE Nick = @nick", connection);
            cmd.Parameters.AddWithValue("@nick", nick);
            using var reader = cmd.ExecuteReader();
            if (reader.Read())
            {
                string email = reader.GetString(0);
                int stars = reader.GetInt32(1);
                int coins = reader.GetInt32(2);
                return Ok(new InfoUserResponse { status = true, nick = nick, email = email, stars = stars, coins = coins });
            }
            return NotFound(new { status = false, message = "Usuário não encontrado." });
        }
    }
}
